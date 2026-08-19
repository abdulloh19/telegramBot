package com.mnemonic.bot;

import com.mnemonic.model.QuizQuestion;
import com.mnemonic.model.Word;
import com.mnemonic.model.WordLevel;
import com.mnemonic.repository.WordRepository;
import com.mnemonic.service.QuizService;
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
    private final QuizService quizService;

    // Chat ID -> Foydalanuvchi hozirgi viktorina savoli
    private final Map<Long, QuizQuestion> currentQuizzes = new ConcurrentHashMap<>();

    // Foydalanuvchi statistikasi (Chat ID -> [ko'rilgan so'zlar, to'g'ri testlar, jami testlar])
    private final Map<Long, UserStats> userStatsMap = new ConcurrentHashMap<>();

    public EnglishMnemonicBot(String botUsername, String botToken) {
        this.botUsername = botUsername;
        this.botToken = botToken;
        this.wordRepository = new WordRepository();
        this.quizService = new QuizService(wordRepository);
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

        // Foydalanuvchini statistikada qayd etish
        userStatsMap.putIfAbsent(chatId, new UserStats());

        switch (text) {
            case "/start":
            case "/menu":
            case "🏠 Asosiy Menyu":
                sendWelcomeMessage(chatId, firstName);
                break;

            case "🎲 Tasodifiy so'z":
                sendRandomWord(chatId);
                break;

            case "📚 Darajalar":
                sendLevelsMenu(chatId);
                break;

            case "🎮 Viktorina (Quiz)":
                sendNewQuiz(chatId);
                break;

            case "💡 Mnemonika nima?":
                sendMnemonicGuide(chatId);
                break;

            case "📊 Statistikam":
                sendUserStats(chatId, firstName);
                break;

            case "🔍 Qidiruv":
                sendMessage(chatId, "🔍 <b>So'z qidirish:</b>\n\nIstalgan inglizcha yoki o'zbekcha so'zni yozib yuboring (Masalan: <code>abandon</code>, <code>frugal</code>, <code>intiluvchan</code>). Bot uning mnemonikasini topib beradi.");
                break;

            default:
                // Matn orqali so'z qidirish
                handleWordSearch(chatId, text);
                break;
        }
    }

    private void handleCallbackQuery(Update update) {
        String data = update.getCallbackQuery().getData();
        long chatId = update.getCallbackQuery().getMessage().getChatId();
        int messageId = update.getCallbackQuery().getMessage().getMessageId();
        String callbackId = update.getCallbackQuery().getId();

        // Tugma bosilganligi haqida bildirishnoma berish
        answerCallback(callbackId);

        if (data.equals("random_word")) {
            sendRandomWord(chatId);
        } else if (data.equals("quiz_next")) {
            sendNewQuiz(chatId);
        } else if (data.startsWith("level_")) {
            handleLevelSelection(chatId, data);
        } else if (data.startsWith("quiz_opt_")) {
            int selectedIndex = Integer.parseInt(data.replace("quiz_opt_", ""));
            handleQuizAnswer(chatId, messageId, selectedIndex);
        }
    }

    private void sendWelcomeMessage(long chatId, String firstName) {
        String welcomeText = "👋 <b>Assalomu alaykum, " + escapeHtml(firstName) + "!</b>\n\n" +
                "🧠 <b>Ingliz Tili Mnemonika Botiga</b> xush kelibsiz!\n\n" +
                "Bu bot orqali siz ingliz tili so'zlarini quruq yodlamasdan, **jonli assotsiatsiyalar, kulgili va esda qolarli obrazlar** yordamida bir umrga eslab qolasiz.\n\n" +
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

    private void sendRandomWord(long chatId) {
        Optional<Word> wordOpt = wordRepository.getRandomWord();
        if (wordOpt.isEmpty()) {
            sendMessage(chatId, "Hozircha so'zlar mavjud emas.");
            return;
        }

        Word word = wordOpt.get();
        getUserStats(chatId).incrementWordsViewed();

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

        InlineKeyboardButton quizBtn = new InlineKeyboardButton("🎮 Viktorinada sinash");
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

    private void handleLevelSelection(long chatId, String data) {
        WordLevel level;
        if (data.equals("level_beginner")) {
            level = WordLevel.BEGINNER;
        } else if (data.equals("level_intermediate")) {
            level = WordLevel.INTERMEDIATE;
        } else {
            level = WordLevel.ADVANCED;
        }

        Optional<Word> wordOpt = wordRepository.getRandomWordByLevel(level);
        if (wordOpt.isPresent()) {
            Word word = wordOpt.get();
            getUserStats(chatId).incrementWordsViewed();

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
            sendMessage(chatId, "Viktorina uchun so'zlar yetarli emas.");
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
            sendMessage(chatId, "Savol muddati tugagan. Yangi savol boshlash uchun '🎮 Viktorina' tugmasini bosing.");
            return;
        }

        UserStats stats = getUserStats(chatId);
        stats.incrementQuizTotal();

        boolean isCorrect = (selectedIndex == question.getCorrectOptionIndex());
        if (isCorrect) {
            stats.incrementQuizCorrect();
        }

        StringBuilder response = new StringBuilder();
        if (isCorrect) {
            response.append("🎉 <b>Ajoyib! To'g'ri javob!</b> ✅\n\n");
        } else {
            response.append("❌ <b>Afsus, noto'g'ri!</b>\n");
            response.append("To'g'ri javob: <b>").append(escapeHtml(question.getOptions().get(question.getCorrectOptionIndex()))).append("</b>\n\n");
        }

        // Mnemonikasini eslatish
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

        InlineKeyboardButton randomWordBtn = new InlineKeyboardButton("🎲 Tasodifiy so'z");
        randomWordBtn.setCallbackData("random_word");
        row1.add(randomWordBtn);

        rows.add(row1);
        markup.setKeyboard(rows);
        editMessage.setReplyMarkup(markup);

        try {
            execute(editMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

        // Savol javoblangach xotiradan tozalaymiz
        currentQuizzes.remove(chatId);
    }

    private void handleWordSearch(long chatId, String query) {
        Optional<Word> wordOpt = wordRepository.findByKeyword(query);
        if (wordOpt.isPresent()) {
            Word word = wordOpt.get();
            getUserStats(chatId).incrementWordsViewed();
            sendMessage(chatId, "🔍 <b>Topilgan so'z:</b>\n\n" + word.toFormattedCard());
        } else {
            String notFoundText = "😔 Kechirasiz, '<b>" + escapeHtml(query) + "</b>' so'zi bo'yicha mnemonika topilmadi.\n\n" +
                    "💡 <i>Tavsiya:</i> Qidirish uchun inglizcha so'z (masalan: <code>lucid</code>, <code>frugal</code>) yoki menyudagi tugmalardan foydalaning.";
            sendMessage(chatId, notFoundText);
        }
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

    private void sendUserStats(long chatId, String firstName) {
        UserStats stats = getUserStats(chatId);
        int accuracy = stats.getQuizTotal() > 0 ? (stats.getQuizCorrect() * 100 / stats.getQuizTotal()) : 0;

        String statsText = "📊 <b>" + escapeHtml(firstName) + " ning O'rganish Statistikasi:</b>\n\n" +
                "👁 <b>Ko'rilgan mnemonik so'zlar:</b> " + stats.getWordsViewed() + " ta\n" +
                "🎯 <b>Viktorinada to'g'ri javoblar:</b> " + stats.getQuizCorrect() + " / " + stats.getQuizTotal() + "\n" +
                "📈 <b>Aniqlik darajasi:</b> " + accuracy + "%\n\n" +
                (stats.getWordsViewed() >= 10 ? "🔥 Juda zo'r natija! O'rganishda davom eting!" : "🚀 Har kuni yangi so'zlarni o'rganib boring!");

        sendMessage(chatId, statsText);
    }

    private ReplyKeyboardMarkup createMainMenuReplyKeyboard() {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow row1 = new KeyboardRow();
        row1.add(new KeyboardButton("🎲 Tasodifiy so'z"));
        row1.add(new KeyboardButton("📚 Darajalar"));

        KeyboardRow row2 = new KeyboardRow();
        row2.add(new KeyboardButton("🎮 Viktorina (Quiz)"));
        row2.add(new KeyboardButton("🔍 Qidiruv"));

        KeyboardRow row3 = new KeyboardRow();
        row3.add(new KeyboardButton("💡 Mnemonika nima?"));
        row3.add(new KeyboardButton("📊 Statistikam"));

        keyboard.add(row1);
        keyboard.add(row2);
        keyboard.add(row3);

        keyboardMarkup.setKeyboard(keyboard);
        return keyboardMarkup;
    }

    private void sendMessage(long chatId, String text) {
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

    private void answerCallback(String callbackId) {
        try {
            AnswerCallbackQuery answer = new AnswerCallbackQuery();
            answer.setCallbackQueryId(callbackId);
            execute(answer);
        } catch (TelegramApiException e) {
            // Callback javob berishda xatolik bo'lsa e'tiborsiz qoldirish mumkin
        }
    }

    private UserStats getUserStats(long chatId) {
        return userStatsMap.computeIfAbsent(chatId, k -> new UserStats());
    }

    private String escapeHtml(String text) {
        if (text == null) return "";
        return text.replace("&", "&amp;")
                   .replace("<", "&lt;")
                   .replace(">", "&gt;");
    }

    // Ichki statistika modeli
    public static class UserStats {
        private int wordsViewed = 0;
        private int quizCorrect = 0;
        private int quizTotal = 0;

        public synchronized void incrementWordsViewed() {
            wordsViewed++;
        }

        public synchronized void incrementQuizCorrect() {
            quizCorrect++;
        }

        public synchronized void incrementQuizTotal() {
            quizTotal++;
        }

        public synchronized int getWordsViewed() {
            return wordsViewed;
        }

        public synchronized int getQuizCorrect() {
            return quizCorrect;
        }

        public synchronized int getQuizTotal() {
            return quizTotal;
        }
    }
}
