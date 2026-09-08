package com.mnemonic.service;

import com.mnemonic.model.DailyDialogue;
import com.mnemonic.model.TargetLanguage;
import com.mnemonic.model.Word;
import org.telegram.telegrambots.meta.api.methods.send.SendVoice;
import org.telegram.telegrambots.meta.api.objects.InputFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * Ingliz va Ruscha so'z hamda iboralarning haqiqiy ovozli talaffuzini yuklab beruvchi va
 * Telegram orqali yuboruvchi audio servis.
 * Yuklangan audio fayllar data/audio_cache/ papkasida keshlanadi.
 */
public class AudioPronunciationService {

    private final File cacheDir;

    public AudioPronunciationService() {
        this.cacheDir = new File("data/audio_cache");
        if (!cacheDir.exists()) {
            cacheDir.mkdirs();
        }
    }

    public File getOrDownloadAudio(String text) {
        return getOrDownloadAudio(text, "en");
    }

    /**
     * So'z yoki matn uchun MP3 faylni keshdan oladi yoki Google TTS orqali yuklab saqlaydi.
     * langCode: "en" yoki "ru"
     */
    public synchronized File getOrDownloadAudio(String text, String langCode) {
        if (text == null || text.trim().isEmpty()) {
            return null;
        }

        String safeLang = (langCode != null && langCode.toLowerCase().startsWith("ru")) ? "ru" : "en";
        int hash = Math.abs(text.hashCode());
        String sanitized = text.trim().toLowerCase().replaceAll("[^a-zA-Z0-9\\u0400-\\u04FF_\\-]", "_");
        if (sanitized.length() > 30) {
            sanitized = sanitized.substring(0, 30);
        }
        String fileName = safeLang + "_" + sanitized + "_" + hash + ".mp3";
        File audioFile = new File(cacheDir, fileName);

        if (audioFile.exists() && audioFile.length() > 0) {
            return audioFile;
        }

        try {
            String encodedText = URLEncoder.encode(text.trim(), StandardCharsets.UTF_8);
            String urlStr = "https://translate.google.com/translate_tts?ie=UTF-8&tl=" + safeLang + "&client=tw-ob&q=" + encodedText;

            URL url = new URL(urlStr);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36");
            connection.setConnectTimeout(8000);
            connection.setReadTimeout(8000);

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (InputStream in = connection.getInputStream();
                     FileOutputStream out = new FileOutputStream(audioFile)) {
                    byte[] buffer = new byte[4096];
                    int bytesRead;
                    while ((bytesRead = in.read(buffer)) != -1) {
                        out.write(buffer, 0, bytesRead);
                    }
                }
                return audioFile;
            } else {
                System.err.println("⚠️ TTS audio yuklashda xatolik (" + responseCode + "): " + text + " [" + safeLang + "]");
            }
        } catch (Exception e) {
            System.err.println("⚠️ TTS yuklashda xatolik yuz berdi: " + e.getMessage());
        }

        return null;
    }

    /**
     * Berilgan Word modeli uchun Telegram SendVoice obyektini tayyorlaydi
     */
    public SendVoice createVoiceMessage(long chatId, Word word) {
        String langCode = word.getLanguage() != null ? word.getLanguage().getCode() : "en";
        File audioFile = getOrDownloadAudio(word.getEnglishWord(), langCode);
        if (audioFile == null || !audioFile.exists()) {
            return null;
        }

        String flag = (word.getLanguage() == TargetLanguage.RUSSIAN) ? "🇷🇺" : "🇬🇧";
        SendVoice sendVoice = new SendVoice();
        sendVoice.setChatId(String.valueOf(chatId));
        sendVoice.setVoice(new InputFile(audioFile));
        sendVoice.setCaption("🔊 <b>Talaffuzi (" + flag + "):</b> <code>" + word.getEnglishWord() + "</code> " +
                             word.getPronunciation() + "\n" +
                             "🇺🇿 <b>Ma'nosi:</b> <b>" + word.getUzbekMeaning() + "</b>\n" +
                             "💡 <i>Fonetik ilmoq: " + word.getMnemonicHook() + "</i>");
        sendVoice.setParseMode("HTML");
        return sendVoice;
    }

    /**
     * Ixtiyoriy so'z yoki matn uchun Telegram SendVoice obyektini tayyorlaydi
     */
    public SendVoice createVoiceMessage(long chatId, String wordText, String caption) {
        return createVoiceMessage(chatId, wordText, caption, "en");
    }

    public SendVoice createVoiceMessage(long chatId, String wordText, String caption, String langCode) {
        File audioFile = getOrDownloadAudio(wordText, langCode);
        if (audioFile == null || !audioFile.exists()) {
            return null;
        }

        SendVoice sendVoice = new SendVoice();
        sendVoice.setChatId(String.valueOf(chatId));
        sendVoice.setVoice(new InputFile(audioFile));
        sendVoice.setCaption(caption);
        sendVoice.setParseMode("HTML");
        return sendVoice;
    }

    /**
     * Butun dialog uchun to'liq ovozli xabar (SendVoice) tayyorlaydi
     */
    public SendVoice createDialogueVoiceMessage(long chatId, DailyDialogue dialogue) {
        String langCode = dialogue.getLanguage() != null ? dialogue.getLanguage().getCode() : "en";
        File audioFile = getOrDownloadAudio(dialogue.getSpeechScript(), langCode);
        if (audioFile == null || !audioFile.exists()) {
            return null;
        }

        String flag = (dialogue.getLanguage() == TargetLanguage.RUSSIAN) ? "🇷🇺" : "🇬🇧";
        SendVoice sendVoice = new SendVoice();
        sendVoice.setChatId(String.valueOf(chatId));
        sendVoice.setVoice(new InputFile(audioFile));
        sendVoice.setCaption("🎧 <b>Audio Dialog (" + flag + "):</b> " + dialogue.getTitle() + "\n" +
                             "📊 <b>Daraja:</b> " + dialogue.getLevel().getDisplayName() + "\n" +
                             "💡 <i>Bugungi 20 ta so'z real suhbatda qo'llangan. Eshitib, birga takrorlang!</i>");
        sendVoice.setParseMode("HTML");
        return sendVoice;
    }
}
