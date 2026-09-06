package com.mnemonic.model;

public class Word {
    private final String englishWord;
    private final String pronunciation;
    private final String uzbekMeaning;
    private final String mnemonicHook;
    private final String mnemonicStory;
    private final String exampleEn;
    private final String exampleUz;
    private final WordLevel level;

    public Word(String englishWord, String pronunciation, String uzbekMeaning,
                String mnemonicHook, String mnemonicStory,
                String exampleEn, String exampleUz, WordLevel level) {
        this.englishWord = englishWord;
        this.pronunciation = pronunciation;
        this.uzbekMeaning = uzbekMeaning;
        this.mnemonicHook = mnemonicHook;
        this.mnemonicStory = mnemonicStory;
        this.exampleEn = exampleEn;
        this.exampleUz = exampleUz;
        this.level = level;
    }

    public String getEnglishWord() {
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

    /**
     * Telegramda 4-bosqichli kreativ mnemonika formatida kartochkani qaytaradi
     */
    public String toFormattedCard() {
        return "━━━━━━━━━━━━━━━━━━━━━\n" +
               "🔤 <b>SO'Z:</b> <code>" + englishWord.toUpperCase() + "</code> " + pronunciation + "\n" +
               "🇺🇿 <b>MA'NOSI:</b> <b>" + uzbekMeaning + "</b>\n" +
               "📊 <b>Daraja:</b> " + level.getDisplayName() + "\n" +
               "━━━━━━━━━━━━━━━━━━━━━\n\n" +
               "🧠 <b>KREATIV MNEMONIK METODIKA (4 Qadam):</b>\n\n" +
               "1️⃣ 🔗 <b>Fonetik Ilmoq (Eshitish):</b>\n" +
               "   👉 <i>«" + mnemonicHook + "»</i>\n\n" +
               "2️⃣ 🎬 <b>Kinematik Obraz (Tasavvur qiling):</b>\n" +
               "   👉 " + mnemonicStory + "\n\n" +
               "3️⃣ 📝 <b>Kontekst & Misol Gap:</b>\n" +
               "   🇬🇧 <i>\"" + exampleEn + "\"</i>\n" +
               "   🇺🇿 <i>\"" + exampleUz + "\"</i>\n\n" +
               "4️⃣ ⚡ <b>Xotirani Faollashtirish (3 soniya):</b>\n" +
               "   👉 <i>Ko'zingizni 3 soniya yuming, yuqoridagi voqeani tasavvur qiling va '🔊 Audio' tugmasi orqali talaffuzni qaytaring!</i>\n" +
               "━━━━━━━━━━━━━━━━━━━━━";
    }
}
