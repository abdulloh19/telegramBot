package com.mnemonic.model;

public class Word {
    private final String englishWord; // Word text in target language (English or Russian)
    private final String pronunciation;
    private final String uzbekMeaning;
    private final String mnemonicHook;
    private final String mnemonicStory;
    private final String exampleEn; // Example in target language
    private final String exampleUz;
    private final WordLevel level;
    private final TargetLanguage language;

    public Word(String englishWord, String pronunciation, String uzbekMeaning,
                String mnemonicHook, String mnemonicStory,
                String exampleEn, String exampleUz, WordLevel level) {
        this(englishWord, pronunciation, uzbekMeaning, mnemonicHook, mnemonicStory, exampleEn, exampleUz, level, TargetLanguage.ENGLISH);
    }

    public Word(String englishWord, String pronunciation, String uzbekMeaning,
                String mnemonicHook, String mnemonicStory,
                String exampleEn, String exampleUz, WordLevel level, TargetLanguage language) {
        this.englishWord = englishWord;
        this.pronunciation = pronunciation;
        this.uzbekMeaning = uzbekMeaning;
        this.mnemonicHook = mnemonicHook;
        this.mnemonicStory = mnemonicStory;
        this.exampleEn = exampleEn;
        this.exampleUz = exampleUz;
        this.level = level;
        this.language = language != null ? language : TargetLanguage.ENGLISH;
    }

    public String getEnglishWord() {
        return englishWord;
    }

    public String getWord() {
        return englishWord;
    }

    public String getPronunciation() {
        return pronunciation;
    }

    public String getUzbekMeaning() {
        return uzbekMeaning;
    }

    public String getMnemonicHook() {
        return mnemonicHook;
    }

    public String getMnemonicStory() {
        return mnemonicStory;
    }

    public String getExampleEn() {
        return exampleEn;
    }

    public String getExampleUz() {
        return exampleUz;
    }

    public WordLevel getLevel() {
        return level;
    }

    public TargetLanguage getLanguage() {
        return language;
    }

    /**
     * Telegramda 4-bosqichli kreativ mnemonika formatida kartochkani qaytaradi
     */
    public String toFormattedCard() {
        String langFlag = (language == TargetLanguage.RUSSIAN) ? "🇷🇺" : "🇬🇧";
        String wordHeader = (language == TargetLanguage.RUSSIAN) ? "СЛОВО (SO'Z)" : "SO'Z";

        return "━━━━━━━━━━━━━━━━━━━━━\n" +
               "🔤 <b>" + wordHeader + ":</b> <code>" + englishWord.toUpperCase() + "</code> " + pronunciation + "\n" +
               "🇺🇿 <b>MA'NOSI:</b> <b>" + uzbekMeaning + "</b>\n" +
               "📊 <b>Daraja:</b> " + level.getDisplayName() + " (" + langFlag + ")\n" +
               "━━━━━━━━━━━━━━━━━━━━━\n\n" +
               "🧠 <b>KREATIV MNEMONIK METODIKA (4 Qadam):</b>\n\n" +
               "1️⃣ 🔗 <b>Fonetik Ilmoq (Eshitish):</b>\n" +
               "   👉 <i>«" + mnemonicHook + "»</i>\n\n" +
               "2️⃣ 🎬 <b>Kinematik Obraz (Tasavvur qiling):</b>\n" +
               "   👉 " + mnemonicStory + "\n\n" +
               "3️⃣ 📝 <b>Kontekst & Misol Gap:</b>\n" +
               "   " + langFlag + " <i>\"" + exampleEn + "\"</i>\n" +
               "   🇺🇿 <i>\"" + exampleUz + "\"</i>\n\n" +
               "4️⃣ ⚡ <b>Xotirani Faollashtirish (3 soniya):</b>\n" +
               "   👉 <i>Ko'zingizni 3 soniya yuming, yuqoridagi voqeani tasavvur qiling va '🔊 Audio' tugmasi orqali talaffuzni qaytaring!</i>\n" +
               "━━━━━━━━━━━━━━━━━━━━━";
    }
}
