package com.mnemonic.repository;

import com.mnemonic.model.Word;
import com.mnemonic.model.WordLevel;

import java.util.*;
import java.util.stream.Collectors;

public class WordRepository {
    private final List<Word> words = new ArrayList<>();
    private final Random random = new Random();
    public static final int WORDS_PER_DAY = 20;

    public WordRepository() {
        initData();
    }

    private void initData() {
        // =========================================================================
        // 🟢 BOSHLANG'ICH DARAJA (BEGINNER: A1 - A2) — 20 TA SO'Z
        // =========================================================================
        words.add(new Word("abandon", "[əˈbændən]", "Tashlab ketmoq, tark etmoq", "A-bandomiz!",
                "Kemada ketayotgan qaroqchilar kema cho'kayotganini ko'rib, 'A, bandomiz!' deb qichqirib kemani tashlab qochishdi.",
                "They had to abandon their car in the heavy snow.", "Ular qalin qorda mashinalarini tashlab ketishga majbur bo'lishdi.", WordLevel.BEGINNER));

        words.add(new Word("curious", "[ˈkjʊəriəs]", "Qiziquvchan, sinchkov", "Kuryer",
                "Har safar kuryer eshik qoqqanida, qutida nima borligini bilishga oshiqadigan o'ta qiziquvchan mushuk yugurib keladi.",
                "Cats are naturally curious animals.", "Mushuklar tabiatan qiziquvchan hayvonlardir.", WordLevel.BEGINNER));

        words.add(new Word("drowsy", "[ˈdraʊzi]", "Uyqusiragan, mudragan", "Dori",
                "Kasal bo'lib kuchli tinchlantiruvchi dori ichgach, ko'zlari suzilib uyqusirab qoldi.",
                "The medication made him feel very drowsy.", "Dori uni juda uyqusiratib qo'ydi.", WordLevel.BEGINNER));

        words.add(new Word("hesitate", "[ˈhezɪteɪt]", "Ikkilanmoq, taraddudlanmoq", "Hech aytolmaslik",
                "Sinf oldida turib to'g'ri javobni 'hech aytolmay' ikkilanib turgan o'quvchi.",
                "Do not hesitate to ask questions if you need help.", "Agar yordam kerak bo'lsa, savol berishga ikkilanmang.", WordLevel.BEGINNER));

        words.add(new Word("fragile", "[ˈfrædʒaɪl]", "Mo'rt, tez sinuvchan", "Frajer / Freza",
                "Pochtadan kelgan qutiga 'Fragile' yozilgan, chunki ichidagi billur vaza juda nozik va tez sinadi.",
                "Be careful with that box, the glasses inside are fragile.", "Bu qutini ehtiyot qiling, ichidagi stakanlar mo'rt.", WordLevel.BEGINNER));

        words.add(new Word("novice", "[ˈnɒvɪs]", "Yangi boshlovchi, havaskor", "Novvoy shogird",
                "Novvoyxonada non yopishni endigina o'rganayotgan yangi boshlovchi shogird.",
                "He is still a novice in computer programming.", "U dasturlashda hali yangi boshlovchi.", WordLevel.BEGINNER));

        words.add(new Word("quench", "[kwentʃ]", "Qondirmoq (chanqoqni)", "Qaynoq choy / Kventin",
                "Jazirama issiqda muzdek ko'k choy ichib o'z chanqog'ini qondirdi.",
                "A glass of cold water will quench your thirst.", "Bir stakan sovuq suv chanqog'ingizni qondiradi.", WordLevel.BEGINNER));

        words.add(new Word("obstacle", "[ˈɒbstəkl]", "To'siq, g'ov", "Ob-stakan (Katta stakan)",
                "Yugurish yo'lakchasida bahaybat stakan to'siq bo'lib turibdi va sportchi undan sakrab o'tdi.",
                "Fear is the biggest obstacle to success.", "Qo'rquv — muvaffaqiyat yo'lidagi eng katta to'siqdir.", WordLevel.BEGINNER));

        words.add(new Word("marvellous", "[ˈmɑːvələs]", "Ajoyib, hayratlanarli", "Marvel",
                "Marvel kinolaridagi kabi hayratlanarli va ajoyib sehrli manzara.",
                "We had a marvellous time at the beach yesterday.", "Biz kecha plyajda ajoyib vaqt o'tkazdik.", WordLevel.BEGINNER));

        words.add(new Word("candid", "[ˈkændɪd]", "Samimiy, ochiqko'ngil", "Kand / Konfet",
                "Do'stlariga shirin konfet ulashib, ko'nglidagi bor gapni ochiq va samimiy aytadigan inson.",
                "He gave a candid interview about his past mistakes.", "U o'tmishdagi xatolari haqida samimiy intervyu berdi.", WordLevel.BEGINNER));

        words.add(new Word("frugal", "[ˈfruːɡl]", "Tejamkor, tejab sarflaydigan", "Frukta (Arzon meva)",
                "Bozordan faqat arzon frukta/meva sotib olib, har bir tiyinini tejaydigan odam.",
                "They lived a very frugal life to save for a new house.", "Ular yangi uyga pul yig'ish uchun juda tejamkor hayot kechirishdi.", WordLevel.BEGINNER));

        words.add(new Word("carnivore", "[ˈkɑːnɪvɔːr]", "Go'shtxo'r hayvon", "Qorinda bor",
                "Go'shtxo'r sherning qornida faqat go'sht bor, u aslo o't yemaydi.",
                "Lions and tigers are examples of carnivores.", "Sherlar va yo'lbarslar go'shtxo'rlarga misoldir.", WordLevel.BEGINNER));

        words.add(new Word("lucid", "[ˈluːsɪd]", "Ravshan, tiniq, tushunarli", "Lyustra",
                "Zaldagi yangi kristall lyustra yonganida butun xona tiniq va ravshan bo'lib ketdi.",
                "The professor gave a lucid explanation of the complex theory.", "Professor murakkab nazariyani ravshan va tushunarli tushuntirdi.", WordLevel.BEGINNER));

        words.add(new Word("ancient", "[ˈeɪnʃənt]", "Qadimiy, ko'hna", "Anjir / Eshon",
                "Qadimiy eshonlar bog'idagi 500 yillik anjir daraxti ostida suhbatlashishdi.",
                "Rome is famous for its ancient monuments.", "Rim o'zining qadimiy yodgorliklari bilan mashhur.", WordLevel.BEGINNER));

        words.add(new Word("brave", "[breɪv]", "Jasur, qo'rqmas, botir", "Beret / Breker",
                "Qizil beret kiyib daryoga cho'kayotgan bolani qutqarib qolgan jasur yigit.",
                "The brave firefighter rescued the cat from the fire.", "Jasur o't o'chiruvchi mushukni olovdan qutqardi.", WordLevel.BEGINNER));

        words.add(new Word("calm", "[kɑːm]", "Xotirjam, tinch, osoyishta", "Qalam / Komil",
                "Qo'liga qalam olib, shovqin-suron orasida ham xotirjam rasm chizib o'tirgan Komil.",
                "Try to stay calm during the exam.", "Imtihon paytida xotirjam bo'lishga harakat qiling.", WordLevel.BEGINNER));

        words.add(new Word("eager", "[ˈiːɡər]", "Ishtiyoqmand, chanqoq", "Igor / O'g'ri",
                "Yangi chet tilini o'rganishga o'ta ishtiyoqmand talaba har kuni 3 soat shug'ullanadi.",
                "The students were eager to start the new project.", "Talabalar yangi loyihani boshlashga ishtiyoqmand edilar.", WordLevel.BEGINNER));

        words.add(new Word("fierce", "[fɪəs]", "Shiddatli, yirtqich, quturgan", "Fursat / Fikr",
                "O'rmonda kutilmaganda shiddatli va yirtqich qora bo'riga duch kelishdi.",
                "A fierce storm destroyed several houses in the village.", "Shiddatli bo'ron qishloqdagi bir nechta uylarni vayron qildi.", WordLevel.BEGINNER));

        words.add(new Word("generous", "[ˈdʒenərəs]", "Saxiy, qo'li ochiq", "General",
                "O'zining barcha maoshini muhtojlarga ulashgan saxiy general.",
                "It was generous of you to pay for dinner.", "Kechki ovqat uchun to'laganingiz juda saxiy ish bo'ldi.", WordLevel.BEGINNER));

        words.add(new Word("honest", "[ˈɒnɪst]", "Rostgo'y, to'g'riso'z, halol", "Onasi",
                "Onasiga hech qachon yolg'on gapirmaydigan, doim haqiqatni aytadigan rostgo'y bola.",
                "He gave an honest answer to the difficult question.", "U qiyin savolga rostgo'y javob berdi.", WordLevel.BEGINNER));


        // =========================================================================
        // 🟡 O'RTA DARAJA (INTERMEDIATE: B1 - B2) — 20 TA SO'Z
        // =========================================================================
        words.add(new Word("ambitious", "[æmˈbɪʃəs]", "Maqsadga intiluvchan, shijoatli", "Anjir yeb...",
                "Anjir mevasini yeb o'tirib 'Men bu yil albatta Garvardga kiraman!' deb ulkan rejalar tuzayotgan shijoatli yigit.",
                "She is an ambitious student who wants to be a CEO.", "U bosh direktor bo'lishni xohlaydigan intiluvchan talaba.", WordLevel.INTERMEDIATE));

        words.add(new Word("perish", "[ˈperɪʃ]", "Halok bo'lmoq, yo'q bo'lmoq", "Parij",
                "Katta muzlik davrida butun Parij shahri muz ostida qolib nobud bo'ldi deb tasavvur qiling.",
                "Without water, all living creatures will perish.", "Suvsiz barcha tirik mavjudotlar halok bo'ladi.", WordLevel.INTERMEDIATE));

        words.add(new Word("deter", "[dɪˈtɜːr]", "Qaytarmoq, to'xtatib qolmoq", "Devor / Detektor",
                "Baland tikanli devor va detektor o'g'rini hovliga kirishdan to'xtatib qoldi.",
                "High security cameras deter criminals from stealing.", "Yuqori darajadagi xavfsizlik kameralari jinoyatchilarni o'g'rilikdan qaytaradi.", WordLevel.INTERMEDIATE));

        words.add(new Word("elated", "[ɪˈleɪtɪd]", "Quvonchdan boshi osmonda", "Elita / Ilhaq",
                "Eng yaxshi elita universitetiga qabul qilinganini eshitib, quvonchdan boshi osmonga yetdi.",
                "She was elated when she received the job offer.", "U ish taklifini olganida quvonchdan boshi osmonda edi.", WordLevel.INTERMEDIATE));

        words.add(new Word("pragmatic", "[præɡˈmætɪk]", "Amaliy, tajribaga asoslangan", "Praktika",
                "Quruq orzular bilan emas, hayotiy praktika (tajriba) va reallikka qarab ish tutadigan kishi.",
                "We need a pragmatic solution to this financial problem.", "Ushbu moliyaviy muammoga amaliy yechim kerak.", WordLevel.INTERMEDIATE));

        words.add(new Word("dormant", "[ˈdɔːmənt]", "Harakatsiz, uxlab yotgan", "Dormon / Dormir",
                "Yuz yillardan buyon hech qanday harakat bildirmay uxlab yotgan qadimgi vulqon.",
                "The volcano has been dormant for hundreds of years.", "Vulqon yuzlab yillar davomida harakatsiz yotgan.", WordLevel.INTERMEDIATE));

        words.add(new Word("feasible", "[ˈfiːzəbl]", "Amalga oshirsa bo'ladigan, real", "Fizika",
                "Fizika qonunlariga to'la mos keladigan va real amalga oshirsa bo'ladigan texnologik reja.",
                "It is a feasible project that can be completed in two months.", "Bu ikki oy ichida amalga oshirsa bo'ladigan reja.", WordLevel.INTERMEDIATE));

        words.add(new Word("hostile", "[ˈhɒstaɪl]", "Dushmanona, adovatli", "Hostel / Qostil",
                "Arzon hostelga kirgan sayyohlarga xo'jayin dushmanona va tajovuzkor nigoh bilan qaradi.",
                "They found themselves in a hostile environment.", "Ular o'zlarini dushmanona muhitda ko'rishdi.", WordLevel.INTERMEDIATE));

        words.add(new Word("lenient", "[ˈliːniənt]", "Yumshoqko'ngil, rahmdil (jazolashda)", "Lenin / Lenta",
                "Qoidani buzgan o'quvchiga qattiq jazo bermasdan yumshoq muomala qilgan murabbiy.",
                "The judge was lenient because it was his first offense.", "Sudya birinchi marta bo'lgani uchun unga nisbatan yumshoq bo'ldi.", WordLevel.INTERMEDIATE));

        words.add(new Word("notorious", "[nəʊˈtɔːriəs]", "Yomon oti chiqqan, badnom", "Notarius / Motor",
                "Soxta motor va mashinalar savdosi tufayli butun shaharga yomon oti chiqqan qallob.",
                "The company is notorious for paying low salaries.", "Kompaniya kam maosh to'lashi bilan badnom bo'lgan.", WordLevel.INTERMEDIATE));

        words.add(new Word("reluctant", "[rɪˈlʌktənt]", "Istaksiz, xohlamay turgan", "Rul va lak",
                "Yangi bo'yalgan tirnog'ining laki buzilmasligi uchun mashina rulini istaksiz ushlagan qiz.",
                "He was reluctant to talk about his difficult childhood.", "U qiyin bolaligi haqida gapirishga istaksiz edi.", WordLevel.INTERMEDIATE));

        words.add(new Word("vulnerable", "[ˈvʌlnərəbl]", "Zaif, himoyasiz", "Vulqon va bola",
                "Otilayotgan qizg'in vulqon yonida hech qanday qalqonsiz qolgan zaif va himoyasiz odam.",
                "Old people are particularly vulnerable to the winter flu.", "Keksalar qishki shamollashga ayniqsa zaif bo'ladilar.", WordLevel.INTERMEDIATE));

        words.add(new Word("diligent", "[ˈdɪlɪdʒənt]", "Tirishqoq, mehnatsevar", "Dilshod / Diler",
                "Kechayu kunduz tinmay kitob o'qib IELTS dan 8.5 olgan mehnatsevar Dilshod.",
                "She is a diligent worker who always finishes tasks on time.", "U topshiriqlarni doim vaqtida bajaradigan mehnatsevar xodim.", WordLevel.INTERMEDIATE));

        words.add(new Word("gullible", "[ˈɡʌləbl]", "Tez ishonuvchan, soddadil", "Galuboy / Gul",
                "Kim nima desa darhol ishonib qoladigan, aldanib qolishi oson soddadil inson.",
                "Don't be so gullible, you shouldn't believe everything online.", "Bunchalik sodda bo'lmang, internetdagi hamma narsaga ishonmang.", WordLevel.INTERMEDIATE));

        words.add(new Word("obstinate", "[ˈɒbstɪnət]", "O'jar, qaysar, so'zida turib oluvchi", "Ob-stakan ustidagi eshak",
                "Ko'prik ustida to'xtab olib, hech kimning gapiga kirmay o'jarlik qilayotgan eshak.",
                "He can be very obstinate when he thinks he is right.", "O'zini haq deb bilganida u juda o'jar bo'lib qoladi.", WordLevel.INTERMEDIATE));

        words.add(new Word("adequate", "[ˈædɪkwət]", "Yetarli, munosib, qoniqarli", "Adolat / Kvatira",
                "Yangi ochilgan yotoqxonada talabalar uchun yetarli va qoniqarli sharoitlar yaratildi.",
                "The room was small but adequate for our needs.", "Xona kichik edi, lekin bizning ehtiyojimiz uchun yetarli edi.", WordLevel.INTERMEDIATE));

        words.add(new Word("concur", "[kənˈkɜːr]", "Fikriga qo'shilmoq, hamfikr bo'lmoq", "Konkurs / Konkret",
                "Hakamlar hay'atining barcha a'zolari birinchi o'rin g'olibiga bir ovozdan qo'shilishdi.",
                "I strongly concur with your opinion on this matter.", "Men bu masala bo'yicha sizning fikringizga to'liq qo'shilaman.", WordLevel.INTERMEDIATE));

        words.add(new Word("deviate", "[ˈdiːvieɪt]", "Chetlashmoq, yo'ldan chiqmoq", "Devor / Divan",
                "Katta magistral yo'ldan baland devor tomonga burilib chetlashib ketgan mashina.",
                "Do not deviate from the agreed plan.", "Kelishilgan rejadan chetlashmang.", WordLevel.INTERMEDIATE));

        words.add(new Word("exhibit", "[ɪɡˈzɪbɪt]", "Namoyish qilmoq, ko'rsatmoq", "Eksponat",
                "Qadimiy san'at muzeyida o'zining nodir rasmlarini xalqqa namoyish qilgan rassom.",
                "The gallery will exhibit paintings by local artists.", "Galereya mahalliy rassomlarning rasmlarini namoyish etadi.", WordLevel.INTERMEDIATE));

        words.add(new Word("genuine", "[ˈdʒenjuɪn]", "Haqiqiy, soxta bo'lmagan, asl", "Jenya / Janub",
                "Bozordagi soxta buyumlar orasidan topilgan haqiqiy va asl olmos uzuk.",
                "This bag is made of genuine Italian leather.", "Bu sumka haqiqiy italyan charmidan qilingan.", WordLevel.INTERMEDIATE));


        // =========================================================================
        // 🔴 YUQORI DARAJA (ADVANCED / IELTS: C1 - C2) — 20 TA SO'Z
        // =========================================================================
        words.add(new Word("resilient", "[rɪˈzɪliənt]", "Bardoshli, chidamli, qayishqoq", "Rezinka",
                "Rezinka kabi qanchalik cho'zilsa yoki bosilsa ham, darhol o'z holiga qaytadigan irodali va bardoshli inson.",
                "Children are remarkably resilient and adapt to changes quickly.", "Bolalar nihoyatda bardoshli bo'lib, o'zgarishlarga tez moslashadilar.", WordLevel.ADVANCED));

        words.add(new Word("subtle", "[ˈsʌtl]", "Nozik, sezilarsiz", "Sopol / Sotil",
                "Qadimiy sopol ko'zadagi naqshlar shunchalik nozikki, faqat diqqat bilan qaraganda seziladi.",
                "There is a subtle difference between these two colors.", "Bu ikki rang o'rtasida nozik (sezilarsiz) farq bor.", WordLevel.ADVANCED));

        words.add(new Word("lucrative", "[ˈluːkrətɪv]", "Juda foydali, mo'may daromadli", "Luk (Piyoz)",
                "Bahorda katta yerga luk (piyoz) ekib eksport qilgan fermer mo'may daromadli foyda oldi.",
                "Investing in real estate turned out to be a lucrative business.", "Ko'chmas mulkka sarmoya kiritish juda foydali biznes bo'lib chiqdi.", WordLevel.ADVANCED));

        words.add(new Word("meticulous", "[məˈtɪkjələs]", "Sinchkov, o'ta ehtiyotkor", "Matematik",
                "Matematik olim har bir formulani mikroskopdek sinchkovlik bilan tekshirib chiqdi.",
                "The architect was meticulous in every detail of the design.", "Arxitektor loyihaning har bir detalida o'ta sinchkov edi.", WordLevel.ADVANCED));

        words.add(new Word("zealous", "[ˈzeləs]", "G'ayratli, fidoyi, intiluvchan", "Zelen (Ko'kat)",
                "Bahoriy barra zelen/ko'katlarni yeb kuchga to'lgan, o'z maqsadiga erishish uchun tinmay harakat qiladigan g'ayratli xodim.",
                "He was a zealous supporter of environmental protection.", "U atrof-muhitni muhofaza qilishning g'ayratli tarafdori edi.", WordLevel.ADVANCED));

        words.add(new Word("benevolent", "[bəˈnevələnt]", "Saxiy, mehribon, xayrixoh", "Bilet bepul",
                "Muhtojlarga konsert biletlarini bepul tarqatuvchi saxiy va mehribon homiy.",
                "The benevolent gentleman donated millions to the orphanage.", "Saxiy janob bolalar uyiga millionlab pul xayriya qildi.", WordLevel.ADVANCED));

        words.add(new Word("alleviate", "[əˈliːvieɪt]", "Yengillashtirmoq (og'riqni)", "Ali va vata",
                "Ali yaralangan do'stining og'riyotgan joyiga dorili paxta/vata qo'yib, uning og'rig'ini yengillashtirdi.",
                "The doctor prescribed pills to alleviate the severe headache.", "Shifokor kuchli bosh og'rig'ini yengillashtirish uchun dori yozdi.", WordLevel.ADVANCED));

        words.add(new Word("versatile", "[ˈvɜːsətaɪl]", "Ko'p qirrali, har tomonlama moslashuvchan", "Versal",
                "Versal saroyi kabi har qanday vaziyatga mos tushadigan, barcha sohada qobiliyatli inson.",
                "Smartphone is a versatile tool for communication and learning.", "Smartfon aloqa va o'rganish uchun ko'p qirrali vositadir.", WordLevel.ADVANCED));

        words.add(new Word("scrutinize", "[ˈskruːtɪnaɪz]", "Sinchiklab tekshirmoq", "Skripka / Skrutka",
                "Eski skripka ustasi har bir yog'och detalni lupa bilan sinchiklab tekshirib chiqdi.",
                "The customs officers will scrutinize all passports carefully.", "Bojxona xodimlari barcha pasportlarni sinchiklab tekshiradilar.", WordLevel.ADVANCED));

        words.add(new Word("gregarious", "[ɡrɪˈɡeəriəs]", "Kirishimli, do'stsevar", "Guruh",
                "Yolg'iz qolishni yoqtirmaydigan, doimo katta guruhlar orasida yuradigan kirishimli yoshlar.",
                "Being gregarious, he made friends very easily in the new school.", "Kirishimli bo'lgani uchun, u juda tez do'stlar orttirdi.", WordLevel.ADVANCED));

        words.add(new Word("opulent", "[ˈɒpjələnt]", "Hashamatli, o'ta boy", "Opal toshlari / Pul",
                "Xonalariga qimmatbaho opal toshlari qadalgan va oltin bilan bezatilgan hashamatli qasr.",
                "The king lived in an opulent palace surrounded by gold.", "Qirol oltinlar bilan o'ralgan hashamatli saroyda yashardi.", WordLevel.ADVANCED));

        words.add(new Word("coerce", "[kəʊˈɜːs]", "Majburlamoq, zo'rlamoq", "Ko'r ko'rsatkich",
                "Ko'ziga qora mato bog'lab, qog'ozga imzo chekishga majburlashdi.",
                "You cannot coerce someone into loving you.", "Siz kimnidir sizni sevishga majburlay olmaysiz.", WordLevel.ADVANCED));

        words.add(new Word("plausible", "[ˈplɔːzəbl]", "Haqiqatga yaqin, ishonarli", "Plastilin",
                "Plastilindek shakl berilgan, eshitganda haqiqatga yaqin va ishonarli tuyuladigan bahona.",
                "His excuse sounded plausible, so the teacher believed him.", "Uning bahonasi ishonarli eshitildi, shuning uchun ustoz ishondi.", WordLevel.ADVANCED));

        words.add(new Word("superficial", "[ˌsuːpəˈfɪʃl]", "Yuzaki, sayoz", "Super fish (Baliq)",
                "Okeanning chuquriga kirmay, faqat suvning eng ustki yuzasida suzuvchi sayoz baliq.",
                "He only has a superficial knowledge of French.", "U fransuz tilini faqat yuzaki darajada biladi.", WordLevel.ADVANCED));

        words.add(new Word("tenacious", "[təˈneɪʃəs]", "Tirishqoq, mahkam yopishuvchi", "Tennischi",
                "Mag'lubiyatga tan bermay oxirgi to'pgacha tirishqoqlik bilan kurashuvchi mahoratli tennischi.",
                "She is a tenacious fighter who never gives up on her dreams.", "U orzulari yo'lida hech qachon taslim bo'lmaydigan tirishqoq kurashchi.", WordLevel.ADVANCED));

        words.add(new Word("abundant", "[əˈbʌndənt]", "Mo'l-ko'l, serob", "Obod / Banan",
                "Obod qilingan yangi bog'da minglab banan va mevalar mo'l-ko'l bo'lib pishdi.",
                "The region has an abundant supply of fresh water.", "Bu hududda toza ichimlik suvi mo'l-ko'ldir.", WordLevel.ADVANCED));

        words.add(new Word("belligerent", "[bəˈlɪdʒərənt]", "Urushqoq, tajovuzkor", "Bilyardchi",
                "Bilyard o'yinida yutqazib qo'yib, har bir o'yinchiga urushqoqlik qilgan tajovuzkor odam.",
                "His belligerent attitude made him unpopular with colleagues.", "Uning urushqoq munosabati tufayli hamkasblari uni yoqtirishmasdi.", WordLevel.ADVANCED));

        words.add(new Word("clandestine", "[klænˈdestɪn]", "Maxfiy, yashirin", "Klanning dasti",
                "Maxfiy klanning dasti bilan tunda yashirin uchrashuv tashkil qilindi.",
                "They held a clandestine meeting to discuss the secret plan.", "Ular maxfiy rejani muhokama qilish uchun yashirin uchrashuv o'tkazdilar.", WordLevel.ADVANCED));

        words.add(new Word("eloquent", "[ˈeləkwənt]", "Notiq, shirinso'z, fasohatli", "Elektron / Qalam",
                "Minbarga chiqib butun xalqni o'zining shirin va fasohatli nutqi bilan lol qoldirgan notiq.",
                "His eloquent speech moved many in the audience to tears.", "Uning notiqlik bilan aytgan nutqi ko'pchilikni ko'z yoshiga soldi.", WordLevel.ADVANCED));

        words.add(new Word("ubiquitous", "[juːˈbɪkwɪtəs]", "Har yerda hoziru-nozir, hamma joyda uchraydigan", "USB / Yubiley",
                "Bugungi kunda smartfonlar har bir insonning qo'lida hamma joyda uchraydigan buyumga aylandi.",
                "Smartphones have become ubiquitous in modern everyday life.", "Smartfonlar zamonaviy hayotda hamma joyda uchraydigan bo'lib qoldi.", WordLevel.ADVANCED));
    }

