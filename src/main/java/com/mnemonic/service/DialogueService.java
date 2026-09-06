package com.mnemonic.service;

import com.mnemonic.model.DailyDialogue;
import com.mnemonic.model.UserProfile;
import com.mnemonic.model.WordLevel;

import java.util.*;

/**
 * Kunlik 20 ta so'zni hayotiy jonli dialoglarga aylantirib beruvchi
 * va ularning audio matnini boshqaruvchi servis.
 */
public class DialogueService {

    private final Map<WordLevel, List<DailyDialogue>> dialoguesByLevel = new HashMap<>();

    public DialogueService() {
        initDialogues();
    }

    private void initDialogues() {
        // =========================================================================
        // 🟢 BOSHLANG'ICH DARAJA (BEGINNER: A1-A2) DIALOGLARI
        // =========================================================================
        List<DailyDialogue.DialogueLine> beginnerLines = List.of(
            new DailyDialogue.DialogueLine("Alex", "🧔",
                "Hey Emma, cats are naturally curious, but why are you looking at that box? Be careful, the glasses inside are fragile!",
                "Hey Emma, mushuklar tabiatan qiziquvchan, lekin nega bu qutiga qarayapsan? Ehtiyot bo'l, ichidagi stakanlar mo'rt!"),
            new DailyDialogue.DialogueLine("Emma", "👩",
                "I was eager to see what was inside! Don't hesitate to tell me if you need help with moving.",
                "Men shunchaki ichida nima borligini ko'rishga ishtiyoqmand edim! Agar yordam kerak bo'lsa, aytishga ikkilanma."),
            new DailyDialogue.DialogueLine("Alex", "🧔",
                "Thanks Emma! Heavy traffic was a big obstacle this morning. Let's quench our thirst with cold tea and stay calm.",
                "Rahmat Emma! Bugun ertalab tirbandlik katta to'siq bo'ldi. Keling, sovuq choy bilan chanqog'imizni qondiraylik va xotirjam bo'laylik."),
            new DailyDialogue.DialogueLine("Emma", "👩",
                "That sounds like a marvellous idea! You are always so generous and honest with your friends.",
                "Bu ajoyib g'oya! Sen har doim do'stlaring bilan juda saxiy va rostgo'ysan."),
            new DailyDialogue.DialogueLine("Alex", "🧔",
                "To be candid, living a frugal life helps me save money to visit ancient historical monuments!",
                "Samimiy aytsam, tejamkor hayot kechirishim menga qadimiy tarixiy yodgorliklarni borib ko'rishga pul yig'ishga yordam beradi!")
        );

        DailyDialogue beginnerDialog = new DailyDialogue(
            "beginner_day_1",
            "☕ Qahvaxonadagi qiziqarli uchrashuv (At the Café)",
            "Two friends meeting at a café discussing their day and travel plans.",
            "Ikki do'st qahvaxonada ko'rishib, o'z kunlari va sayohat rejalari haqida suhbatlashmoqda.",
            WordLevel.BEGINNER,
            1,
            beginnerLines,
            List.of("curious", "fragile", "eager", "hesitate", "obstacle", "quench", "calm", "marvellous", "generous", "honest", "candid", "frugal", "ancient")
        );

        dialoguesByLevel.computeIfAbsent(WordLevel.BEGINNER, k -> new ArrayList<>()).add(beginnerDialog);

        // =========================================================================
        // 🟡 O'RTA DARAJA (INTERMEDIATE: B1-B2) DIALOGLARI
        // =========================================================================
        List<DailyDialogue.DialogueLine> intermediateLines = List.of(
            new DailyDialogue.DialogueLine("David", "👨‍💼",
                "Sarah, our investors are ambitious, but we need a pragmatic and feasible plan for next month.",
                "Sarah, investorlarimiz shijoatli va talabchan, lekin bizga keyingi oy uchun amaliy va real reja kerak."),
            new DailyDialogue.DialogueLine("Sarah", "👩‍💼",
                "I agree. High market competition will not deter us if our team remains diligent every day.",
                "Qo'shilaman. Agar jamoamiz har kuni tirishqoq va mehnatsevar bo'lsa, kuchli bozor raqobati bizni to'xtatib qololmaydi."),
            new DailyDialogue.DialogueLine("David", "👨‍💼",
                "Exactly! Everyone was elated when we launched the first prototype yesterday.",
                "Aynan shunday! Kecha birinchi prototipni ishga tushirganimizda butun jamoamiz quvonchdan boshlari osmonda edi."),
            new DailyDialogue.DialogueLine("Sarah", "👩‍💼",
                "Yes, but we should not be lenient with security bugs, otherwise our systems stay vulnerable.",
                "Ha, lekin xavfsizlik xatolariga nisbatan yumshoq bo'lmasligimiz kerak, aks holda tizimimiz zaif va himoyasiz qoladi."),
            new DailyDialogue.DialogueLine("David", "👨‍💼",
                "I was reluctant to hire expensive auditors before, but now I see it is essential.",
                "Ilgari qimmat auditorlarni yollashga istaksiz edim, lekin hozir bu juda zarurligini tushundim.")
        );

        DailyDialogue intermediateDialog = new DailyDialogue(
            "intermediate_day_1",
            "💼 Yangi Startap Loyihasi Muhokamasi (Startup Strategy Meeting)",
            "Two project managers discussing strategic plans and security in an office.",
            "Ikki loyiha menejeri ofisda strategik rejalar va xavfsizlik haqida suhbatlashmoqda.",
            WordLevel.INTERMEDIATE,
            1,
            intermediateLines,
            List.of("ambitious", "pragmatic", "feasible", "deter", "diligent", "elated", "lenient", "vulnerable", "reluctant")
        );

        dialoguesByLevel.computeIfAbsent(WordLevel.INTERMEDIATE, k -> new ArrayList<>()).add(intermediateDialog);

        // =========================================================================
        // 🔴 YUQORI DARAJA (ADVANCED: C1-C2) DIALOGLARI
        // =========================================================================
        List<DailyDialogue.DialogueLine> advancedLines = List.of(
            new DailyDialogue.DialogueLine("Dr. Robert", "👨‍🔬",
                "Smartphones are ubiquitous today, yet modern digital trends can be surprisingly ephemeral.",
                "Smartfonlar bugun har yerda hoziru nozir, biroq zamonaviy raqamli trendlar hayratlanarli darajada o'tkinchi bo'lishi mumkin."),
            new DailyDialogue.DialogueLine("Dr. Olivia", "👩‍🔬",
                "Indeed. That is why your research required such meticulous analysis and a lucid explanation.",
                "Haqiqatan ham. Shuning uchun ham sizning tadqiqotingiz sinchkovlik bilan tahlil va ravshan tushuntirishni talab qildi."),
            new DailyDialogue.DialogueLine("Dr. Robert", "👨‍🔬",
                "Nature is resilient, but we must protect our pristine forests from environmental damage.",
                "Tabiat bardoshli va chidamli, ammo biz o'zimizning bokira, toza o'rmonlarimizni ekologik zarardan asrashimiz shart."),
            new DailyDialogue.DialogueLine("Dr. Olivia", "👩‍🔬",
                "Your eloquent speech today proved that a tenacious scientist always discovers the truth.",
                "Sizning bugungi notiqlarcha nutqingiz tirishqoq va qat'iyatli olim doimo haqiqatni kashf etishini isbotladi.")
        );

        DailyDialogue advancedDialog = new DailyDialogue(
            "advanced_day_1",
            "🎓 Xalqaro Ilmiy Simpozium (Global Science Symposium)",
            "Two academic scientists discussing technological trends and environmental research.",
            "Ikki akademik olim texnologik trendlar va ekologik tadqiqotlarni muhokama qilmoqda.",
            WordLevel.ADVANCED,
            1,
            advancedLines,
            List.of("ubiquitous", "ephemeral", "meticulous", "lucid", "resilient", "pristine", "eloquent", "tenacious")
        );

        dialoguesByLevel.computeIfAbsent(WordLevel.ADVANCED, k -> new ArrayList<>()).add(advancedDialog);
    }

    /**
     * Foydalanuvchi darajasi va kuniga mos dialogni qaytaradi
     */
    public DailyDialogue getDialogueForProfile(UserProfile profile) {
        WordLevel level = profile.getSelectedLevel() != null ? profile.getSelectedLevel() : WordLevel.BEGINNER;
        List<DailyDialogue> list = dialoguesByLevel.getOrDefault(level, dialoguesByLevel.get(WordLevel.BEGINNER));
        if (list == null || list.isEmpty()) {
            return null;
        }
        int index = Math.max(0, (profile.getCurrentDayIndex() - 1) % list.size());
        return list.get(index);
    }

    public DailyDialogue getDialogueById(String id) {
        for (List<DailyDialogue> list : dialoguesByLevel.values()) {
            for (DailyDialogue d : list) {
                if (d.getId().equals(id)) {
                    return d;
                }
            }
        }
        return null;
    }
}
