package com.mnemonic.model;

import java.util.List;

/**
 * Kunlik 20 ta yangi mnemonik so'zlarni real hayotiy kontekstda
 * bog'lab beruvchi jonli dialog modeli (Ingliz va Rus tillari uchun).
 */
public class DailyDialogue {
    private final String id;
    private final String title;
    private final String situationEn;
    private final String situationUz;
    private final WordLevel level;
    private final int dayIndex;
    private final List<DialogueLine> lines;
    private final List<String> targetWords;
    private final TargetLanguage language;

    public DailyDialogue(String id, String title, String situationEn, String situationUz,
                         WordLevel level, int dayIndex, List<DialogueLine> lines, List<String> targetWords) {
        this(id, title, situationEn, situationUz, level, dayIndex, lines, targetWords, TargetLanguage.ENGLISH);
    }

    public DailyDialogue(String id, String title, String situationEn, String situationUz,
                         WordLevel level, int dayIndex, List<DialogueLine> lines, List<String> targetWords,
                         TargetLanguage language) {
        this.id = id;
        this.title = title;
        this.situationEn = situationEn;
        this.situationUz = situationUz;
        this.level = level;
        this.dayIndex = dayIndex;
        this.lines = lines;
        this.targetWords = targetWords;
        this.language = language != null ? language : TargetLanguage.ENGLISH;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getSituationEn() {
        return situationEn;
    }

    public String getSituationUz() {
        return situationUz;
    }

    public WordLevel getLevel() {
        return level;
    }

    public int getDayIndex() {
        return dayIndex;
    }

    public List<DialogueLine> getLines() {
        return lines;
    }

    public List<String> getTargetWords() {
        return targetWords;
    }

    public TargetLanguage getLanguage() {
        return language;
    }

    /**
     * TTS ovozli o'qish uchun to'liq dialog matni
     */
    public String getSpeechScript() {
        StringBuilder sb = new StringBuilder();
        for (DialogueLine line : lines) {
            sb.append(line.getSpeaker()).append(": ").append(line.getTextEn()).append(" ");
        }
        return sb.toString().trim();
    }

    /**
     * Telegramda chiroyli HTML ko'rinishida formatlangan dialog kartochkasi
     */
    public String toFormattedCard() {
        String langFlag = (language == TargetLanguage.RUSSIAN) ? "🇷🇺" : "🇬🇧";
        String langName = (language == TargetLanguage.RUSSIAN) ? "ruscha" : "inglizcha";

        StringBuilder sb = new StringBuilder();
        sb.append("🗣️ <b>KUNLIK REAL DIALOG & SPEAKING (").append(langFlag).append("):</b>\n");
        sb.append("🎬 <b>Mavzu:</b> <b>").append(title).append("</b>\n");
        sb.append("📍 <i>Vaziyat: ").append(situationUz).append("</i>\n");
        sb.append("📊 <b>Daraja:</b> ").append(level.getDisplayName()).append("\n");
        sb.append("━━━━━━━━━━━━━━━━━━━━━\n\n");

        for (DialogueLine line : lines) {
            sb.append(line.getSpeakerIcon()).append(" <b>").append(line.getSpeaker()).append(":</b>\n");
            sb.append(langFlag).append(" <i>\"").append(line.getTextEn()).append("\"</i>\n");
            sb.append("🇺🇿 <i>\"").append(line.getTextUz()).append("\"</i>\n\n");
        }

        sb.append("━━━━━━━━━━━━━━━━━━━━━\n");
        sb.append("💡 <b>Dialogda ishlatilgan bugungi so'zlar:</b>\n");
        sb.append("👉 <code>").append(String.join(", ", targetWords)).append("</code>\n\n");
        sb.append("👇 <b>Ushbu dialogni to'liq ").append(langName).append(" talaffuzda eshitish uchun quyidagi tugmani bosing:</b>");

        return sb.toString();
    }

    public static class DialogueLine {
        private final String speaker;
        private final String speakerIcon;
        private final String textEn; // Target language text
        private final String textUz;

        public DialogueLine(String speaker, String speakerIcon, String textEn, String textUz) {
            this.speaker = speaker;
            this.speakerIcon = speakerIcon;
            this.textEn = textEn;
            this.textUz = textUz;
        }

        public String getSpeaker() {
            return speaker;
        }

        public String getSpeakerIcon() {
            return speakerIcon;
        }

        public String getTextEn() {
            return textEn;
        }

        public String getTextTarget() {
            return textEn;
        }

        public String getTextUz() {
            return textUz;
        }
    }
}