    public List<Word> getAllWords() {
        return Collections.unmodifiableList(words);
    }

    public int getTotalDays() {
        return (int) Math.ceil((double) words.size() / WORDS_PER_DAY);
    }

    public List<Word> getWordsByLevel(WordLevel level) {
        if (level == null) return getAllWords();
        return words.stream()
                .filter(w -> w.getLevel() == level)
                .collect(Collectors.toList());
    }

    /**
     * Foydalanuvchining tanlagan darajasiga mos 20 ta so'zni qaytaradi
     */
    public List<Word> getWordsForDayAndLevel(int dayIndex, WordLevel level) {
        List<Word> levelWords = getWordsByLevel(level);
        if (levelWords.isEmpty()) {
            levelWords = getAllWords();
        }

        int totalDays = (int) Math.ceil((double) levelWords.size() / WORDS_PER_DAY);
        if (totalDays == 0) totalDays = 1;

        int normalizedDay = ((dayIndex - 1) % totalDays);
        int startIndex = normalizedDay * WORDS_PER_DAY;
        int endIndex = Math.min(startIndex + WORDS_PER_DAY, levelWords.size());

        if (startIndex >= levelWords.size()) {
            return new ArrayList<>(levelWords.subList(0, Math.min(WORDS_PER_DAY, levelWords.size())));
        }

        return new ArrayList<>(levelWords.subList(startIndex, endIndex));
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
