package com.mnemonic.service;

import com.mnemonic.model.DailyDialogue;
import com.mnemonic.model.TargetLanguage;
import com.mnemonic.model.UserProfile;
import com.mnemonic.model.WordLevel;

import java.util.*;

/**
 * Kunlik 20 ta so'zni hayotiy jonli dialoglarga aylantirib beruvchi
 * va ularning audio matnini boshqaruvchi servis (Ingliz va Rus tillari uchun).
 */
public class DialogueService {

    private final Map<TargetLanguage, Map<WordLevel, List<DailyDialogue>>> dialoguesByLangAndLevel = new EnumMap<>(TargetLanguage.class);

    public DialogueService() {
        for (TargetLanguage lang : TargetLanguage.values()) {
            dialoguesByLangAndLevel.put(lang, new EnumMap<>(WordLevel.class));
        }
        initEnglishDialogues();
        initRussianDialogues();
    }

    private void initEnglishDialogues() {
        // 🟢 BOSHLANG'ICH (A1-A2) INGLIZCHA
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
            "en_beginner_day_1",
            "☕ Qahvaxonadagi qiziqarli uchrashuv (At the Café)",
            "Two friends meeting at a café discussing their day and travel plans.",
            "Ikki do'st qahvaxonada ko'rishib, o'z kunlari va sayohat rejalari haqida suhbatlashmoqda.",
            WordLevel.BEGINNER,
            1,
            beginnerLines,
            List.of("curious", "fragile", "eager", "hesitate", "obstacle", "quench", "calm", "marvellous", "generous", "honest", "candid", "frugal", "ancient"),
            TargetLanguage.ENGLISH
        );
        dialoguesByLangAndLevel.get(TargetLanguage.ENGLISH).computeIfAbsent(WordLevel.BEGINNER, k -> new ArrayList<>()).add(beginnerDialog);

