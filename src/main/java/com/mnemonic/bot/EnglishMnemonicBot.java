package com.mnemonic.bot;

import com.mnemonic.model.Exercise;
import com.mnemonic.model.QuizQuestion;
import com.mnemonic.model.TargetLanguage;
import com.mnemonic.model.UserProfile;
import com.mnemonic.model.Word;
import com.mnemonic.model.WordLevel;
import com.mnemonic.repository.UserRepository;
import com.mnemonic.repository.WordRepository;
import com.mnemonic.service.AudioPronunciationService;
import com.mnemonic.service.CreativeContentService;
import com.mnemonic.service.DailyLessonService;
import com.mnemonic.service.ExerciseService;
import com.mnemonic.service.QuizService;
import com.mnemonic.service.StreakService;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendVoice;
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
    private final AudioPronunciationService audioService;
    private final CreativeContentService creativeContentService;
    private final com.mnemonic.service.DialogueService dialogueService;

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
        this.audioService = new AudioPronunciationService();
        this.creativeContentService = new CreativeContentService();
        this.dialogueService = new com.mnemonic.service.DialogueService();
    }

    public com.mnemonic.service.DialogueService getDialogueService() {
        return dialogueService;
    }

    public UserRepository getUserRepository() {
        return userRepository;
    }

    public WordRepository getWordRepository() {
        return wordRepository;
    }

    public CreativeContentService getCreativeContentService() {
        return creativeContentService;
    }

    public AudioPronunciationService getAudioService() {
        return audioService;
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

        // Agar foydalanuvchi hali tilni tanlamagan bo'lsa, avval til so'raymiz
        if (profile.getTargetLanguage() == null && !text.equals("/start")) {
            sendInitialLanguagePrompt(chatId, profile);
            return;
        }

        // Agar foydalanuvchi hali darajasini tanlamagan bo'lsa, avval daraja so'raymiz
        if (profile.getSelectedLevel() == null && !text.equals("/start")) {
            sendInitialLevelPrompt(chatId, profile);
            return;
        }

        switch (text) {
            case "/start":
            case "/menu":
            case "🏠 Asosiy Menyu":
                if (profile.getTargetLanguage() == null) {
                    sendInitialLanguagePrompt(chatId, profile);
                } else if (profile.getSelectedLevel() == null) {
                    sendInitialLevelPrompt(chatId, profile);
                } else {
                    sendWelcomeMessage(chatId, profile);
                }
                break;

            case "🌐 Tilni o'zgartirish":
            case "/language":
            case "/til":
                sendInitialLanguagePrompt(chatId, profile);
                break;

            case "📅 Kunlik 20 ta so'z":
                sendDailyLesson(chatId, profile, false, 0);
                break;

            case "🗣️ Kunlik Dialog":
            case "🗣️ Kunlik Dialog (Speaking)":
            case "/dialog":
                sendDailyDialogue(chatId, profile);
                break;

            case "📝 Kunlik Mashqlar":
                startDailyExercises(chatId, profile, false, 0);
                break;

            case "🔥 Streak & Natijalarim":
                sendStreakStats(chatId, profile);
                break;

            case "🎯 Darajani o'zgartirish":
                sendInitialLevelPrompt(chatId, profile);
                break;

            case "⏰ Eslatma sozlamalari":
                sendReminderSettings(chatId, profile, false, 0);
                break;

            case "🎲 Tasodifiy so'z":
                sendRandomWord(chatId, profile);
                break;

            case "📚 Darajalar":
                sendLevelsMenu(chatId, profile);
                break;

            case "🎮 Tezkor Test":
                sendNewQuiz(chatId);
                break;

            case "💡 Mnemonika nima?":
                sendMnemonicGuide(chatId);
                break;

            case "🔍 Qidiruv":
                String langName = (profile.getTargetLanguage() == TargetLanguage.RUSSIAN) ? "ruscha" : "inglizcha";
                sendMessage(chatId, "🔍 <b>So'z qidirish (" + (profile.getTargetLanguage() == TargetLanguage.RUSSIAN ? "🇷🇺" : "🇬🇧") + "):</b>\n\nIstalgan " + langName + " yoki o'zbekcha so'zni yozib yuboring. Bot uning mnemonikasini topib beradi va audio talaffuzini eshitish imkonini taqdim etadi.");
                break;

            case "📱 Web Ilova (Next.js Mini App)":
            case "/app":
            case "/webapp":
                sendWebAppInfoMessage(chatId, profile);
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

        if (data.startsWith("set_lang_")) {
            handleSetUserLanguage(chatId, profile, data, messageId);
        } else if (data.startsWith("set_level_")) {
            handleSetUserLevel(chatId, profile, data, messageId);
        } else if (data.equals("lesson_start")) {
            sendDailyLesson(chatId, profile, true, messageId);
        } else if (data.startsWith("lesson_next")) {
            if (data.startsWith("lesson_next_")) {
                int nextIdx = Integer.parseInt(data.replace("lesson_next_", ""));
                profile.setCurrentWordInDay(nextIdx);
                userRepository.save(profile);
            } else {
                dailyLessonService.nextWord(profile);
            }
            sendDailyLesson(chatId, profile, true, messageId);
        } else if (data.startsWith("lesson_prev")) {
            if (data.startsWith("lesson_prev_")) {
                int prevIdx = Integer.parseInt(data.replace("lesson_prev_", ""));
                profile.setCurrentWordInDay(prevIdx);
                userRepository.save(profile);
            } else {
                dailyLessonService.previousWord(profile);
            }
            sendDailyLesson(chatId, profile, true, messageId);
        } else if (data.startsWith("lesson_audio_next")) {
            int targetIdx;
            if (data.startsWith("lesson_audio_next_")) {
                targetIdx = Integer.parseInt(data.replace("lesson_audio_next_", ""));
            } else {
                targetIdx = profile.getCurrentWordInDay();
            }
            List<Word> todayWords = dailyLessonService.getTodayWords(profile);
            if (targetIdx >= 0 && targetIdx < todayWords.size()) {
                Word curWord = todayWords.get(targetIdx);
                sendWordAudio(chatId, curWord);
                int nextIdx = Math.min(targetIdx + 1, todayWords.size() - 1);
                profile.setCurrentWordInDay(nextIdx);
                userRepository.save(profile);
                sendDailyLesson(chatId, profile, false, 0);
            }
        } else if (data.startsWith("lesson_audio_finish")) {
            int targetIdx;
            if (data.startsWith("lesson_audio_finish_")) {
                targetIdx = Integer.parseInt(data.replace("lesson_audio_finish_", ""));
            } else {
                targetIdx = profile.getCurrentWordInDay();
            }
            List<Word> todayWords = dailyLessonService.getTodayWords(profile);
            if (targetIdx >= 0 && targetIdx < todayWords.size()) {
                Word curWord = todayWords.get(targetIdx);
                sendWordAudio(chatId, curWord);
            }
            handleFinishDailyLesson(chatId, profile, 0);
        } else if (data.startsWith("lesson_audio_current")) {
            int targetIdx;
            if (data.startsWith("lesson_audio_current_")) {
                targetIdx = Integer.parseInt(data.replace("lesson_audio_current_", ""));
            } else {
                targetIdx = profile.getCurrentWordInDay();
            }
            List<Word> todayWords = dailyLessonService.getTodayWords(profile);
            if (targetIdx >= 0 && targetIdx < todayWords.size()) {
                Word curWord = todayWords.get(targetIdx);
                sendWordAudio(chatId, curWord);
            }
        } else if (data.equals("lesson_finish")) {
            handleFinishDailyLesson(chatId, profile, messageId);
        } else if (data.equals("dialog_show")) {
            sendDailyDialogue(chatId, profile);
        } else if (data.startsWith("dialog_audio_")) {
            String dialogId = data.replace("dialog_audio_", "");
            sendDialogueAudio(chatId, dialogId, profile);
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
        } else if (data.startsWith("explore_level_")) {
            handleExploreLevel(chatId, profile, data);
        } else if (data.startsWith("quiz_opt_")) {
            int selectedIndex = Integer.parseInt(data.replace("quiz_opt_", ""));
            handleQuizAnswer(chatId, messageId, selectedIndex);
        } else if (data.startsWith("audio_word_")) {
            String wordText = data.replace("audio_word_", "");
            sendWordAudio(chatId, wordText);
        } else if (data.startsWith("exercise_audio_next_")) {
            String wordText = data.replace("exercise_audio_next_", "");
            sendWordAudio(chatId, wordText);
            sendCurrentExerciseQuestion(chatId, profile, false, 0);
        } else if (data.startsWith("quiz_audio_next_")) {
            String wordText = data.replace("quiz_audio_next_", "");
            sendWordAudio(chatId, wordText);
            sendNewQuiz(chatId);
        }
    }

    // =========================================================================
    // 🌐 TILNI SO'RASH VA TANLASH (INGLIZ TILI / RUS TILI)
    // =========================================================================
    private void sendInitialLanguagePrompt(long chatId, UserProfile profile) {
        String name = profile.getFirstName() != null ? profile.getFirstName() : "Do'stim";

        String text = "👋 <b>Assalomu alaykum, " + escapeHtml(name) + "!</b>\n\n" +
                "🧠 <b>Mnemonika Ta'lim Botiga xush kelibsiz!</b>\n\n" +
                "Qaysi tilni mnemonika (assotsiatsiya, audio va jonli obrazlar) usulida o'rganmoqchisiz?\n\n" +
                "👇 <b>Iltimos, o'rganmoqchi bo'lgan tilingizni tanlang:</b>";

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        List<InlineKeyboardButton> r1 = new ArrayList<>();
        InlineKeyboardButton b1 = new InlineKeyboardButton("🇬🇧 Ingliz tili (English)");
        b1.setCallbackData("set_lang_en");
        r1.add(b1);

        List<InlineKeyboardButton> r2 = new ArrayList<>();
        InlineKeyboardButton b2 = new InlineKeyboardButton("🇷🇺 Rus tili (Русский язык)");
        b2.setCallbackData("set_lang_ru");
        r2.add(b2);

        rows.add(r1);
        rows.add(r2);
        markup.setKeyboard(rows);

        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);
        message.setParseMode("HTML");
        message.setReplyMarkup(markup);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void handleSetUserLanguage(long chatId, UserProfile profile, String data, int messageId) {
        TargetLanguage lang = data.equals("set_lang_ru") ? TargetLanguage.RUSSIAN : TargetLanguage.ENGLISH;
        profile.setTargetLanguage(lang);
        profile.setSelectedLevel(null); // yangi til uchun daraja so'raladi
        profile.setCurrentWordInDay(0);
        userRepository.save(profile);

        sendInitialLevelPrompt(chatId, profile);
    }

    // =========================================================================
    // 🎯 DARAJANI SO'RASH VA TANLASH
    // =========================================================================
    private void sendInitialLevelPrompt(long chatId, UserProfile profile) {
        String name = profile.getFirstName() != null ? profile.getFirstName() : "Do'stim";
        TargetLanguage lang = (profile.getTargetLanguage() != null) ? profile.getTargetLanguage() : TargetLanguage.ENGLISH;
        String langTitle = (lang == TargetLanguage.RUSSIAN) ? "🇷🇺 Rus Tili Mnemonika Tizimi" : "🇬🇧 Ingliz Tili Mnemonika Tizimi";
        String langWord = (lang == TargetLanguage.RUSSIAN) ? "rus tili" : "ingliz tili";

        String text = "👋 <b>Assalomu alaykum, " + escapeHtml(name) + "!</b>\n\n" +
                "🧠 <b>" + langTitle + "ga xush kelibsiz!</b>\n\n" +
                "Sizga eng mos so'zlar, ovozli talaffuzlar va mashqlarni taqdim etishimiz uchun, iltimos, <b>" + langWord + " darajangizni tanlang:</b>";

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        List<InlineKeyboardButton> r1 = new ArrayList<>();
        InlineKeyboardButton b1 = new InlineKeyboardButton("🟢 Boshlang'ich (A1-A2)");
        b1.setCallbackData("set_level_beginner");
        r1.add(b1);

        List<InlineKeyboardButton> r2 = new ArrayList<>();
        InlineKeyboardButton b2 = new InlineKeyboardButton("🟡 O'rta (B1-B2)");
        b2.setCallbackData("set_level_intermediate");
        r2.add(b2);

        List<InlineKeyboardButton> r3 = new ArrayList<>();
        InlineKeyboardButton b3 = new InlineKeyboardButton("🔴 Yuqori (C1-C2)");
        b3.setCallbackData("set_level_advanced");
        r3.add(b3);

        rows.add(r1);
        rows.add(r2);
        rows.add(r3);
        markup.setKeyboard(rows);

        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);
        message.setParseMode("HTML");
        message.setReplyMarkup(markup);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void handleSetUserLevel(long chatId, UserProfile profile, String data, int messageId) {
        WordLevel level;
        if (data.equals("set_level_beginner")) {
            level = WordLevel.BEGINNER;
        } else if (data.equals("set_level_intermediate")) {
            level = WordLevel.INTERMEDIATE;
        } else {
            level = WordLevel.ADVANCED;
        }

        profile.setSelectedLevel(level);
        profile.setCurrentWordInDay(0);
        userRepository.save(profile);

        TargetLanguage lang = (profile.getTargetLanguage() != null) ? profile.getTargetLanguage() : TargetLanguage.ENGLISH;
        String langFlag = lang.getFlag();

        String successText = "🎉 <b>Ajoyib! Tilingiz va Darajangiz belgilandi:</b> " + lang.getDisplayName() + " (" + level.getDisplayName() + ")\n\n" +
                "Endi har kuni sizga aynan <b>" + langFlag + " " + level.getDisplayName() + "</b> darajasidagi 20 ta yangi mnemonik so'z, audio talaffuzlar va mustahkamlovchi mashqlar beriladi.\n\n" +
                "👇 O'rganishni boshlash uchun pastdagi menyudan kerakli bo'limni tanlang:";

        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(successText);
        message.setParseMode("HTML");
        message.setReplyMarkup(createMainMenuReplyKeyboard());

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void sendWelcomeMessage(long chatId, UserProfile profile) {
        String name = profile.getFirstName() != null ? profile.getFirstName() : "Do'stim";
        int streak = profile.getCurrentStreak();
        String levelName = profile.getSelectedLevel() != null ? profile.getSelectedLevel().getDisplayName() : "Tanlanmagan";
        TargetLanguage lang = (profile.getTargetLanguage() != null) ? profile.getTargetLanguage() : TargetLanguage.ENGLISH;
        String langFlag = lang.getFlag();
        String botTitle = (lang == TargetLanguage.RUSSIAN) ? "Rus Tili Mnemonika Boti" : "Ingliz Tili Mnemonika Boti";

        String welcomeText = "👋 <b>Assalomu alaykum, " + escapeHtml(name) + "!</b>\n\n" +
                "🧠 <b>" + langFlag + " " + botTitle + "ga xush kelibsiz!</b>\n\n" +
                "✨ <b>YANGILANISHLAR VA IMKONIYATLAR:</b>\n" +
                "• 🌐 <b>Ikki Til Tizimi:</b> 🇬🇧 Ingliz va 🇷🇺 Rus tillarini o'rganish imkoniyati!\n" +
                "• 🔊 <b>Haqiqiy Audio Talaffuz:</b> Har bir so'z va jonli dialogni sof diktor ovozida eshitish.\n" +
                "• 🗣️ <b>Jonli Dialoglar:</b> Real hayotiy suhbatlar va replika audiolari.\n" +
                "• 🧠 <b>4-Bosqichli Kreativ Metodika:</b> Fonetik ilmoq, kinematik tasavvur, kontekst va xotirani faollashtirish.\n\n" +
                "━━━━━━━━━━━━━━━━━━━━━\n" +
                "🌐 <b>Faol til:</b> " + lang.getDisplayName() + "\n" +
                "🎯 <b>Tanlangan daraja:</b> " + levelName + "\n" +
                "🔥 <b>Joriy Streak:</b> " + streak + " kun ketma-ket\n" +
                "📚 <b>Bugungi Dars:</b> " + profile.getCurrentDayIndex() + "-kunlik to'plam (20 ta so'z)\n" +
                "━━━━━━━━━━━━━━━━━━━━━\n\n" +
                "👇 O'rganishni boshlash uchun quyidagi menyudan tanlang:";

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

    private void sendWebAppInfoMessage(long chatId, UserProfile profile) {
        String text = "📱 <b>Mnemonic Learning System — Zamonaviy Next.js Web Ilova</b>\n\n" +
                "✨ <b>Asosiy afzalliklari:</b>\n" +
                "• 🎨 <b>Animatsion menyu va ultra-zamonaviy Dark UI:</b> Suzuvchi pastki dok, silliq o'tishlar va zamonaviy dizayn.\n" +
                "• ⚡ <b>Internet past bo'lsa ham 0ms tezlik:</b> Brauzerning ichki kesh va Web Speech API orqali 2G yoki internetsiz holatda ham bir zumda ishlaydi!\n" +
                "• 🌐 <b>Ingliz 🇬🇧 va Rus 🇷🇺 tillari:</b> Bitta bosishda til va darajani o'zgartirish.\n" +
                "• 🗣️ <b>Jonli dialoglar va replika audio ijrosi:</b> Hayotiy suhbatlar (qahvaxona, IT-startap, ilmiy simpozium).\n" +
                "• ⚡ <b>Speed Quiz:</b> 15 soniyalik dinamik taymer, ovoz effektlari va natijalar!\n\n" +
                "🌐 <b>Brauzerda ochish manzili:</b>\n" +
                "👉 <code>http://localhost:3000</code>\n\n" +
                "<i>(Telegram Mini App sifatida ulash uchun domenni @BotFather orqali Menu Button ga biriktirish kifoya)</i>";

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> r1 = new ArrayList<>();
        InlineKeyboardButton webBtn = new InlineKeyboardButton("🚀 Ilovani Brauzerda Ochish (localhost:3000)");
        webBtn.setUrl("http://localhost:3000");
        r1.add(webBtn);
        rows.add(r1);
        markup.setKeyboard(rows);

        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);
        message.setParseMode("HTML");
        message.setReplyMarkup(markup);

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
        Word currentWord = todayWords.get(Math.max(0, Math.min(currentWordIdx, totalWords - 1)));

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        if (currentWordIdx < totalWords - 1) {
            // Asosiy tugma: Audio eshitish va avtomatik pastdan keyingi so'zga o'tish
            List<InlineKeyboardButton> audioNextRow = new ArrayList<>();
            InlineKeyboardButton audioNextBtn = new InlineKeyboardButton("🔊 Tinglash & Keyingi so'z (" + (currentWordIdx + 2) + "/" + totalWords + ") ▶️");
            audioNextBtn.setCallbackData("lesson_audio_next_" + currentWordIdx);
            audioNextRow.add(audioNextBtn);
            rows.add(audioNextRow);
        } else {
            // 20-so'zda bo'lsa: Audio eshitish va darsni tugatib mashqlarga o'tish
            List<InlineKeyboardButton> audioFinishRow = new ArrayList<>();
            InlineKeyboardButton audioFinishBtn = new InlineKeyboardButton("🔊 Tinglash & Mashqlarga o'tish (5 ta test) 🏁");
            audioFinishBtn.setCallbackData("lesson_audio_finish_" + currentWordIdx);
            audioFinishRow.add(audioFinishBtn);
            rows.add(audioFinishRow);
        }

        // Navigatsiya tugmalari
        List<InlineKeyboardButton> navRow = new ArrayList<>();
        if (currentWordIdx > 0) {
            InlineKeyboardButton prevBtn = new InlineKeyboardButton("◀️ Oldingi");
            prevBtn.setCallbackData("lesson_prev_" + (currentWordIdx - 1));
            navRow.add(prevBtn);
        }

        InlineKeyboardButton countBtn = new InlineKeyboardButton("📌 " + (currentWordIdx + 1) + "/" + totalWords);
        countBtn.setCallbackData("lesson_audio_current_" + currentWordIdx);
        navRow.add(countBtn);

        if (currentWordIdx < totalWords - 1) {
            InlineKeyboardButton nextBtn = new InlineKeyboardButton("Keyingi ▶️");
            nextBtn.setCallbackData("lesson_next_" + (currentWordIdx + 1));
            navRow.add(nextBtn);
        }
        rows.add(navRow);

        // Yakunlash va mashq tugmasi
        List<InlineKeyboardButton> actionRow = new ArrayList<>();
        InlineKeyboardButton finishBtn = new InlineKeyboardButton("✅ Darsni tugatish & Mashqlar");
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
        sb.append("💡 <i>Endi so'zlarni hayotiy suhbatda ko'rish uchun Dialog o'qing yoki mashqlarni bajaring!</i>");

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        List<InlineKeyboardButton> row0 = new ArrayList<>();
        InlineKeyboardButton dialogBtn = new InlineKeyboardButton("🗣️ 20 ta so'zdan tuzilgan Dialog & Audio");
        dialogBtn.setCallbackData("dialog_show");
        row0.add(dialogBtn);
        rows.add(row0);

        List<InlineKeyboardButton> row1 = new ArrayList<>();
        InlineKeyboardButton exBtn = new InlineKeyboardButton("📝 Mashqlarni boshlash (5 ta test)");
        exBtn.setCallbackData("exercise_start");
        row1.add(exBtn);
        rows.add(row1);
        markup.setKeyboard(rows);

        if (messageId > 0) {
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

    // =========================================================================
    // 📝 KUNLIK MASHQLAR (EXERCISES) VA XATO BO'LGANDA TUSHUNTIRISH
    // =========================================================================
    private void startDailyExercises(long chatId, UserProfile profile, boolean isEdit, int messageId) {
        List<Exercise> exercises = exerciseService.generateDailyExerciseSet(profile);
        ExerciseSession session = new ExerciseSession(exercises);
        activeExerciseSessions.put(chatId, session);

        sendCurrentExerciseQuestion(chatId, profile, isEdit, messageId);
    }

    private void sendCurrentExerciseQuestion(long chatId, UserProfile profile, boolean isEdit, int messageId) {
        ExerciseSession session = activeExerciseSessions.get(chatId);
        if (session == null) {
            sendMessage(chatId, "Mashqlar topilmadi. '📝 Kunlik Mashqlar' tugmasi orqali yangi mashq boshlang.");
            return;
        }

        if (session.isCompleted()) {
            StreakService.StreakResult streakRes = exerciseService.completeExerciseSession(profile, session.getCorrectCount());
            activeExerciseSessions.remove(chatId);

            int total = session.getTotalQuestions();
            int correct = session.getCorrectCount();
            int percent = (int) Math.round(((double) correct / total) * 100);

            StringBuilder resSb = new StringBuilder();
            resSb.append("🏆 <b>MASHQLAR MUVAFFAQIYATLI YAKUNLANDI!</b> 🎓\n\n");
            resSb.append("📊 <b>Natijangiz:</b> ").append(correct).append(" / ").append(total).append(" ta to'g'ri (").append(percent).append("%)\n");
            resSb.append("🔥 <b>Streak:</b> ").append(streakRes.getCurrentStreak()).append(" kun ketma-ket!\n");
            resSb.append("⭐️ <b>Jami yechilgan mashqlar:</b> ").append(profile.getTotalExercisesCompleted()).append(" ta\n\n");

            if (percent == 100) {
                resSb.append("🌟 <i>A'lo darajada! Siz bugungi barcha mnemonik so'zlarni 100% o'zlashtirdingiz!</i>");
            } else if (percent >= 60) {
                resSb.append("👍 <i>Yaxshi natija! Xato qilgan so'zlaringizni dars bo'limida yana bir bor ko'rib chiqing.</i>");
            } else {
                resSb.append("💪 <i>Harakatingiz tahsinga loyiq! '📅 Kunlik 20 ta so'z' bo'limi orqali qayta takrorlang.</i>");
            }

            InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
            List<List<InlineKeyboardButton>> rows = new ArrayList<>();

            List<InlineKeyboardButton> row0 = new ArrayList<>();
            InlineKeyboardButton dialogBtn = new InlineKeyboardButton("🗣️ 20 ta so'zdan tuzilgan Dialog & Audio");
            dialogBtn.setCallbackData("dialog_show");
            row0.add(dialogBtn);
            rows.add(row0);

            List<InlineKeyboardButton> row1 = new ArrayList<>();
            InlineKeyboardButton retryBtn = new InlineKeyboardButton("🔄 Qayta mashq qilish");
            retryBtn.setCallbackData("exercise_start");
            row1.add(retryBtn);

            InlineKeyboardButton lessonBtn = new InlineKeyboardButton("📅 Bugungi 20 ta so'z");
            lessonBtn.setCallbackData("lesson_start");
            row1.add(lessonBtn);
            rows.add(row1);

            markup.setKeyboard(rows);

            SendMessage resMsg = new SendMessage();
            resMsg.setChatId(String.valueOf(chatId));
            resMsg.setText(resSb.toString());
            resMsg.setParseMode("HTML");
            resMsg.setReplyMarkup(markup);

            try {
                execute(resMsg);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
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

        // Yangi xabar sifatida chiqarish (har doim pastda ko'rinishi uchun!)
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
            sb.append("🎉 <b>Ajoyib! To'g'ri javob!</b> ✅\n\n");
            sb.append("💡 <b>Eslab qolish uchun mnemonika:</b>\n");
            sb.append(currentEx.getRelatedWord().toFormattedCard());
        } else {
            // Xato javob berilganda to'liq tushuntirib berish
            String userChoice = currentEx.getOptions().get(selectedIdx);
            String correctChoice = currentEx.getOptions().get(currentEx.getCorrectOptionIndex());

            sb.append("❌ <b>Noto'g'ri javob!</b>\n\n");
            sb.append("Siz tanladingiz: <s>").append(escapeHtml(userChoice)).append("</s>\n");
            sb.append("✅ <b>To'g'ri javob:</b> <b>").append(escapeHtml(correctChoice)).append("</b>\n\n");
            sb.append("🧠 <b>NEGA BUNDAY? (Mnemonik tushuntirish):</b>\n");
            sb.append(currentEx.getRelatedWord().toFormattedCard());
        }

        session.nextQuestion();

        String wordStr = currentEx.getRelatedWord().getEnglishWord().toLowerCase();
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        if (!session.isCompleted()) {
            List<InlineKeyboardButton> row1 = new ArrayList<>();
            InlineKeyboardButton audioNextBtn = new InlineKeyboardButton("🔊 Tinglash & Keyingi mashq (" + (session.getCurrentIndex() + 1) + "/" + session.getTotalQuestions() + ") ➡️");
            audioNextBtn.setCallbackData("exercise_audio_next_" + wordStr);
            row1.add(audioNextBtn);
            rows.add(row1);

            List<InlineKeyboardButton> row2 = new ArrayList<>();
            InlineKeyboardButton nextBtn = new InlineKeyboardButton("➡️ Keyingi mashq (Audiosiz)");
            nextBtn.setCallbackData("exercise_next_question");
            row2.add(nextBtn);
            rows.add(row2);
        } else {
            List<InlineKeyboardButton> row1 = new ArrayList<>();
            InlineKeyboardButton audioFinishBtn = new InlineKeyboardButton("🔊 Tinglash & Natijani ko'rish 🏁");
            audioFinishBtn.setCallbackData("exercise_audio_next_" + wordStr);
            row1.add(audioFinishBtn);
            rows.add(row1);

            List<InlineKeyboardButton> row2 = new ArrayList<>();
            InlineKeyboardButton finishBtn = new InlineKeyboardButton("🏁 Natijani ko'rish (Audiosiz)");
            finishBtn.setCallbackData("exercise_next_question");
            row2.add(finishBtn);
            rows.add(row2);
        }
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
        sb.append("💡 <i>Bot har kuni belgilangan vaqtda sizga har xil kreativ mavzudagi bildirishnomalar, Kun So'zi audio talaffuzi va Streak saqlash eslatmalarini yuboradi.</i>\n\n");
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
    // 🎲 TASODIFIY SO'Z & QIDIRUV & AUDIO
    // =========================================================================
    private void sendRandomWord(long chatId, UserProfile profile) {
        TargetLanguage lang = (profile.getTargetLanguage() != null) ? profile.getTargetLanguage() : TargetLanguage.ENGLISH;
        Optional<Word> wordOpt = (profile.getSelectedLevel() != null)
                ? wordRepository.getRandomWordByLevel(profile.getSelectedLevel(), lang)
                : wordRepository.getRandomWord(lang);

        if (wordOpt.isEmpty()) {
            wordOpt = wordRepository.getRandomWord(lang);
        }

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

        List<InlineKeyboardButton> audioRow = new ArrayList<>();
        InlineKeyboardButton audioBtn = new InlineKeyboardButton("🔊 O'qilishini eshitish (Audio)");
        audioBtn.setCallbackData("audio_word_" + word.getEnglishWord().toLowerCase());
        audioRow.add(audioBtn);
        rows.add(audioRow);

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
        TargetLanguage lang = (profile.getTargetLanguage() != null) ? profile.getTargetLanguage() : TargetLanguage.ENGLISH;
        Optional<Word> wordOpt = wordRepository.findByKeyword(query, lang);
        if (wordOpt.isEmpty()) {
            wordOpt = wordRepository.findByKeyword(query);
        }

        if (wordOpt.isPresent()) {
            Word word = wordOpt.get();
            SendMessage message = new SendMessage();
            message.setChatId(String.valueOf(chatId));
            message.setText("🔍 <b>Topilgan so'z:</b>\n\n" + word.toFormattedCard());
            message.setParseMode("HTML");

            InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
            List<List<InlineKeyboardButton>> rows = new ArrayList<>();

            List<InlineKeyboardButton> audioRow = new ArrayList<>();
            InlineKeyboardButton audioBtn = new InlineKeyboardButton("🔊 O'qilishini eshitish (Audio)");
            audioBtn.setCallbackData("audio_word_" + word.getEnglishWord().toLowerCase());
            audioRow.add(audioBtn);
            rows.add(audioRow);

            List<InlineKeyboardButton> row1 = new ArrayList<>();
            InlineKeyboardButton randBtn = new InlineKeyboardButton("🎲 Boshqa so'z");
            randBtn.setCallbackData("random_word");
            row1.add(randBtn);
            rows.add(row1);

            markup.setKeyboard(rows);
            message.setReplyMarkup(markup);

            try {
                execute(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        } else {
            String langName = (lang == TargetLanguage.RUSSIAN) ? "ruscha" : "inglizcha";
            String notFoundText = "😔 Kechirasiz, '<b>" + escapeHtml(query) + "</b>' so'zi bo'yicha mnemonika topilmadi.\n\n" +
                    "💡 <i>Tavsiya:</i> Qidirish uchun " + langName + " so'z yoki menyudagi tugmalardan foydalaning.";
            sendMessage(chatId, notFoundText);
        }
    }

    private void sendLevelsMenu(long chatId, UserProfile profile) {
        String currentLevelStr = profile.getSelectedLevel() != null ? profile.getSelectedLevel().getDisplayName() : "Tanlanmagan";
        TargetLanguage lang = (profile.getTargetLanguage() != null) ? profile.getTargetLanguage() : TargetLanguage.ENGLISH;

        String text = "📚 <b>DARAXALAR BO'LIMI (" + lang.getDisplayName() + "):</b>\n\n" +
                "📌 <b>Hozirgi darajangiz:</b> " + currentLevelStr + "\n\n" +
                "Istalgan darajadagi so'zlarni ko'rish uchun quyidagi tugmalardan birini tanlang:";

        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);
        message.setParseMode("HTML");

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        List<InlineKeyboardButton> row1 = new ArrayList<>();
        InlineKeyboardButton b1 = new InlineKeyboardButton("🟢 Boshlang'ich (A1-A2)");
        b1.setCallbackData("explore_level_beginner");
        row1.add(b1);

        List<InlineKeyboardButton> row2 = new ArrayList<>();
        InlineKeyboardButton b2 = new InlineKeyboardButton("🟡 O'rta (B1-B2)");
        b2.setCallbackData("explore_level_intermediate");
        row2.add(b2);

        List<InlineKeyboardButton> row3 = new ArrayList<>();
        InlineKeyboardButton b3 = new InlineKeyboardButton("🔴 Yuqori (C1-C2)");
        b3.setCallbackData("explore_level_advanced");
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

    private void handleExploreLevel(long chatId, UserProfile profile, String data) {
        WordLevel level;
        if (data.equals("explore_level_beginner")) level = WordLevel.BEGINNER;
        else if (data.equals("explore_level_intermediate")) level = WordLevel.INTERMEDIATE;
        else level = WordLevel.ADVANCED;

        TargetLanguage lang = (profile.getTargetLanguage() != null) ? profile.getTargetLanguage() : TargetLanguage.ENGLISH;
        Optional<Word> wordOpt = wordRepository.getRandomWordByLevel(level, lang);
        if (wordOpt.isPresent()) {
            Word word = wordOpt.get();

            SendMessage message = new SendMessage();
            message.setChatId(String.valueOf(chatId));
            message.setText(word.toFormattedCard());
            message.setParseMode("HTML");

            InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
            List<List<InlineKeyboardButton>> rows = new ArrayList<>();

            List<InlineKeyboardButton> audioRow = new ArrayList<>();
            InlineKeyboardButton audioBtn = new InlineKeyboardButton("🔊 O'qilishini eshitish (Audio)");
            audioBtn.setCallbackData("audio_word_" + word.getEnglishWord().toLowerCase());
            audioRow.add(audioBtn);
            rows.add(audioRow);

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

    // =========================================================================
    // 🔊 AUDIO TALAFFUZNI YUBORISH METODLARI
    // =========================================================================
    public void sendWordAudio(long chatId, Word word) {
        SendVoice voice = audioService.createVoiceMessage(chatId, word);
        if (voice != null) {
            try {
                execute(voice);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        } else {
            sendMessage(chatId, "⚠️ Audio talaffuzni yuklashda vaqtinchalik muammo yuz berdi.");
        }
    }

    public void sendWordAudio(long chatId, String wordText) {
        UserProfile profile = userRepository.get(chatId).orElse(null);
        TargetLanguage lang = (profile != null && profile.getTargetLanguage() != null) ? profile.getTargetLanguage() : TargetLanguage.ENGLISH;

        Optional<Word> wordOpt = wordRepository.findByKeyword(wordText, lang);
        if (wordOpt.isEmpty()) {
            wordOpt = wordRepository.findByKeyword(wordText);
        }

        if (wordOpt.isPresent()) {
            sendWordAudio(chatId, wordOpt.get());
        } else {
            String langCode = (lang == TargetLanguage.RUSSIAN) ? "ru" : "en";
            SendVoice voice = audioService.createVoiceMessage(chatId, wordText, "🔊 <b>Talaffuzi:</b> <code>" + wordText + "</code>", langCode);
            if (voice != null) {
                try {
                    execute(voice);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            } else {
                sendMessage(chatId, "⚠️ Audio talaffuzni yuklab bo'lmadi.");
            }
        }
    }

    private void sendNewQuiz(long chatId) {
        UserProfile profile = userRepository.get(chatId).orElse(null);
        TargetLanguage lang = (profile != null && profile.getTargetLanguage() != null) ? profile.getTargetLanguage() : TargetLanguage.ENGLISH;

        Optional<QuizQuestion> questionOpt = quizService.generateQuestion(lang);
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
            response.append("💡 <b>Eslab qolish uchun mnemonika:</b>\n");
            response.append(question.getRelatedWord().toFormattedCard());
        } else {
            String userChoice = question.getOptions().get(selectedIndex);
            String correctChoice = question.getOptions().get(question.getCorrectOptionIndex());

            response.append("❌ <b>Noto'g'ri javob!</b>\n\n");
            response.append("Siz tanladingiz: <s>").append(escapeHtml(userChoice)).append("</s>\n");
            response.append("✅ <b>To'g'ri javob:</b> <b>").append(escapeHtml(correctChoice)).append("</b>\n\n");
            response.append("🧠 <b>NEGA BUNDAY? (Mnemonik tushuntirish):</b>\n");
            response.append(question.getRelatedWord().toFormattedCard());
        }

        EditMessageText editMessage = new EditMessageText();
        editMessage.setChatId(String.valueOf(chatId));
        editMessage.setMessageId(messageId);
        editMessage.setText(response.toString());
        editMessage.setParseMode("HTML");

        String quizWordStr = question.getRelatedWord().getEnglishWord().toLowerCase();
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        List<InlineKeyboardButton> row1 = new ArrayList<>();
        InlineKeyboardButton audioNextQuiz = new InlineKeyboardButton("🔊 Tinglash & Keyingi savol ➡️");
        audioNextQuiz.setCallbackData("quiz_audio_next_" + quizWordStr);
        row1.add(audioNextQuiz);
        rows.add(row1);

        List<InlineKeyboardButton> row2 = new ArrayList<>();
        InlineKeyboardButton nextQuiz = new InlineKeyboardButton("➡️ Keyingi savol (Audiosiz)");
        nextQuiz.setCallbackData("quiz_next");
        row2.add(nextQuiz);
        rows.add(row2);

        markup.setKeyboard(rows);
        editMessage.setReplyMarkup(markup);

        try {
            execute(editMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

        currentQuizzes.remove(chatId);
    }

    // =========================================================================
    // 🗣️ KUNLIK DIALOG & SPEAKING PRACTICE (AUDIO BILAN)
    // =========================================================================
    private void sendDailyDialogue(long chatId, UserProfile profile) {
        com.mnemonic.model.DailyDialogue dialogue = dialogueService.getDialogueForProfile(profile);
        if (dialogue == null) {
            sendMessage(chatId, "Hozircha dialog mavjud emas.");
            return;
        }

        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(dialogue.toFormattedCard());
        message.setParseMode("HTML");

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        List<InlineKeyboardButton> row1 = new ArrayList<>();
        InlineKeyboardButton audioBtn = new InlineKeyboardButton("🎧 Butun dialogni tinglash (Audio)");
        audioBtn.setCallbackData("dialog_audio_" + dialogue.getId());
        row1.add(audioBtn);
        rows.add(row1);

        List<InlineKeyboardButton> row2 = new ArrayList<>();
        InlineKeyboardButton exBtn = new InlineKeyboardButton("📝 Mashqlarni yechish");
        exBtn.setCallbackData("exercise_start");
        row2.add(exBtn);

        InlineKeyboardButton lessonBtn = new InlineKeyboardButton("📅 20 ta so'z");
        lessonBtn.setCallbackData("lesson_start");
        row2.add(lessonBtn);
        rows.add(row2);

        markup.setKeyboard(rows);
        message.setReplyMarkup(markup);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void sendDialogueAudio(long chatId, String dialogId, UserProfile profile) {
        com.mnemonic.model.DailyDialogue dialogue = dialogueService.getDialogueById(dialogId);
        if (dialogue == null) {
            dialogue = dialogueService.getDialogueForProfile(profile);
        }
        if (dialogue == null) {
            sendMessage(chatId, "Dialog audio topilmadi.");
            return;
        }

        SendVoice voice = audioService.createDialogueVoiceMessage(chatId, dialogue);
        if (voice != null) {
            try {
                execute(voice);

                // Audio ostidan darhol mashqlar va boshqa harakatlar tugmasini chiqarish
                SendMessage nextMsg = new SendMessage();
                nextMsg.setChatId(String.valueOf(chatId));
                nextMsg.setText("👏 <b>Dialogni muvaffaqiyatli tingladingiz!</b> 🎧\n\n" +
                                "💡 <i>Tavsiya: Dialogdagi jumlalarni ovoz chiqarib 1-2 marta o'zingiz ham qaytarib ko'ring.</i>\n\n" +
                                "👇 Endi ushbu so'zlarni mustahkamlash uchun 5 ta test mashqini yeching:");
                nextMsg.setParseMode("HTML");

                InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
                List<List<InlineKeyboardButton>> rows = new ArrayList<>();

                List<InlineKeyboardButton> row1 = new ArrayList<>();
                InlineKeyboardButton exBtn = new InlineKeyboardButton("📝 5 ta Mashqni boshlash");
                exBtn.setCallbackData("exercise_start");
                row1.add(exBtn);
                rows.add(row1);

                List<InlineKeyboardButton> row2 = new ArrayList<>();
                InlineKeyboardButton lessonBtn = new InlineKeyboardButton("📅 Bugungi 20 ta so'z");
                lessonBtn.setCallbackData("lesson_start");
                row2.add(lessonBtn);
                rows.add(row2);

                markup.setKeyboard(rows);
                nextMsg.setReplyMarkup(markup);

                execute(nextMsg);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        } else {
            sendMessage(chatId, "⚠️ Dialog audiosini yuklashda vaqtinchalik muammo yuz berdi.");
        }
    }

    private void sendMnemonicGuide(long chatId) {
        String guide = "🧠 <b>Mnemonika Nima va U Qanday Ishlaydi?</b>\n\n" +
                "Mnemonika — inson miyasining assotsiativ xotirasidan foydalanib, yangi ma'lumotlarni oson va uzoq muddatga eslab qolish san'atidir.\n\n" +
                "🔑 <b>4 ta Oltin Qoida:</b>\n" +
                "1. <b>Fonetik o'xshashlik (Ilmoq):</b> Inglizcha so'z talaffuziga o'xshash o'zbekcha tanish so'z tanlanadi.\n" +
                "2. <b>Kinematik jonli obraz:</b> Miya zerikarli faktlarni emas, kulgili, bo'rttirilgan va harakatli tasvirlarni yaxshi eslab qoladi.\n" +
                "3. <b>Bog'lovchi hikoya:</b> Yangi so'zning asl ma'nosi bilan topilgan obraz bir-biriga mantiqiy bog'lanadi.\n" +
                "4. <b>Ovozli takrorlash (Audio):</b> So'zning to'g'ri talaffuzini eshitib, uni ovoz chiqarib 3 marta takrorlash xotirani mustahkamlaydi.\n\n" +
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
        row2.add(new KeyboardButton("🗣️ Kunlik Dialog"));
        row2.add(new KeyboardButton("🔥 Streak & Natijalarim"));

        KeyboardRow row3 = new KeyboardRow();
        row3.add(new KeyboardButton("🌐 Tilni o'zgartirish"));
        row3.add(new KeyboardButton("🎯 Darajani o'zgartirish"));

        KeyboardRow row4 = new KeyboardRow();
        row4.add(new KeyboardButton("🎲 Tasodifiy so'z"));
        row4.add(new KeyboardButton("📚 Darajalar"));

        KeyboardRow row5 = new KeyboardRow();
        row5.add(new KeyboardButton("🎮 Tezkor Test"));
        row5.add(new KeyboardButton("🔍 Qidiruv"));

        KeyboardRow row6 = new KeyboardRow();
        row6.add(new KeyboardButton("⏰ Eslatma sozlamalari"));
        row6.add(new KeyboardButton("💡 Mnemonika nima?"));

        KeyboardRow row7 = new KeyboardRow();
        row7.add(new KeyboardButton("📱 Web Ilova (Next.js Mini App)"));

        keyboard.add(row1);
        keyboard.add(row2);
        keyboard.add(row3);
        keyboard.add(row4);
        keyboard.add(row5);
        keyboard.add(row6);
        keyboard.add(row7);

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
