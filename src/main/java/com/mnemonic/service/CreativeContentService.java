package com.mnemonic.service;

import com.mnemonic.model.UserProfile;
import com.mnemonic.model.Word;

import java.time.LocalDate;
import java.util.Random;

/**
 * Har kuni foydalanuvchiga yuboriladigan eslatmalarni rang-barang, qiziqarli,
 * motivatsion va neyro-xotira layfhaklari bilan boyitilgan holda shakllantiruvchi servis.
 */
public class CreativeContentService {

    private final Random random = new Random();

    // 7 xil neyro-xotira va mnemonika layfhaklari
    private static final String[] MEMORY_HACKS = {
        "💡 <b>Neyro-Xotira Siri #1 (Ebbinghaus Qoidasi):</b>\n<i>Yangi o'rganilgan so'z dastlabki 24 soat ichida bir marta takrorlansa, uning xotirada saqlanish foizi 80% gacha oshadi!</i>",
        "💡 <b>Neyro-Xotira Siri #2 (Kinematik Obrazlar):</b>\n<i>Inson miyasi harakatdagi, yorqin rangli yoki kulgili tasavvurlarni quruq so'zlarga qaraganda 7 barobar mustahkamroq eslab qoladi!</i>",
        "💡 <b>Neyro-Xotira Siri #3 (Fonetik Ko'prik):</b>\n<i>Inglizcha so'zni o'zingiz biladigan o'zbekcha so'zga o'xshatib ilmoq (hook) qilsangiz, miya yangi ma'lumotni 'tanish fayl' deb qabul qiladi.</i>",
        "💡 <b>Neyro-Xotira Siri #4 (Faol Qayta Eslash):</b>\n<i>Lug'atni shunchaki o'qish emas, balki qisqa mashqlarda javobini o'zingiz topishga urinish miyadagi neyron yo'llarini 2 barobar mustahkamlaydi.</i>",
        "💡 <b>Neyro-Xotira Siri #5 (Uyqudan Oldingi Sehr):</b>\n<i>Uyqudan 15-20 daqiqa oldin ko'rib chiqilgan 20 ta mnemonik so'z kechasi miya uyqu fazasida uzoq muddatli xotiraga (LTM) yoziladi!</i>",
        "💡 <b>Neyro-Xotira Siri #6 (Hissiy Bog'liqlik):</b>\n<i>Obraz qanchalik kulgili, bo'rttirilgan yoki g'alati bo'lsa — miyangiz uni bir umrga eslab qoladi!</i>",
        "💡 <b>Neyro-Xotira Siri #7 (Streak Psixologiyasi):</b>\n<i>Har kuni atigi 10 daqiqa vaqt ajratish — haftada 1 marta 2 soat o'rgangandan ko'ra 5 barobar samaraliroq natija beradi!</i>"
    };

    // Poliglotlar va qiziqarli til faktlari
    private static final String[] FUN_FACTS = {
        "🌍 <b>Qiziqarli Fakt:</b> Ingliz tilidagi eng uzun umumiy so'zlardan biri <i>'uncopyrightable'</i> bo'lib, uning birorta harfi takrorlanmaydi!",
        "🌍 <b>Qiziqarli Fakt:</b> Har 98 daqiqada ingliz tiliga bitta yangi so'z qo'shiladi (yiliga taxminan 4,000 ta so'z)!",
        "🌍 <b>Qiziqarli Fakt:</b> Mnemonika so'zi qadimgi yunonlarning xotira ma'budasi <i>Mnemosyne</i> nomidan olingan.",
        "🌍 <b>Qiziqarli Fakt:</b> Dunyodagi mashhur poliglotlar yangi tilni aynan assotsiativ xotira va mnemonik kartochkalar bilan boshlashadi.",
        "🌍 <b>Qiziqarli Fakt:</b> Ingliz tilidagi eng ko'p ma'noga ega so'z — <i>'set'</i> so'zi bo'lib, uning 430 dan ortiq ma'nosi bor!"
    };

