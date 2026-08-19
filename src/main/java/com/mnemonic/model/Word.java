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
     * Telegramda chiroyli HTML formatida kartochkani qaytaradi
     */
    public String toFormattedCard() {
        return "📖 <b>So'z:</b> <code>" + englishWord + "</code> " + pronunciation + "\n" +
               "🇺🇿 <b>Ma'nosi:</b> <b>" + uzbekMeaning + "</b>\n" +
               "📊 <b>Daraja:</b> " + level.getDisplayName() + "\n\n" +
               "🧠 <b>MNEMONIKA (Eslab qolish siri):</b>\n" +
               "🔗 <b>Kalit so'z:</b> <i>" + mnemonicHook + "</i>\n" +
               "🎬 <b>Obraz / Hikoya:</b>\n" + mnemonicStory + "\n\n" +
               "📝 <b>Misol:</b>\n" +
               "🇬🇧 <i>" + exampleEn + "</i>\n" +
               "🇺🇿 <i>" + exampleUz + "</i>";
    }
}
