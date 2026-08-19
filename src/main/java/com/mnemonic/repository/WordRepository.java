package com.mnemonic.repository;

import com.mnemonic.model.Word;
import com.mnemonic.model.WordLevel;

import java.util.*;
import java.util.stream.Collectors;

public class WordRepository {
    private final List<Word> words = new ArrayList<>();
    private final Random random = new Random();

    public WordRepository() {
        initData();
    }

    private void initData() {
        // ==================== BEGINNER (A1 - A2) ====================
        words.add(new Word(
                "abandon",
                "[əˈbændən]",
                "Tashlab ketmoq, tark etmoq",
                "A-bandomiz!",
                "Kemada ketayotgan qaroqchilar kema cho'kayotganini ko'rib, 'A, bandomiz!' deb qichqirib kemani tashlab qochishdi.",
                "They had to abandon their car in the heavy snow.",
                "Ular qalin qorda mashinalarini tashlab ketishga majbur bo'lishdi.",
                WordLevel.BEGINNER
        ));

        words.add(new Word(
                "curious",
                "[ˈkjʊəriəs]",
                "Qiziquvchan, sinchkov",
                "Kuryer",
                "Har safar kuryer eshik qoqqanida, qutida nima borligini bilishga oshiqadigan o'ta qiziquvchan mushuk yugurib keladi.",
                "Cats are naturally curious animals.",
                "Mushuklar tabiatan qiziquvchan hayvonlardir.",
                WordLevel.BEGINNER
        ));

        words.add(new Word(
                "drowsy",
                "[ˈdraʊzi]",
                "Uyqusiragan, mudragan",
                "Dori",
                "Kasal bo'lib kuchli tinchlantiruvchi dori ichgach, ko'zlari suzilib uyqusirab qoldi.",
                "The medication made him feel very drowsy.",
                "Dori uni juda uyqusiratib qo'ydi.",
                WordLevel.BEGINNER
        ));

        words.add(new Word(
                "hesitate",
                "[ˈhezɪteɪt]",
                "Ikkilanmoq, taraddudlanmoq",
                "Hech aytolmaslik",
                "Sinf oldida turib to'g'ri javobni 'hech aytolmay' ikkilanib turgan o'quvchi.",
                "Do not hesitate to ask questions if you need help.",
                "Agar yordam kerak bo'lsa, savol berishga ikkilanmang.",
                WordLevel.BEGINNER
        ));

        words.add(new Word(
                "fragile",
                "[ˈfrædʒaɪl]",
                "Mo'rt, tez sinuvchan",
                "Frajer / Freza",
                "Pochtadan kelgan qutiga 'Fragile' yozilgan, chunki ichidagi billur vaza juda nozik va tez sinadi.",
                "Be careful with that box, the glasses inside are fragile.",
                "Bu qutini ehtiyot qiling, ichidagi stakanlar mo'rt.",
                WordLevel.BEGINNER
        ));

        words.add(new Word(
                "novice",
                "[ˈnɒvɪs]",
                "Yangi boshlovchi, havaskor",
                "Novvoy shogird",
                "Novvoyxonada non yopishni endigina o'rganayotgan yangi boshlovchi shogird.",
                "He is still a novice in computer programming.",
                "U dasturlashda hali yangi boshlovchi.",
                WordLevel.BEGINNER
        ));

        words.add(new Word(
                "quench",
                "[kwentʃ]",
                "Qondirmoq (chanqoqni), o'chirmoq (olovni)",
                "Qaynoq choy / Kventin",
                "Jazirama issiqda muzdek ko'k choy ichib o'z chanqog'ini qondirdi.",
                "A glass of cold water will quench your thirst.",
                "Bir stakan sovuq suv chanqog'ingizni qondiradi.",
                WordLevel.BEGINNER
        ));

        words.add(new Word(
                "obstacle",
                "[ˈɒbstəkl]",
                "To'siq, g'ov",
                "Ob-stakan (Katta stakan)",
                "Yugurish yo'lakchasida bahaybat stakan to'siq bo'lib turibdi va sportchi undan sakrab o'tdi.",
                "Fear is the biggest obstacle to success.",
                "Qo'rquv — muvaffaqiyat yo'lidagi eng katta to'siqdir.",
                WordLevel.BEGINNER
        ));

        // ==================== INTERMEDIATE (B1 - B2) ====================
        words.add(new Word(
                "ambitious",
                "[æmˈbɪʃəs]",
                "Maqsadga intiluvchan, shijoatli",
                "Anjir yeb...",
                "Anjir mevasini yeb o'tirib 'Men bu yil albatta Garvardga kiraman!' deb ulkan rejalar tuzayotgan shijoatli yigit.",
                "She is an ambitious student who wants to be a CEO.",
                "U bosh direktor bo'lishni xohlaydigan intiluvchan talaba.",
                WordLevel.INTERMEDIATE
        ));

        words.add(new Word(
                "candid",
                "[ˈkændɪd]",
                "Samimiy, ochiqko'ngil, rostgo'y",
                "Kand / Konfet",
                "Do'stlariga shirin konfet ulashib, ko'nglidagi bor gapni ochiq va samimiy aytadigan inson.",
                "He gave a candid interview about his past mistakes.",
                "U o'tmishdagi xatolari haqida samimiy intervyu berdi.",
                WordLevel.INTERMEDIATE
        ));

        words.add(new Word(
                "frugal",
                "[ˈfruːɡl]",
                "Tejamkor, tejab-tergab sarflaydigan",
                "Frukta (Arzon meva)",
                "Bozordan faqat arzon frukta/meva sotib olib, har bir tiyinini tejaydigan odam.",
                "They lived a very frugal life to save for a new house.",
                "Ular yangi uyga pul yig'ish uchun juda tejamkor hayot kechirishdi.",
                WordLevel.INTERMEDIATE
        ));

        words.add(new Word(
                "lucid",
                "[ˈluːsɪd]",
                "Ravshan, tiniq, tushunarli",
                "Lyustra",
                "Zaldagi yangi kristall lyustra yonganida butun xona tiniq va ravshan bo'lib ketdi.",
                "The professor gave a lucid explanation of the complex theory.",
                "Professor murakkab nazariyani ravshan va tushunarli tushuntirdi.",
                WordLevel.INTERMEDIATE
        ));

        words.add(new Word(
                "carnivore",
                "[ˈkɑːnɪvɔːr]",
                "Go'shtxo'r hayvon",
                "Qorinda bor",
                "Go'shtxo'r sherning qornida faqat go'sht bor, u aslo o't yemaydi.",
                "Lions and tigers are examples of carnivores.",
                "Sherlar va yo'lbarslar go'shtxo'rlarga misoldir.",
                WordLevel.INTERMEDIATE
        ));

        words.add(new Word(
                "perish",
                "[ˈperɪʃ]",
                "Halok bo'lmoq, yo'q bo'lmoq, nobud bo'lmoq",
                "Parij",
                "Katta muzlik davrida butun Parij shahri muz ostida qolib nobud bo'ldi deb tasavvur qiling.",
                "Without water, all living creatures will perish.",
                "Suvsiz barcha tirik mavjudotlar halok bo'ladi.",
                WordLevel.INTERMEDIATE
        ));

        words.add(new Word(
                "deter",
                "[dɪˈtɜːr]",
                "Qaytarmoq, to'xtatib qolmoq, yo'ldan urmoq",
                "Devor / Detektor",
                "Baland tikanli devor va detektor o'g'rini hovliga kirishdan to'xtatib qoldi.",
                "High security cameras deter criminals from stealing.",
                "Yuqori darajadagi xavfsizlik kameralari jinoyatchilarni o'g'rilikdan qaytaradi.",
                WordLevel.INTERMEDIATE
        ));

        words.add(new Word(
                "elated",
                "[ɪˈleɪtɪd]",
                "Boshi osmonda, benihoyat xursand",
                "Elita / Ilhaq",
                "Eng yaxshi elita universitetiga qabul qilinganini eshitib, quvonchdan boshi osmonga yetdi.",
                "She was elated when she received the job offer.",
                "U ish taklifini olganida quvonchdan boshi osmonda edi.",
                WordLevel.INTERMEDIATE
        ));

        words.add(new Word(
                "pragmatic",
                "[præɡˈmætɪk]",
                "Amaliy, reallikka asoslangan",
                "Praktika",
                "Quruq orzular bilan emas, hayotiy praktika (tajriba) va reallikka qarab ish tutadigan kishi.",
                "We need a pragmatic solution to this financial problem.",
                "Ushbu moliyaviy muammoga amaliy yechim kerak.",
                WordLevel.INTERMEDIATE
        ));

        // ==================== ADVANCED / IELTS (C1 - C2) ====================
        words.add(new Word(
                "resilient",
                "[rɪˈzɪliənt]",
                "Bardoshli, qiyinchilikka chidamli, qayishqoq",
                "Rezinka",
                "Rezinka kabi qanchalik cho'zilsa yoki bosilsa ham, darhol o'z holiga qaytadigan irodali va bardoshli inson.",
                "Children are remarkably resilient and adapt to changes quickly.",
                "Bolalar nihoyatda bardoshli bo'lib, o'zgarishlarga tez moslashadilar.",
                WordLevel.ADVANCED
        ));

        words.add(new Word(
                "subtle",
                "[ˈsʌtl]",
                "Nozik, sezilarsiz, sezgir",
                "Sopol / Sotil",
                "Qadimiy sopol ko'zadagi naqshlar shunchalik nozikki, faqat diqqat bilan qaraganda seziladi.",
                "There is a subtle difference between these two colors.",
                "Bu ikki rang o'rtasida nozik (sezilarsiz) farq bor.",
                WordLevel.ADVANCED
        ));

        words.add(new Word(
                "lucrative",
                "[ˈluːkrətɪv]",
                "Juda foydali, mo'may daromadli",
                "Luk (Piyoz) / Lokomotiv",
                "Bahorda katta yerga luk (piyoz) ekib eksport qilgan fermer mo'may daromadli foyda oldi.",
                "Investing in real estate turned out to be a lucrative business.",
                "Ko'chmas mulkka sarmoya kiritish juda foydali biznes bo'lib chiqdi.",
                WordLevel.ADVANCED
        ));

        words.add(new Word(
                "meticulous",
                "[məˈtɪkjələs]",
                "Sinchkov, o'ta ehtiyotkor, mayda-chuydasigacha aniq",
                "Matematik",
                "Matematik olim har bir formulani mikroskopdek sinchkovlik bilan tekshirib chiqdi.",
                "The architect was meticulous in every detail of the design.",
                "Arxitektor loyihaning har bir detalida o'ta sinchkov edi.",
                WordLevel.ADVANCED
        ));

        words.add(new Word(
                "zealous",
                "[ˈzeləs]",
                "G'ayratli, fidoyi, o'ta intiluvchan",
                "Zelen (Ko'kat)",
                "Bahoriy barra zelen/ko'katlarni yeb kuchga to'lgan, o'z maqsadiga erishish uchun tinmay harakat qiladigan g'ayratli xodim.",
                "He was a zealous supporter of environmental protection.",
                "U atrof-muhitni muhofaza qilishning g'ayratli va fidoyi tarafdori edi.",
                WordLevel.ADVANCED
        ));

        words.add(new Word(
                "benevolent",
                "[bəˈnevələnt]",
                "Saxiy, mehribon, xayr-ehson qiluvchi",
                "Bilet bepul",
                "Muhtojlarga konsert biletlarini bepul tarqatuvchi saxiy va mehribon homiy.",
                "The benevolent gentleman donated millions to the orphanage.",
                "Saxiy janob bolalar uyiga millionlab pul xayriya qildi.",
                WordLevel.ADVANCED
        ));

        words.add(new Word(
                "alleviate",
                "[əˈliːvieɪt]",
                "Yengillashtirmoq, og'riqni kamaytirmoq",
                "Ali va vata",
                "Ali yaralangan do'stining og'riyotgan joyiga dorili paxta/vata qo'yib, uning og'rig'ini yengillashtirdi.",
                "The doctor prescribed pills to alleviate the severe headache.",
                "Shifokor kuchli bosh og'rig'ini yengillashtirish uchun tabletkalar yozib berdi.",
                WordLevel.ADVANCED
        ));

        words.add(new Word(
                "versatile",
                "[ˈvɜːsətaɪl]",
                "Ko'p qirrali, har tomonlama moslashuvchan",
                "Versal",
                "Versal saroyi kabi har qanday me'moriy vaziyatga mos tushadigan, barcha sohada qobiliyatli inson.",
                "Smartphone is a versatile tool for communication and learning.",
                "Smartfon aloqa va o'rganish uchun juda ko'p qirrali vositadir.",
                WordLevel.ADVANCED
        ));

        words.add(new Word(
                "scrutinize",
                "[ˈskruːtɪnaɪz]",
                "Sinchiklab tekshirmoq, ko'zdan kechirmoq",
                "Skrupulyoz / Skripka",
                "Eski skripka ustasi har bir detalni lupa bilan sinchiklab tekshirib chiqdi.",
                "The customs officers will scrutinize all passports carefully.",
                "Bojxona xodimlari barcha pasportlarni sinchiklab tekshiradilar.",
                WordLevel.ADVANCED
        ));

        words.add(new Word(
                "gregarious",
                "[ɡrɪˈɡeəriəs]",
                "Kirishimli, xushchaqchaq, jamoani yaxshi ko'ruvchi",
                "Guruh",
                "Yolg'iz qolishni yoqtirmaydigan, doimo katta guruhlar orasida yuradigan kirishimli yoshlar.",
                "Being gregarious, he made friends very easily in the new school.",
                "Kirishimli bo'lgani uchun, u yangi maktabda juda tez do'stlar orttirdi.",
                WordLevel.ADVANCED
        ));

        words.add(new Word(
                "opulent",
                "[ˈɒpjələnt]",
                "Hashamatli, o'ta boy, serhasham",
                "Opal toshlari / Pul",
                "Xonalariga qimmatbaho opal toshlari qadalgan va oltin bilan bezatilgan hashamatli qasr.",
                "The king lived in an opulent palace surrounded by gold.",
                "Qirol oltinlar bilan o'ralgan hashamatli saroyda yashardi.",
                WordLevel.ADVANCED
        ));
    }

    public List<Word> getAllWords() {
        return Collections.unmodifiableList(words);
    }

    public Optional<Word> getRandomWord() {
        if (words.isEmpty()) return Optional.empty();
        return Optional.of(words.get(random.nextInt(words.size())));
    }

    public Optional<Word> getRandomWordByLevel(WordLevel level) {
        List<Word> filtered = getWordsByLevel(level);
        if (filtered.isEmpty()) return Optional.empty();
        return Optional.of(filtered.get(random.nextInt(filtered.size())));
    }

    public List<Word> getWordsByLevel(WordLevel level) {
        return words.stream()
                .filter(w -> w.getLevel() == level)
                .collect(Collectors.toList());
    }

    public Optional<Word> findByKeyword(String query) {
        if (query == null || query.trim().isEmpty()) return Optional.empty();
        String normalized = query.trim().toLowerCase();

        return words.stream()
                .filter(w -> w.getEnglishWord().toLowerCase().equals(normalized)
                        || w.getEnglishWord().toLowerCase().contains(normalized)
                        || w.getUzbekMeaning().toLowerCase().contains(normalized)
                        || w.getMnemonicHook().toLowerCase().contains(normalized))
                .findFirst();
    }
}