    /**
     * Foydalanuvchining darajasi, kuni va strekiga moslashgan boyitilgan eslatma xabarini yaratadi
     */
    public String buildCreativeReminder(UserProfile profile, Word featuredWord) {
        String name = profile.getFirstName() != null ? profile.getFirstName() : "Do'stim";
        int streak = profile.getCurrentStreak();
        int dayIndex = profile.getCurrentDayIndex();
        int dayOfWeek = LocalDate.now().getDayOfWeek().getValue(); // 1..7

        StringBuilder sb = new StringBuilder();

        // 1. Sarlavha (Kunga qarab rang-barang)
        switch (dayOfWeek) {
            case 1 -> sb.append("🚀 <b>DUSHANBA — HAFTANING YANGI BOSHQICHI!</b> 🧠\n\n");
            case 2 -> sb.append("⚡ <b>SESHANBA — INTELLEKT VA XOTIRA KUNI!</b> 💡\n\n");
            case 3 -> sb.append("🔥 <b>CHORSHANBA — MOTIVATSIYA VA STREAK VAQTI!</b> 🎯\n\n");
            case 4 -> sb.append("🌟 <b>PAYSHANBA — MNEMONIK BILIMLAR CHUQURRASHUVI!</b> 🎬\n\n");
            case 5 -> sb.append("🎉 <b>JUMA — KUNLIK G'ALABA VA YANGI SO'ZLAR!</b> 🏆\n\n");
            case 6 -> sb.append("🌿 <b>SHANBA — RELAKS VA YENGIL O'RGANISH!</b> 📚\n\n");
            default -> sb.append("👑 <b>YAKSHANBA — HAFTALIK NATIJALAR VA YANGI MARRALAR!</b> 💎\n\n");
        }

        sb.append("Salom, <b>").append(escapeHtml(name)).append("</b>!\n\n");

        // 2. Streak va rivojlanish statusi
        if (streak > 0) {
            sb.append("🔥 <b>Joriy natijangiz:</b> <code>").append(streak).append(" kun ketma-ket</code> dars qilyapsiz!\n");
            sb.append("✨ <i>Ajoyib natija! Ushbu zanjirni uzmaslik uchun bugungi 20 ta so'z darsingizni bajaring.</i>\n\n");
        } else {
            sb.append("🎯 <b>Bugungi maqsad:</b> Yangi <b>20 ta mnemonik so'z</b> bilan lug'at boyligingizni oshirish!\n");
            sb.append("🚀 <i>Har kuni 10 daqiqa ajratib, 1 oyda 600 ta yangi so'z o'rganing!</i>\n\n");
        }

        // 3. Maxsus "Kun So'zi" tizeri va qiziqarli ilmog'i (Featured Word)
        if (featuredWord != null) {
            sb.append("━━━━━━━━━━━━━━━━━━━━━\n");
            sb.append("🌟 <b>BUGUNGI KUN SO'ZI (TEASER):</b>\n\n");
            sb.append("🔤 <b>").append(featuredWord.getEnglishWord().toUpperCase()).append("</b> ")
              .append(featuredWord.getPronunciation()).append("\n");
            sb.append("🇺🇿 <b>Ma'nosi:</b> <i>").append(featuredWord.getUzbekMeaning()).append("</i>\n");
            sb.append("🔗 <b>Mnemonik Ilmoq:</b> <i>«").append(featuredWord.getMnemonicHook()).append("»</i>\n");
            sb.append("🎬 <b>Tasavvur qiling:</b> ").append(featuredWord.getMnemonicStory()).append("\n");
            sb.append("━━━━━━━━━━━━━━━━━━━━━\n\n");
        }

        // 4. Kunga mos neyro-xotira layfhaki yoki qiziqarli fakt
        String hack = MEMORY_HACKS[(dayIndex + dayOfWeek) % MEMORY_HACKS.length];
        sb.append(hack).append("\n\n");

        sb.append("👇 <b>So'zning ovozli talaffuzini eshitish yoki darsni boshlash uchun tanlang:</b>");

        return sb.toString();
    }

    private String escapeHtml(String text) {
        if (text == null) return "";
        return text.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }
}
