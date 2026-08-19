package com.mnemonic;

import com.mnemonic.bot.EnglishMnemonicBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class Main {

    // Bot konfiguratsiyalari (Kerak bo'lsa o'zingizning tokeningizni kiriting)
    private static final String DEFAULT_BOT_USERNAME = "MnemonicEngBot";
    private static final String DEFAULT_BOT_TOKEN = "8767592148:AAEYoPsMOWwMabavs_I34Lri9zyH6dSyeZU";

    public static void main(String[] args) {
        String botUsername = System.getenv("BOT_USERNAME") != null ? System.getenv("BOT_USERNAME") : DEFAULT_BOT_USERNAME;
        String botToken = System.getenv("BOT_TOKEN") != null ? System.getenv("BOT_TOKEN") : DEFAULT_BOT_TOKEN;

        System.out.println("=================================================");
        System.out.println("🚀 Ingliz Tili Mnemonika Telegram Boti ishga tushmoqda...");
        System.out.println("🤖 Bot Username: @" + botUsername);
        System.out.println("=================================================");

        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            EnglishMnemonicBot bot = new EnglishMnemonicBot(botUsername, botToken);
            botsApi.registerBot(bot);

            System.out.println("✅ Bot muvaffaqiyatli ishga tushdi va xabarlarni qabul qilishga tayyor!");
            System.out.println("👉 Telegramda botingizga kiring va /start buyrug'ini yuboring.");
        } catch (TelegramApiException e) {
            System.err.println("❌ Botni ishga tushirishda xatolik yuz berdi!");
            System.err.println("Xatolik sababi: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