        // 🟡 O'RTA (B1-B2) INGLIZCHA
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
            "en_intermediate_day_1",
            "💼 Yangi Startap Loyihasi Muhokamasi (Startup Strategy Meeting)",
            "Two project managers discussing strategic plans and security in an office.",
            "Ikki loyiha menejeri ofisda strategik rejalar va xavfsizlik haqida suhbatlashmoqda.",
            WordLevel.INTERMEDIATE,
            1,
            intermediateLines,
            List.of("ambitious", "pragmatic", "feasible", "deter", "diligent", "elated", "lenient", "vulnerable", "reluctant"),
            TargetLanguage.ENGLISH
        );
        dialoguesByLangAndLevel.get(TargetLanguage.ENGLISH).computeIfAbsent(WordLevel.INTERMEDIATE, k -> new ArrayList<>()).add(intermediateDialog);

        // 🔴 YUQORI (C1-C2) INGLIZCHA
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
            "en_advanced_day_1",
            "🎓 Xalqaro Ilmiy Simpozium (Global Science Symposium)",
            "Two academic scientists discussing technological trends and environmental research.",
            "Ikki akademik olim texnologik trendlar va ekologik tadqiqotlarni muhokama qilmoqda.",
            WordLevel.ADVANCED,
            1,
            advancedLines,
            List.of("ubiquitous", "ephemeral", "meticulous", "lucid", "resilient", "pristine", "eloquent", "tenacious"),
            TargetLanguage.ENGLISH
        );
        dialoguesByLangAndLevel.get(TargetLanguage.ENGLISH).computeIfAbsent(WordLevel.ADVANCED, k -> new ArrayList<>()).add(advancedDialog);
    }

    private void initRussianDialogues() {
        // 🟢 BOSHLANG'ICH (A1-A2) RUSCHA
        List<DailyDialogue.DialogueLine> ruBeginnerLines = List.of(
            new DailyDialogue.DialogueLine("Иван", "🧔",
                "Здравствуйте, Анна! Какая сегодня прекрасная погода! Вдруг пойдёт дождь, держите зонт осторожно.",
                "Assalomu alaykum, Anna! Bugun qanday ajoyib ob-havo! To'satdan yomg'ir yog'ib qolsa, soyabonni ehtiyotkorlik bilan ushlang."),
            new DailyDialogue.DialogueLine("Анна", "👩",
                "Большое спасибо, Иван! Ваша тёплая улыбка и помощь всегда поднимают мне настроение.",
                "Katta rahmat, Ivan! Sizning samimiy tabassumingiz va yordamingiz doimo kayfiyatimni ko'taradi."),
            new DailyDialogue.DialogueLine("Иван", "🧔",
                "Для меня наша дружба — настоящее счастье! У нас есть время выпить горячий чай и верить в успех.",
                "Men uchun do'stligimiz — haqiqiy baxt! Bizda qaynoq choy ichishga va muvaffaqiyatga ishonishga vaqt bor."),
            new DailyDialogue.DialogueLine("Анна", "👩",
                "Это чистая правда! Моя заветная мечта — отправиться в сказочное путешествие уже завтра.",
                "Bu toza haqiqat! Mening ezgu orzum — ertagayoq ertaknamo ajoyib sayohatga otlanish."),
            new DailyDialogue.DialogueLine("Иван", "🧔",
                "Прекрасно! Обратите внимание на маршрут, а вера и надежда приведут нас к победе!",
                "Ajoyib! Marshrutga diqqat qarating, ishonch va umid esa bizni g'alabaga yetaklaydi!")
        );

        DailyDialogue ruBeginnerDialog = new DailyDialogue(
            "ru_beginner_day_1",
            "☕ Встреча в уютном кафе (Qahvaxonadagi samimiy uchrashuv)",
            "Два друга встретились в кафе, чтобы обсудить поездку, погоду и свои мечты.",
            "Ikki do'st qahvaxonada uchrashib, sayohat, ob-havo va orzulari haqida suhbatlashmoqda.",
            WordLevel.BEGINNER,
            1,
            ruBeginnerLines,
            List.of("Спасибо", "Вдруг", "Мечта", "Победа", "Погода", "Осторожно", "Улыбка", "Дружба", "Помощь", "Сказка", "Время", "Надежда", "Счастье", "Путешествие", "Внимание", "Правда", "Успех", "Завтра", "Здравствуйте"),
            TargetLanguage.RUSSIAN
        );
        dialoguesByLangAndLevel.get(TargetLanguage.RUSSIAN).computeIfAbsent(WordLevel.BEGINNER, k -> new ArrayList<>()).add(ruBeginnerDialog);

        // 🟡 O'RTA (B1-B2) RUSCHA
        List<DailyDialogue.DialogueLine> ruIntermediateLines = List.of(
            new DailyDialogue.DialogueLine("Михаил", "👨‍💼",
                "Никакое препятствие не сломит нас, если в команде есть решительность и вдохновение.",
                "Agar jamoada qat'iyat va ilhom bo'lsa, hech qanday to'siq bizni sindira olmaydi."),
            new DailyDialogue.DialogueLine("Елена", "👩‍💼",
                "Согласна! Наше научное исследование доказало, что любопытство клиентов стимулирует спрос.",
                "Qo'shilaman! Bizning ilmiy tadqiqotimiz mijozlarning qiziquvchanligi talabni rag'batlantirishini isbotladi."),
            new DailyDialogue.DialogueLine("Михаил", "👨‍💼",
                "Это грандиозное достижение! Взаимное уважение и полное доверие инвесторов укрепят позиции.",
                "Bu ulkan yutuq! O'zaro hurmat va investorlarning to'liq ishonchi pozitsiyamizni mustahkamlaydi."),
            new DailyDialogue.DialogueLine("Елена", "👩‍💼",
                "Отбросим любое сомнение: персональная ответственность каждого сотрудника гарантирует победу.",
                "Har qanday shubhani chetga suramiz: har bir xodimning shaxsiy mas'uliyati g'alabani kafolatlaydi.")
        );

        DailyDialogue ruIntermediateDialog = new DailyDialogue(
            "ru_intermediate_day_1",
            "💼 Обсуждение стартапа и стратегии (Startap va strategiya)",
            "Два руководителя обсуждают исследование рынка и преодоление препятствий.",
            "Ikki rahbar bozor tadqiqoti va to'siqlarni yengib o'tishni muhokama qilmoqda.",
            WordLevel.INTERMEDIATE,
            1,
            ruIntermediateLines,
            List.of("Препятствие", "Вдохновение", "Решительность", "Исследование", "Любопытство", "Достижение", "Уважение", "Сомнение", "Ответственность", "Доверие"),
            TargetLanguage.RUSSIAN
        );
        dialoguesByLangAndLevel.get(TargetLanguage.RUSSIAN).computeIfAbsent(WordLevel.INTERMEDIATE, k -> new ArrayList<>()).add(ruIntermediateDialog);

        // 🔴 YUQORI (C1-C2) RUSCHA
        List<DailyDialogue.DialogueLine> ruAdvancedLines = List.of(
            new DailyDialogue.DialogueLine("Профессор Соколов", "👨‍🔬",
                "Ваша безупречная репутация и красноречивый доклад вызвали восхищение президиума.",
                "Sizning benuqson obro'yingiz va fasohatli maruzangiz prezidiumning yuksak olqishiga sazovor bo'ldi."),
            new DailyDialogue.DialogueLine("Доктор Морозова", "👩‍🔬",
                "Благодарю вас. Это был кропотливый труд, но фундаментальные законы — незыблемая основа истины.",
                "Minnatdorman. Bu mashaqqatli va sinchkov mehnat edi, ammo fundamental qonunlar — haqiqatning o'zgarmas poydevoridir."),
            new DailyDialogue.DialogueLine("Профессор Соколов", "👨‍🔬",
                "Время скоротечно, однако утончённый подход учёного преодолеет любые непреодолимые преграды.",
                "Vaqt tez o'tuvchidir, ammo olimning nafis va nozik yondashuvi har qanday yengib bo'lmas to'siqlarni zabt etadi.")
        );

        DailyDialogue ruAdvancedDialog = new DailyDialogue(
            "ru_advanced_day_1",
            "🎓 Научный Симпозиум (Xalqaro Ilmiy Simpozium)",
            "Академики обсуждают фундаментальные научные открытия и преодоление трудностей.",
            "Akademiklar fundamental ilmiy kashfiyotlar va qiyinchiliklarni yengishni muhokama qilmoqda.",
            WordLevel.ADVANCED,
            1,
            ruAdvancedLines,
            List.of("Безупречный", "Красноречивый", "Кропотливый", "Незыблемый", "Скоротечный", "Утончённый", "Непреодолимый"),
            TargetLanguage.RUSSIAN
        );
        dialoguesByLangAndLevel.get(TargetLanguage.RUSSIAN).computeIfAbsent(WordLevel.ADVANCED, k -> new ArrayList<>()).add(ruAdvancedDialog);
    }

    /**
     * Foydalanuvchi tili, darajasi va kuniga mos dialogni qaytaradi
     */
    public DailyDialogue getDialogueForProfile(UserProfile profile) {
        TargetLanguage lang = (profile.getTargetLanguage() != null) ? profile.getTargetLanguage() : TargetLanguage.ENGLISH;
        WordLevel level = (profile.getSelectedLevel() != null) ? profile.getSelectedLevel() : WordLevel.BEGINNER;

        Map<WordLevel, List<DailyDialogue>> levelMap = dialoguesByLangAndLevel.getOrDefault(lang, dialoguesByLangAndLevel.get(TargetLanguage.ENGLISH));
        List<DailyDialogue> list = levelMap.getOrDefault(level, levelMap.get(WordLevel.BEGINNER));
        if (list == null || list.isEmpty()) {
            return null;
        }
        int index = Math.max(0, (profile.getCurrentDayIndex() - 1) % list.size());
        return list.get(index);
    }

    public DailyDialogue getDialogueById(String id) {
        for (Map<WordLevel, List<DailyDialogue>> levelMap : dialoguesByLangAndLevel.values()) {
            for (List<DailyDialogue> list : levelMap.values()) {
                for (DailyDialogue d : list) {
                    if (d.getId().equals(id)) {
                        return d;
                    }
                }
            }
        }
        return null;
    }
}
