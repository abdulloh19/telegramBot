package com.mnemonic.service;

import com.mnemonic.model.UserProfile;
import com.mnemonic.repository.UserRepository;
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
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private Consumer<SendMessage> messageSender;

    // Chat ID -> Oxirgi eslatma yuborilgan sana (kuniga faqat 1 marta eslatma jo'natish uchun)
    private final Map<Long, LocalDate> lastReminderSentDate = new HashMap<>();

    public ReminderSchedulerService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void setMessageSender(Consumer<SendMessage> messageSender) {
        this.messageSender = messageSender;
    }

    public void start() {
        System.out.println("⏰ Kunlik Eslatma (Reminder Scheduler) servisi ishga tushdi.");

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

            // Foydalanuvchi belgilagan soat kelganmi?
            if (profile.getReminderHour() == currentHour) {
                // Bugun hali eslatma yuborilmaganmi?
                LocalDate lastSent = lastReminderSentDate.get(profile.getChatId());
                if (lastSent != null && lastSent.equals(today)) {
                    continue;
                }

                // Foydalanuvchi bugun allaqachon darsni bajargan bo'lsa, eslatish shart emas
                if (today.equals(profile.getLastActiveDate())) {
                    continue;
                }

                sendReminderMessage(profile);
                lastReminderSentDate.put(profile.getChatId(), today);
            }
        }
    }

    private void sendReminderMessage(UserProfile profile) {
        String name = profile.getFirstName() != null ? profile.getFirstName() : "Do'stim";
        int streak = profile.getCurrentStreak();

        StringBuilder sb = new StringBuilder();
        sb.append("⏰ <b>KUNLIK ESLATMA!</b> 🧠\n\n");
        sb.append("Salom, <b>").append(name).append("</b>!\n\n");
        sb.append("📅 Bugungi <b>20 ta yangi mnemonik so'z</b> darsingiz sizni kutmoqda!\n");

        if (streak > 0) {
            sb.append("🔥 <b>Joriy Streak:</b> ").append(streak).append(" kun ketma-ket!\n");
            sb.append("⚠️ <i>Ushbu ajoyib natijangizni yo'qotib qo'ymaslik uchun bugun darsni o'z vaqtida bajaring!</i>\n\n");
        } else {
            sb.append("🚀 <i>Har kuni 20 tadan so'z o'rganib, lug'at boyligingizni 1 oyda 600 taga oshiring!</i>\n\n");
        }

        sb.append("👇 Darsni boshlash uchun quyidagi tugmani bosing:");

        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(profile.getChatId()));
        message.setText(sb.toString());
        message.setParseMode("HTML");

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        List<InlineKeyboardButton> row1 = new ArrayList<>();
        InlineKeyboardButton startLessonBtn = new InlineKeyboardButton("📅 Bugungi 20 ta so'zni boshlash");
        startLessonBtn.setCallbackData("lesson_start");
        row1.add(startLessonBtn);

        List<InlineKeyboardButton> row2 = new ArrayList<>();
        InlineKeyboardButton exerciseBtn = new InlineKeyboardButton("📝 Mashqlarni yechish");
        exerciseBtn.setCallbackData("exercise_start");
        row2.add(exerciseBtn);

        rows.add(row1);
        rows.add(row2);
        markup.setKeyboard(rows);
        message.setReplyMarkup(markup);

        try {
            messageSender.accept(message);
            System.out.println("📬 Eslatma yuborildi: @" + profile.getFirstName() + " (ChatId: " + profile.getChatId() + ")");
        } catch (Exception e) {
            System.err.println("❌ Eslatma yuborishda xatolik (" + profile.getChatId() + "): " + e.getMessage());
        }
    }

    public void stop() {
        scheduler.shutdown();
    }
}
