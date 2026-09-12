package com.mnemonic.service;

import com.mnemonic.model.UserProfile;
import com.mnemonic.model.Word;
import com.mnemonic.repository.UserRepository;
import com.mnemonic.repository.WordRepository;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class ReminderSchedulerService {
    private final UserRepository userRepository;
    private final WordRepository wordRepository;
    private final CreativeContentService creativeContentService;
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private Consumer<SendMessage> messageSender;

    // Chat ID -> Oxirgi eslatma yuborilgan sana (kuniga faqat 1 marta eslatma jo'natish uchun)
    private final Map<Long, LocalDate> lastReminderSentDate = new HashMap<>();

    public ReminderSchedulerService(UserRepository userRepository, WordRepository wordRepository, CreativeContentService creativeContentService) {
        this.userRepository = userRepository;
        this.wordRepository = wordRepository;
        this.creativeContentService = creativeContentService;
    }

    public void setMessageSender(Consumer<SendMessage> messageSender) {
        this.messageSender = messageSender;
    }

    public void start() {
        System.out.println("⏰ Kreativ Kunlik Eslatma (Reminder Scheduler) servisi ishga tushdi.");

        // Har 15 daqiqada tekshirib boradi
        scheduler.scheduleAtFixedRate(this::checkAndSendReminders, 1, 15, TimeUnit.MINUTES);
    }

    private void checkAndSendReminders() {
        if (messageSender == null) return;

        LocalTime nowTime = LocalTime.now();
        LocalDate today = LocalDate.now();
        int currentHour = nowTime.getHour();

        List<UserProfile> profiles = userRepository.getAllProfiles();

        for (UserProfile profile : profiles) {
            if (!profile.isReminderEnabled()) {
                continue;
            }

            // 1. 3 kun va undan ortiq kirmagan foydalanuvchilarga "Sizni sog'indik" engagement xabari
            LocalDate lastActive = profile.getLastActiveDate();
            if (lastActive != null) {
                long daysAbsent = java.time.temporal.ChronoUnit.DAYS.between(lastActive, today);
                if (daysAbsent >= 3) {
                    LocalDate lastSent = lastReminderSentDate.get(profile.getChatId());
                    if (lastSent == null || !lastSent.equals(today)) {
                        sendWeMissYouMessage(profile, daysAbsent);
                        lastReminderSentDate.put(profile.getChatId(), today);
                        profile.setLastReminderDate(today);
                        profile.setEnteredAfterReminder(false);
                        userRepository.save(profile);
                    }
                    continue;
                }
            }

            // 2. Kunlik majburiy eslatma (har kuni foydalanuvchini botga kirishga undash uchun)
            if (profile.getReminderHour() == currentHour) {
                // Bugun hali eslatma yuborilmaganmi?
                LocalDate lastSent = lastReminderSentDate.get(profile.getChatId());
                if (lastSent != null && lastSent.equals(today)) {
                    continue;
                }

                // Foydalanuvchi bugun allaqachon darsni bajargan bo'lsa, eslatish shart emas
                if (today.equals(profile.getLastActiveDate()) && profile.isTodayLessonCompleted()) {
                    continue;
                }

                sendReminderMessage(profile);
                lastReminderSentDate.put(profile.getChatId(), today);
                profile.setLastReminderDate(today);
                profile.setEnteredAfterReminder(false);
                userRepository.save(profile);
            }
        }
    }

    private void sendWeMissYouMessage(UserProfile profile, long daysAbsent) {
        String weMissYouText = creativeContentService.buildWeMissYouReminder(profile, daysAbsent);

        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(profile.getChatId()));
        message.setText(weMissYouText);
        message.setParseMode("HTML");

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        List<InlineKeyboardButton> row1 = new ArrayList<>();
        InlineKeyboardButton lessonBtn = new InlineKeyboardButton("🚀 Darsga qaytish & Streakni saqlash!");
        lessonBtn.setCallbackData("lesson_start");
        row1.add(lessonBtn);
        rows.add(row1);

        String webUrl = System.getenv("WEBAPP_URL") != null ? System.getenv("WEBAPP_URL") : "https://yrs-jvc-minutes-substance.trycloudflare.com";
        List<InlineKeyboardButton> row2 = new ArrayList<>();
        InlineKeyboardButton webBtn = new InlineKeyboardButton("📱 Super Ilovada Darsni Ochish");
        webBtn.setWebApp(new org.telegram.telegrambots.meta.api.objects.webapp.WebAppInfo(webUrl));
        row2.add(webBtn);
        rows.add(row2);

        markup.setKeyboard(rows);
        message.setReplyMarkup(markup);

        try {
            messageSender.accept(message);
            System.out.println("💌 'Sizni sog'indik' (3+ kun kirmagan) xabari yuborildi: @" + profile.getFirstName() + " (" + profile.getChatId() + ")");
        } catch (Exception e) {
            System.err.println("❌ 'Sizni sog'indik' xabarini yuborishda xatolik (" + profile.getChatId() + "): " + e.getMessage());
        }
    }

    private void sendReminderMessage(UserProfile profile) {
        // Bugungi darsdagi yoki tanlangan darajadagi e'tiborga molik "Kun So'zi"
        List<Word> dayWords = wordRepository.getWordsForDayAndLevel(profile.getCurrentDayIndex(), profile.getSelectedLevel(), profile.getTargetLanguage());
        Word featuredWord = null;
        if (!dayWords.isEmpty()) {
            int featuredIdx = profile.getCurrentDayIndex() % dayWords.size();
            featuredWord = dayWords.get(featuredIdx);
        } else {
            featuredWord = wordRepository.getRandomWord(profile.getTargetLanguage()).orElse(null);
        }

        String creativeText = creativeContentService.buildCreativeReminder(profile, featuredWord);

        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(profile.getChatId()));
        message.setText(creativeText);
        message.setParseMode("HTML");

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        if (featuredWord != null) {
            List<InlineKeyboardButton> audioRow = new ArrayList<>();
            InlineKeyboardButton audioBtn = new InlineKeyboardButton("🔊 Kun so'zi talaffuzini eshitish");
            audioBtn.setCallbackData("audio_word_" + featuredWord.getEnglishWord().toLowerCase());
            audioRow.add(audioBtn);
            rows.add(audioRow);
        }

        List<InlineKeyboardButton> row1 = new ArrayList<>();
        InlineKeyboardButton startLessonBtn = new InlineKeyboardButton("📅 Bugungi 20 ta so'zni boshlash");
        startLessonBtn.setCallbackData("lesson_start");
        row1.add(startLessonBtn);
        rows.add(row1);

        List<InlineKeyboardButton> row2 = new ArrayList<>();
        InlineKeyboardButton exerciseBtn = new InlineKeyboardButton("📝 Mashqlarni yechish");
        exerciseBtn.setCallbackData("exercise_start");
        row2.add(exerciseBtn);
        rows.add(row2);

        markup.setKeyboard(rows);
        message.setReplyMarkup(markup);

        try {
            messageSender.accept(message);
            System.out.println("📬 Kreativ eslatma yuborildi: @" + profile.getFirstName() + " (ChatId: " + profile.getChatId() + ")");
        } catch (Exception e) {
            System.err.println("❌ Eslatma yuborishda xatolik (" + profile.getChatId() + "): " + e.getMessage());
        }
    }

    public void stop() {
        scheduler.shutdown();
    }
}
