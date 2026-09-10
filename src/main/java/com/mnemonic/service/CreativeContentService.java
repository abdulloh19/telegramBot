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
        LocalDate today = LocalDate.now();

        // Oxirgi kirgan sanasi va necha kun kirmaganini aniqlash
        LocalDate lastActive = profile.getLastActiveDate();
        long daysAbsent = 1;
        String lastActiveFormatted = "ro'yxatdan o'tganingizda";
        if (lastActive != null) {
            daysAbsent = java.time.temporal.ChronoUnit.DAYS.between(lastActive, today);
            lastActiveFormatted = lastActive.format(java.time.format.DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        }

        StringBuilder sb = new StringBuilder();

        // 1. 10 Yillik Kognitiv Psixolog Shoshilinch Sarlavhasi
        if (daysAbsent <= 1) {
            sb.append("🧠 <b>24 SOATLIK NEYRO-FAZA | PSIXOLOGIK OGOHLANTIRISH</b>\n\n");
            sb.append("Salom, <b>").append(escapeHtml(name)).append("</b>!\n");
            sb.append("📅 <i>Oxirgi marta kecha (").append(lastActiveFormatted).append(") dars qilgan edingiz — oradan <b>1 kun</b> o'tdi.</i>\n\n");
            sb.append("💡 <b>Kognitiv Psixolog Tashxisi:</b>\n");
            sb.append("Ebbinghaus qonuniyatiga ko'ra, inson miyasi dastlabki 24 soatda yangi ma'lumotning <b>50-60% ini</b> 'keraksiz' deb o'chirib yuboradi. ");
            sb.append("Agar bugun atigi <b>120 soniya</b> ajratib so'zlarni ko'zdan kechirmasangiz, kechagi barcha intellektual mehnatingiz havoga uchadi!\n\n");
            sb.append("🔥 <i>O'z mehnatingizni qadrlang — miyangizga 'Bu so'zlar menga kerak!' degan signalni bering!</i>\n\n");
        } else if (daysAbsent == 2) {
            sb.append("🚨 <b>48 SOATLIK QIZIL CHIZIQ | NEYRON ZANJIR XAVFI!</b>\n\n");
            sb.append("<b>").append(escapeHtml(name)).append("</b>, ogoh bo'ling!\n");
            sb.append("📅 <i>Siz oxirgi marta <b>2 kun oldin</b> (").append(lastActiveFormatted).append(") kirdingiz!</i>\n\n");
            sb.append("⚡ <b>10 Yillik Psixologiya Tajribasidan Xulosa:</b>\n");
            sb.append("48 soatlik tanaffus — bu xotiraning eng xavfli nuqtasi. Hozir miyangizdagi yangi sinapslar faol ravishda zaiflashmoqda. ");
            sb.append("Siz shunchaki so'zlarni emas, balki shakllanayotgan <b>odatingizni va intizomingizni</b> yo'qotmoqdasiz!\n\n");
            sb.append("⚠️ <i>Ertaga kech bo'ladi, noldan boshlash esa 5 barobar qiyinroq. O'zingizga bergan so'zni eslang!</i>\n\n");
        } else {
            sb.append("🆘 <b>SHOSHILINCH NEYRO-KORREKSIYA | ").append(daysAbsent).append(" KUNLIK SUKUT!</b>\n\n");
            sb.append("Hurmatli <b>").append(escapeHtml(name)).append("</b>!\n");
            sb.append("📅 <i>Oxirgi marta <b>").append(daysAbsent).append(" kun oldin</b> (").append(lastActiveFormatted).append(") kirgan edingiz!</i>\n\n");
            sb.append("💔 <b>Bixevioral Psixologik Tahlil:</b>\n");
            sb.append("72 soatdan ortiq sukut — odatning 90% ga so'nishiga olib keladi. Miyangiz sizni <i>'Ertaga kirsam ham bo'ladi'</i> degan illyuziya bilan aldayapti. ");
            sb.append("Lekin bilasiz-ku — 'erta' hech qachon kelmaydi! Xorijiy tilni mukammal o'rganish orzuingiz shunchaki havoga aylanib ketishiga yo'l qo'ymang!\n\n");
            sb.append("🎯 <i>Dangasalik zanjirini hoziroq uzing! Atigi 1 ta mashq bajaring va o'zingizni hurmat qilishni tiklang!</i>\n\n");
        }

        // 2. Yo'qotish azobi (Loss Aversion) indikatori
        if (streak > 0) {
            sb.append("🔥 <b>Yo'qotish xavfi:</b> <code>").append(streak).append(" kunlik zanjir</code> bugun uzilishi mumkin!\n");
        }
        sb.append("📚 <b>To'plangan bilimingiz:</b> ").append(profile.getTotalWordsLearned()).append(" ta so'z\n\n");

        // 3. Featured Word Teaser
        if (featuredWord != null) {
            sb.append("━━━━━━━━━━━━━━━━━━━━━\n");
            sb.append("🌟 <b>BUGUNGI QUTQARUVCHI SO'Z:</b>\n");
            sb.append("🔤 <b>").append(featuredWord.getEnglishWord().toUpperCase()).append("</b> ")
              .append(featuredWord.getPronunciation()).append("\n");
            sb.append("🇺🇿 <b>Ma'nosi:</b> <i>").append(featuredWord.getUzbekMeaning()).append("</i>\n");
            sb.append("🔗 <b>Mnemonik Kalit:</b> <i>«").append(featuredWord.getMnemonicHook()).append("»</i>\n");
            sb.append("━━━━━━━━━━━━━━━━━━━━━\n\n");
        }

        sb.append("👇 <b>Qaror qabul qiling: Hozir 120 soniya ajrating va xotirangizni qutqaring!</b>");

        return sb.toString();
    }

    /**
     * 3 kun yoki undan ortiq kirmagan foydalanuvchilar uchun ijodiy va ta'sirchan "Sizni sog'indik" xabari
     */
    public String buildWeMissYouReminder(UserProfile profile, long daysAbsent) {
        String firstName = profile.getFirstName() != null ? profile.getFirstName() : "Qadrli o'quvchimiz";
        String username = profile.getUsername() != null && !profile.getUsername().isBlank()
                ? "@" + profile.getUsername().replace("@", "")
                : "";

        String userDisplay = username.isEmpty()
                ? "<b>" + escapeHtml(firstName) + "</b>"
                : "<b>" + escapeHtml(firstName) + "</b> (" + username + ")";

        int streak = profile.getCurrentStreak();
        int wordsCount = profile.getTotalWordsLearned();

        StringBuilder sb = new StringBuilder();
        sb.append("🌟 <b>Sizni judayam sog'indik, ").append(userDisplay).append("!</b> 🥺💔\n\n");
        sb.append("Botimizga kirmaganingizga mana <b>").append(daysAbsent).append(" kun</b> bo'ldi!\n\n");

        sb.append("⚡ <b>Siz to'plagan ulkan bilimlar xavf ostida:</b>\n");
        if (streak > 0) {
            sb.append("• 🔥 <b>").append(streak).append(" kunlik Streak</b> zanjiringiz uzilish arafasida!\n");
        }
        sb.append("• 📚 <b>").append(wordsCount).append(" ta</b> o'rganilgan so'z miyangizda so'nib bormoqda!\n\n");

        sb.append("🧠 <b>Kognitiv Psixologik Fakt:</b>\n");
        sb.append("<i>Inson miyasi 72 soat (3 kun) davomida yangi til bilan shug'ullanmasa, yangi neyron zanjirlarining 70% zaiflashadi. ");
        sb.append("Lekin hozir atigi <b>2 daqiqa</b> vaqt ajratib 1 ta dars ko'rib chiqsangiz — butun natijangiz saqlanib qoladi!</i>\n\n");

        sb.append("🚀 O'zingizga bergan va'dangizni unutmang, birga maqsad sari intilamiz!\n\n");
        sb.append("👇 <b>Quyidagi tugmani bosing va darhol darsni davom ettiring:</b>");

        return sb.toString();
    }

    private String escapeHtml(String text) {
        if (text == null) return "";
        return text.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }
}
