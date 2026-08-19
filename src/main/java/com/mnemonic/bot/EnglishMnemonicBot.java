package com.mnemonic.bot;

import com.mnemonic.model.Exercise;
import com.mnemonic.model.QuizQuestion;
import com.mnemonic.model.UserProfile;
import com.mnemonic.model.Word;
import com.mnemonic.model.WordLevel;
import com.mnemonic.repository.UserRepository;
import com.mnemonic.repository.WordRepository;
import com.mnemonic.service.DailyLessonService;
import com.mnemonic.service.ExerciseService;
import com.mnemonic.service.QuizService;
import com.mnemonic.service.StreakService;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class EnglishMnemonicBot extends TelegramLongPollingBot {

    private final String botUsername;
    private final String botToken;

    private final WordRepository wordRepository;
    private final UserRepository userRepository;
    private final StreakService streakService;
    private final DailyLessonService dailyLessonService;
    private final ExerciseService exerciseService;
    private final QuizService quizService;

    // Chat ID -> Hozirgi viktorina savoli
    private final Map<Long, QuizQuestion> currentQuizzes = new ConcurrentHashMap<>();

    // Chat ID -> Foydalanuvchining aktiv mashqlar sessiyasi
    private final Map<Long, ExerciseSession> activeExerciseSessions = new ConcurrentHashMap<>();

    public EnglishMnemonicBot(String botUsername, String botToken) {
        this.botUsername = botUsername;
        this.botToken = botToken;

        this.wordRepository = new WordRepository();
        this.userRepository = new UserRepository();
        this.streakService = new StreakService(userRepository);
        this.dailyLessonService = new DailyLessonService(wordRepository, userRepository, streakService);
        this.exerciseService = new ExerciseService(wordRepository, userRepository, streakService);
        this.quizService = new QuizService(wordRepository);
    }

    public UserRepository getUserRepository() {
        return userRepository;
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            handleTextMessage(update);
        } else if (update.hasCallbackQuery()) {
            handleCallbackQuery(update);
        }
    }

    private void handleTextMessage(Update update) {
        String text = update.getMessage().getText().trim();
        long chatId = update.getMessage().getChatId();
        String firstName = update.getMessage().getFrom().getFirstName();

        UserProfile profile = userRepository.getOrCreate(chatId, firstName);

        switch (text) {
            case "/start":
            case "/menu":
            case "🏠 Asosiy Menyu":
                sendWelcomeMessage(chatId, profile);
                break;

            case "📅 Kunlik 20 ta so'z":
                sendDailyLesson(chatId, profile, false, 0);
                break;

            case "📝 Kunlik Mashqlar":
                startDailyExercises(chatId, profile, false, 0);
                break;

            case "🔥 Streak & Natijalarim":
                sendStreakStats(chatId, profile);
                break;

            case "⏰ Eslatma sozlamalari":
                sendReminderSettings(chatId, profile, false, 0);
                break;

            case "🎲 Tasodifiy so'z":
                sendRandomWord(chatId, profile);
                break;

            case "📚 Darajalar":
                sendLevelsMenu(chatId);
                break;

            case "🎮 Tezkor Test":
                sendNewQuiz(chatId);
                break;

            case "💡 Mnemonika nima?":
                sendMnemonicGuide(chatId);
                break;

            case "🔍 Qidiruv":
                sendMessage(chatId, "🔍 <b>So'z qidirish:</b>\n\nIstalgan inglizcha yoki o'zbekcha so'zni yozib yuboring (Masalan: <code>abandon</code>, <code>frugal</code>, <code>qaysar</code>). Bot uning mnemonikasini topib beradi.");
                break;

            default:
                handleWordSearch(chatId, profile, text);
                break;
        }
    }

    private void handleCallbackQuery(Update update) {
        String data = update.getCallbackQuery().getData();
        long chatId = update.getCallbackQuery().getMessage().getChatId();
        int messageId = update.getCallbackQuery().getMessage().getMessageId();
        String callbackId = update.getCallbackQuery().getId();
        String firstName = update.getCallbackQuery().getFrom().getFirstName();

        UserProfile profile = userRepository.getOrCreate(chatId, firstName);
        answerCallback(callbackId);

        if (data.equals("lesson_start")) {
            sendDailyLesson(chatId, profile, true, messageId);
        } else if (data.equals("lesson_next")) {
            dailyLessonService.nextWord(profile);
            sendDailyLesson(chatId, profile, true, messageId);
        } else if (data.equals("lesson_prev")) {
            dailyLessonService.previousWord(profile);
            sendDailyLesson(chatId, profile, true, messageId);
        } else if (data.equals("lesson_finish")) {
            handleFinishDailyLesson(chatId, profile, messageId);
        } else if (data.equals("exercise_start")) {
            startDailyExercises(chatId, profile, true, messageId);
        } else if (data.startsWith("exercise_ans_")) {
            int selectedIdx = Integer.parseInt(data.replace("exercise_ans_", ""));
            handleExerciseAnswer(chatId, profile, messageId, selectedIdx);
        } else if (data.equals("exercise_next_question")) {
            sendCurrentExerciseQuestion(chatId, profile, true, messageId);
        } else if (data.equals("reminder_toggle")) {
            profile.setReminderEnabled(!profile.isReminderEnabled());
            userRepository.save(profile);
            sendReminderSettings(chatId, profile, true, messageId);
        } else if (data.startsWith("reminder_set_")) {
            int hour = Integer.parseInt(data.replace("reminder_set_", ""));
            profile.setReminderHour(hour);
            profile.setReminderEnabled(true);
            userRepository.save(profile);
            sendReminderSettings(chatId, profile, true, messageId);
        } else if (data.equals("random_word")) {
            sendRandomWord(chatId, profile);
        } else if (data.equals("quiz_next")) {
            sendNewQuiz(chatId);
        } else if (data.startsWith("level_")) {
            handleLevelSelection(chatId, profile, data);
        } else if (data.startsWith("quiz_opt_")) {
            int selectedIndex = Integer.parseInt(data.replace("quiz_opt_", ""));
            handleQuizAnswer(chatId, messageId, selectedIndex);
        }
    }

    private void sendWelcomeMessage(long chatId, UserProfile profile) {
        String name = profile.getFirstName() != null ? profile.getFirstName() : "Do'stim";
        int streak = profile.getCurrentStreak();

        String welcomeText = "👋 <b>Assalomu alaykum, " + escapeHtml(name) + "!</b>\n\n" +
                "🧠 <b>Ingliz Tili Mnemonika Botiga</b> xush kelibsiz!\n\n" +
                "Har kuni <b>20 ta yangi so'zni</b> jonli obrazlar va esda qolarli hikoyalar bilan o'rganing va 🔥 <b>Streak</b> to'plab boring!\n\n" +
                "🔥 <b>Joriy Streak:</b> " + streak + " kun\n" +
                "📚 <b>Bugungi Dars:</b> " + profile.getCurrentDayIndex() + "-kunlik to'plam (20 ta so'z)\n\n" +
                "👇 Kerakli bo'limni tanlang:";

        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(welcomeText);
        message.setParseMode("HTML");
        message.setReplyMarkup(createMainMenuReplyKeyboard());

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    // =========================================================================
    // 📅 KUNLIK 20 TA SO'Z DARS BOSHQARUVI
    // =========================================================================
    private void sendDailyLesson(long chatId, UserProfile profile, boolean isEdit, int messageId) {
        String lessonText = dailyLessonService.formatLessonCard(profile);
        List<Word> todayWords = dailyLessonService.getTodayWords(profile);
        int currentWordIdx = profile.getCurrentWordInDay();
        int totalWords = todayWords.size();

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        List<InlineKeyboardButton> navRow = new ArrayList<>();
        if (currentWordIdx > 0) {
            InlineKeyboardButton prevBtn = new InlineKeyboardButton("◀️ Oldingi");
            prevBtn.setCallbackData("lesson_prev");
            navRow.add(prevBtn);
        }

        InlineKeyboardButton countBtn = new InlineKeyboardButton("📌 " + (currentWordIdx + 1) + "/" + totalWords);
        countBtn.setCallbackData("noop");
        navRow.add(countBtn);

        if (currentWordIdx < totalWords - 1) {
            InlineKeyboardButton nextBtn = new InlineKeyboardButton("Keyingi ▶️");
            nextBtn.setCallbackData("lesson_next");
            navRow.add(nextBtn);
        }
        rows.add(navRow);

        // Yakunlash va mashq tugmasi
        List<InlineKeyboardButton> actionRow = new ArrayList<>();
        InlineKeyboardButton finishBtn = new InlineKeyboardButton("✅ Darsni tugatish & Mashqlarga o'tish");
        finishBtn.setCallbackData("lesson_finish");
        actionRow.add(finishBtn);
        rows.add(actionRow);

        markup.setKeyboard(rows);

        if (isEdit && messageId > 0) {
            EditMessageText edit = new EditMessageText();
            edit.setChatId(String.valueOf(chatId));
            edit.setMessageId(messageId);
            edit.setText(lessonText);
            edit.setParseMode("HTML");
            edit.setReplyMarkup(markup);
            try {
                execute(edit);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        } else {
            SendMessage message = new SendMessage();
            message.setChatId(String.valueOf(chatId));
            message.setText(lessonText);
            message.setParseMode("HTML");
            message.setReplyMarkup(markup);
            try {
                execute(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleFinishDailyLesson(long chatId, UserProfile profile, int messageId) {
        StreakService.StreakResult streakResult = dailyLessonService.completeTodayLesson(profile);

        StringBuilder sb = new StringBuilder();
        sb.append("🎉 <b>TABRIKLAYMIZ! BUGUNGI 20 TA SO'ZNI O'RGANDINGIZ!</b> 🎓\n\n");
        sb.append("🔥 <b>Streak:</b> ").append(streakResult.getCurrentStreak()).append(" kun ketma-ket!\n");
        sb.append("🏆 <b>Rekord:</b> ").append(streakResult.getMaxStreak()).append(" kun\n");
        sb.append("📚 <b>Jami o'rganilgan so'zlar:</b> ").append(profile.getTotalWordsLearned()).append(" ta\n\n");
        sb.append("💡 <i>Endi so'zlarni mustahkamlash uchun 5 ta qiziqarli mashqni bajaring!</i>");

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> row1 = new ArrayList<>();

        InlineKeyboardButton exBtn = new InlineKeyboardButton("📝 Mashqlarni boshlash (5 ta test)");
        exBtn.setCallbackData("exercise_start");
        row1.add(exBtn);
        rows.add(row1);
        markup.setKeyboard(rows);

        EditMessageText edit = new EditMessageText();
        edit.setChatId(String.valueOf(chatId));
        edit.setMessageId(messageId);
        edit.setText(sb.toString());
        edit.setParseMode("HTML");
        edit.setReplyMarkup(markup);

        try {
            execute(edit);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    // =========================================================================
    // 📝 KUNLIK MASHQLAR (EXERCISES)
    // =========================================================================
    private void startDailyExercises(long chatId, UserProfile profile, boolean isEdit, int messageId) {
        List<Exercise> exercises = exerciseService.generateDailyExerciseSet(profile);
        ExerciseSession session = new ExerciseSession(exercises);
        activeExerciseSessions.put(chatId, session);

        sendCurrentExerciseQuestion(chatId, profile, isEdit, messageId);
    }

    private void sendCurrentExerciseQuestion(long chatId, UserProfile profile, boolean isEdit, int messageId) {
        ExerciseSession session = activeExerciseSessions.get(chatId);
        if (session == null || session.isCompleted()) {
            sendMessage(chatId, "Mashqlar topilmadi. '📝 Kunlik Mashqlar' tugmasi orqali yangi mashq boshlang.");
            return;
        }

        Exercise currentEx = session.getCurrentExercise();

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        for (int i = 0; i < currentEx.getOptions().size(); i++) {
            List<InlineKeyboardButton> row = new ArrayList<>();
            InlineKeyboardButton btn = new InlineKeyboardButton(currentEx.getOptions().get(i));
            btn.setCallbackData("exercise_ans_" + i);
            row.add(btn);
            rows.add(row);
        }
        markup.setKeyboard(rows);

        if (isEdit && messageId > 0) {
            EditMessageText edit = new EditMessageText();
            edit.setChatId(String.valueOf(chatId));
            edit.setMessageId(messageId);
            edit.setText(currentEx.getQuestionPrompt());
            edit.setParseMode("HTML");
            edit.setReplyMarkup(markup);
            try {
                execute(edit);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        } else {
            SendMessage msg = new SendMessage();
            msg.setChatId(String.valueOf(chatId));
            msg.setText(currentEx.getQuestionPrompt());
            msg.setParseMode("HTML");
            msg.setReplyMarkup(markup);
            try {
                execute(msg);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleExerciseAnswer(long chatId, UserProfile profile, int messageId, int selectedIdx) {
        ExerciseSession session = activeExerciseSessions.get(chatId);
        if (session == null || session.isCompleted()) {
            return;
        }

        Exercise currentEx = session.getCurrentExercise();
        boolean isCorrect = (selectedIdx == currentEx.getCorrectOptionIndex());

        if (isCorrect) {
            session.incrementCorrect();
        }

        StringBuilder sb = new StringBuilder();
        if (isCorrect) {
            sb.append("🎉 <b>To'g'ri javob!</b> ✅\n\n");
        } else {
            sb.append("❌ <b>Noto'g'ri!</b>\n");
            sb.append("To'g'ri javob: <b>").append(escapeHtml(currentEx.getOptions().get(currentEx.getCorrectOptionIndex()))).append("</b>\n\n");
        }

        sb.append("💡 <b>Eslab qolish uchun mnemonika:</b>\n");
        sb.append(currentEx.getRelatedWord().toFormattedCard());

        session.nextQuestion();

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> row1 = new ArrayList<>();

        if (!session.isCompleted()) {
            InlineKeyboardButton nextBtn = new InlineKeyboardButton("➡️ Keyingi mashq (" + (session.getCurrentIndex() + 1) + "/" + session.getTotalQuestions() + ")");
            nextBtn.setCallbackData("exercise_next_question");
            row1.add(nextBtn);
        } else {
            InlineKeyboardButton finishBtn = new InlineKeyboardButton("🏁 Mashqlarni yakunlash");
            finishBtn.setCallbackData("exercise_next_question");
            row1.add(finishBtn);
        }
        rows.add(row1);
        markup.setKeyboard(rows);

        EditMessageText edit = new EditMessageText();
        edit.setChatId(String.valueOf(chatId));
        edit.setMessageId(messageId);
        edit.setText(sb.toString());
        edit.setParseMode("HTML");
        edit.setReplyMarkup(markup);

        try {
            execute(edit);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

        // Agar barcha savollar tugagan bo'lsa natijani saqlaymiz
        if (session.isCompleted()) {
            StreakService.StreakResult streakRes = exerciseService.completeExerciseSession(profile, session.getCorrectCount());
            activeExerciseSessions.remove(chatId);
        }
    }

    // =========================================================================
    // 🔥 STREAK VA NATIJALAR
    // =========================================================================
    private void sendStreakStats(long chatId, UserProfile profile) {
        String card = streakService.formatStreakCard(profile);

        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(card);
        message.setParseMode("HTML");

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        List<InlineKeyboardButton> row1 = new ArrayList<>();
        InlineKeyboardButton lessonBtn = new InlineKeyboardButton("📅 Bugungi 20 ta so'z");
        lessonBtn.setCallbackData("lesson_start");
        row1.add(lessonBtn);

        InlineKeyboardButton exBtn = new InlineKeyboardButton("📝 Mashqlar");
        exBtn.setCallbackData("exercise_start");
        row1.add(exBtn);

        rows.add(row1);
        markup.setKeyboard(rows);
        message.setReplyMarkup(markup);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    // =========================================================================
    // ⏰ KUNLIK ESLATMA SOZLAMALARI
    // =========================================================================
    private void sendReminderSettings(long chatId, UserProfile profile, boolean isEdit, int messageId) {
        String status = profile.isReminderEnabled() ? "🟢 Yoqilgan" : "🔴 O'chirilgan";
        String timeStr = String.format("%02d:00", profile.getReminderHour());

        StringBuilder sb = new StringBuilder();
        sb.append("⏰ <b>KUNLIK ESLATMA SOZLAMALARI:</b>\n\n");
        sb.append("🔔 <b>Holati:</b> ").append(status).append("\n");
        sb.append("🕒 <b>Eslatma vaqti:</b> Har kuni soat <b>").append(timeStr).append("</b> da\n\n");
        sb.append("💡 <i>Bot har kuni belgilangan vaqtda sizga yangi 20 ta so'zni o'rganishni va Streak'ni saqlashni eslatib turadi.</i>\n\n");
        sb.append("👇 O'zingizga qulay vaqtni tanlang yoki eslatmani o'zgartiring:");

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        List<InlineKeyboardButton> toggleRow = new ArrayList<>();
        InlineKeyboardButton toggleBtn = new InlineKeyboardButton(profile.isReminderEnabled() ? "🔕 Eslatmani o'chirish" : "🔔 Eslatmani yoqish");
        toggleBtn.setCallbackData("reminder_toggle");
        toggleRow.add(toggleBtn);
        rows.add(toggleRow);

        List<InlineKeyboardButton> timeRow1 = new ArrayList<>();
        timeRow1.add(createTimeBtn("🌅 08:00", 8, profile.getReminderHour()));
        timeRow1.add(createTimeBtn("☀️ 12:00", 12, profile.getReminderHour()));
        rows.add(timeRow1);

        List<InlineKeyboardButton> timeRow2 = new ArrayList<>();
        timeRow2.add(createTimeBtn("🌆 18:00", 18, profile.getReminderHour()));
        timeRow2.add(createTimeBtn("🌙 20:00", 20, profile.getReminderHour()));
        timeRow2.add(createTimeBtn("🌌 22:00", 22, profile.getReminderHour()));
        rows.add(timeRow2);

        markup.setKeyboard(rows);

        if (isEdit && messageId > 0) {
            EditMessageText edit = new EditMessageText();
            edit.setChatId(String.valueOf(chatId));
            edit.setMessageId(messageId);
            edit.setText(sb.toString());
            edit.setParseMode("HTML");
            edit.setReplyMarkup(markup);
            try {
                execute(edit);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        } else {
            SendMessage msg = new SendMessage();
            msg.setChatId(String.valueOf(chatId));
            msg.setText(sb.toString());
            msg.setParseMode("HTML");
            msg.setReplyMarkup(markup);
            try {
                execute(msg);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    private InlineKeyboardButton createTimeBtn(String label, int hour, int currentHour) {
        String title = (hour == currentHour) ? "✅ " + label : label;
        InlineKeyboardButton btn = new InlineKeyboardButton(title);
        btn.setCallbackData("reminder_set_" + hour);
        return btn;
    }

    // =========================================================================
    // 🎲 TASODIFIY SO'Z & QIDIRUV
    // =========================================================================
    private void sendRandomWord(long chatId, UserProfile profile) {
        Optional<Word> wordOpt = wordRepository.getRandomWord();
        if (wordOpt.isEmpty()) {
            sendMessage(chatId, "Hozircha so'zlar mavjud emas.");
            return;
        }

        Word word = wordOpt.get();
        profile.setTotalWordsLearned(profile.getTotalWordsLearned() + 1);
        userRepository.save(profile);

        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(word.toFormattedCard());
        message.setParseMode("HTML");

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        List<InlineKeyboardButton> row1 = new ArrayList<>();
        InlineKeyboardButton nextBtn = new InlineKeyboardButton("🎲 Yana boshqa so'z");
        nextBtn.setCallbackData("random_word");
        row1.add(nextBtn);

        InlineKeyboardButton quizBtn = new InlineKeyboardButton("🎮 Tezkor test");
        quizBtn.setCallbackData("quiz_next");
        row1.add(quizBtn);

        rows.add(row1);
        markup.setKeyboard(rows);
        message.setReplyMarkup(markup);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void handleWordSearch(long chatId, UserProfile profile, String query) {
        Optional<Word> wordOpt = wordRepository.findByKeyword(query);
        if (wordOpt.isPresent()) {
            Word word = wordOpt.get();
            sendMessage(chatId, "🔍 <b>Topilgan so'z:</b>\n\n" + word.toFormattedCard());
        } else {
            String notFoundText = "😔 Kechirasiz, '<b>" + escapeHtml(query) + "</b>' so'zi bo'yicha mnemonika topilmadi.\n\n" +
                    "💡 <i>Tavsiya:</i> Qidirish uchun inglizcha so'z (masalan: <code>lucid</code>, <code>frugal</code>) yoki menyudagi tugmalardan foydalaning.";
            sendMessage(chatId, notFoundText);
        }
    }

    private void sendLevelsMenu(long chatId) {
        String text = "📚 <b>O'zingizga mos darajani tanlang:</b>\n\n" +
                "🟢 <b>Boshlang'ich (A1-A2):</b> Kundalik va oddiy so'zlar\n" +
                "🟡 <b>O'rta (B1-B2):</b> Erkin muloqot va muhim so'zlar\n" +
                "🔴 <b>Yuqori (C1-C2 / IELTS):</b> Akademik va nufuzli so'zlar";

        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);
        message.setParseMode("HTML");

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        List<InlineKeyboardButton> row1 = new ArrayList<>();
        InlineKeyboardButton b1 = new InlineKeyboardButton("🟢 Boshlang'ich (A1-A2)");
        b1.setCallbackData("level_beginner");
        row1.add(b1);

        List<InlineKeyboardButton> row2 = new ArrayList<>();
        InlineKeyboardButton b2 = new InlineKeyboardButton("🟡 O'rta (B1-B2)");
        b2.setCallbackData("level_intermediate");
        row2.add(b2);

        List<InlineKeyboardButton> row3 = new ArrayList<>();
        InlineKeyboardButton b3 = new InlineKeyboardButton("🔴 Yuqori (C1-C2 / IELTS)");
        b3.setCallbackData("level_advanced");
        row3.add(b3);

        rows.add(row1);
        rows.add(row2);
        rows.add(row3);

        markup.setKeyboard(rows);
        message.setReplyMarkup(markup);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void handleLevelSelection(long chatId, UserProfile profile, String data) {
        WordLevel level;
        if (data.equals("level_beginner")) level = WordLevel.BEGINNER;
        else if (data.equals("level_intermediate")) level = WordLevel.INTERMEDIATE;
        else level = WordLevel.ADVANCED;

        Optional<Word> wordOpt = wordRepository.getRandomWordByLevel(level);
        if (wordOpt.isPresent()) {
            Word word = wordOpt.get();

            SendMessage message = new SendMessage();
            message.setChatId(String.valueOf(chatId));
            message.setText(word.toFormattedCard());
            message.setParseMode("HTML");

            InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
            List<List<InlineKeyboardButton>> rows = new ArrayList<>();

            List<InlineKeyboardButton> row1 = new ArrayList<>();
            InlineKeyboardButton nextBtn = new InlineKeyboardButton("🎲 Shu darajadagi boshqa so'z");
            nextBtn.setCallbackData(data);
            row1.add(nextBtn);

            rows.add(row1);
            markup.setKeyboard(rows);
            message.setReplyMarkup(markup);

            try {
                execute(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    private void sendNewQuiz(long chatId) {
        Optional<QuizQuestion> questionOpt = quizService.generateQuestion();
        if (questionOpt.isEmpty()) {
            sendMessage(chatId, "Test uchun so'zlar yetarli emas.");
            return;
        }

        QuizQuestion question = questionOpt.get();
        currentQuizzes.put(chatId, question);

        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(question.getQuestionText());
        message.setParseMode("HTML");

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        for (int i = 0; i < question.getOptions().size(); i++) {
            List<InlineKeyboardButton> row = new ArrayList<>();
            InlineKeyboardButton btn = new InlineKeyboardButton(question.getOptions().get(i));
            btn.setCallbackData("quiz_opt_" + i);
            row.add(btn);
            rows.add(row);
        }

        markup.setKeyboard(rows);
        message.setReplyMarkup(markup);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void handleQuizAnswer(long chatId, int messageId, int selectedIndex) {
        QuizQuestion question = currentQuizzes.get(chatId);
        if (question == null) {
            sendMessage(chatId, "Savol muddati tugagan. Yangi savol boshlash uchun '🎮 Tezkor Test' tugmasini bosing.");
            return;
        }

        boolean isCorrect = (selectedIndex == question.getCorrectOptionIndex());

        StringBuilder response = new StringBuilder();
        if (isCorrect) {
            response.append("🎉 <b>Ajoyib! To'g'ri javob!</b> ✅\n\n");
        } else {
            response.append("❌ <b>Afsus, noto'g'ri!</b>\n");
            response.append("To'g'ri javob: <b>").append(escapeHtml(question.getOptions().get(question.getCorrectOptionIndex()))).append("</b>\n\n");
        }

        response.append("💡 <b>Eslab qolish uchun mnemonika:</b>\n");
        response.append(question.getRelatedWord().toFormattedCard());

        EditMessageText editMessage = new EditMessageText();
        editMessage.setChatId(String.valueOf(chatId));
        editMessage.setMessageId(messageId);
        editMessage.setText(response.toString());
        editMessage.setParseMode("HTML");

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> row1 = new ArrayList<>();

        InlineKeyboardButton nextQuiz = new InlineKeyboardButton("➡️ Keyingi savol");
        nextQuiz.setCallbackData("quiz_next");
        row1.add(nextQuiz);

        rows.add(row1);
        markup.setKeyboard(rows);
        editMessage.setReplyMarkup(markup);

        try {
            execute(editMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

        currentQuizzes.remove(chatId);
    }

    private void sendMnemonicGuide(long chatId) {
        String guide = "🧠 <b>Mnemonika Nima va U Qanday Ishlaydi?</b>\n\n" +
                "Mnemonika — inson miyasining assotsiativ xotirasidan foydalanib, yangi ma'lumotlarni oson va uzoq muddatga eslab qolish san'atidir.\n\n" +
                "🔑 <b>3 ta Oltin Qoida:</b>\n" +
                "1. <b>Fonetik o'xshashlik:</b> Inglizcha so'z talaffuziga o'xshash o'zbekcha tanish so'z tanlanadi.\n" +
                "2. <b>Jonli va g'ayritabiiy obraz:</b> Miya zerikarli faktlarni emas, kulgili, bo'rttirilgan va harakatli tasvirlarni yaxshi eslab qoladi.\n" +
                "3. <b>Bog'lovchi hikoya:</b> Yangi so'zning asl ma'nosi bilan topilgan obraz bir-biriga mantiqiy bog'lanadi.\n\n" +
                "✨ <b>Misol:</b>\n" +
                "• So'z: <b>Abandon</b> [əˈbændən] — <i>Tashlab ketmoq</i>\n" +
                "• Obraz: <i>A-bandomiz!</i>\n" +
                "• Hikoya: Cho'kayotgan kemadagi qaroqchilar 'A, bandomiz' deb qichqirib, kemani tashlab qochishdi.";

        sendMessage(chatId, guide);
    }

    private ReplyKeyboardMarkup createMainMenuReplyKeyboard() {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow row1 = new KeyboardRow();
        row1.add(new KeyboardButton("📅 Kunlik 20 ta so'z"));
        row1.add(new KeyboardButton("📝 Kunlik Mashqlar"));

        KeyboardRow row2 = new KeyboardRow();
        row2.add(new KeyboardButton("🔥 Streak & Natijalarim"));
        row2.add(new KeyboardButton("⏰ Eslatma sozlamalari"));

        KeyboardRow row3 = new KeyboardRow();
        row3.add(new KeyboardButton("🎲 Tasodifiy so'z"));
        row3.add(new KeyboardButton("📚 Darajalar"));

        KeyboardRow row4 = new KeyboardRow();
        row4.add(new KeyboardButton("🎮 Tezkor Test"));
        row4.add(new KeyboardButton("🔍 Qidiruv"));

        keyboard.add(row1);
        keyboard.add(row2);
        keyboard.add(row3);
        keyboard.add(row4);

        keyboardMarkup.setKeyboard(keyboard);
        return keyboardMarkup;
    }

    public void sendMessage(long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);
        message.setParseMode("HTML");
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void sendDirectMessage(SendMessage message) {
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void answerCallback(String callbackId) {
        try {
            AnswerCallbackQuery answer = new AnswerCallbackQuery();
            answer.setCallbackQueryId(callbackId);
            execute(answer);
        } catch (TelegramApiException e) {
            // Callback javobida xatolik bo'lsa e'tiborsiz qoldirish
        }
    }

    private String escapeHtml(String text) {
        if (text == null) return "";
        return text.replace("&", "&amp;")
                   .replace("<", "&lt;")
                   .replace(">", "&gt;");
    }

    // Ichki mashqlar sessiyasi yordamchi klassi
    private static class ExerciseSession {
        private final List<Exercise> exercises;
        private int currentIndex = 0;
        private int correctCount = 0;

        public ExerciseSession(List<Exercise> exercises) {
            this.exercises = exercises;
        }

        public Exercise getCurrentExercise() {
            return exercises.get(currentIndex);
        }

        public void nextQuestion() {
            currentIndex++;
        }

        public void incrementCorrect() {
            correctCount++;
        }

        public boolean isCompleted() {
            return currentIndex >= exercises.size();
        }

        public int getCurrentIndex() {
            return currentIndex;
        }

        public int getTotalQuestions() {
            return exercises.size();
        }

        public int getCorrectCount() {
            return correctCount;
        }
    }
}
