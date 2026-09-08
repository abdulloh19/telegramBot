package com.mnemonic.repository;

import com.mnemonic.model.TargetLanguage;
import com.mnemonic.model.Word;
import com.mnemonic.model.WordLevel;

import java.util.*;
import java.util.stream.Collectors;

public class WordRepository {
    private final List<Word> words = new ArrayList<>();
    private final Random random = new Random();
    public static final int WORDS_PER_DAY = 20;

    public WordRepository() {
        initEnglishWords();
        initRussianWords();
    }

    private void initEnglishWords() {
        // =========================================================================
        // 🟢 BOSHLANG'ICH DARAJA (BEGINNER: A1 - A2) — 20 TA INGLIZCHA SO'Z
        // =========================================================================
        words.add(new Word("abandon", "[əˈbændən]", "Tashlab ketmoq, tark etmoq", "A-bandomiz!",
                "Kemada ketayotgan qaroqchilar kema cho'kayotganini ko'rib, 'A, bandomiz!' deb qichqirib kemani tashlab qochishdi.",
                "They had to abandon their car in the heavy snow.", "Ular qalin qorda mashinalarini tashlab ketishga majbur bo'lishdi.", WordLevel.BEGINNER, TargetLanguage.ENGLISH));

        words.add(new Word("curious", "[ˈkjʊəriəs]", "Qiziquvchan, sinchkov", "Kuryer",
                "Har safar kuryer eshik qoqqanida, qutida nima borligini bilishga oshiqadigan o'ta qiziquvchan mushuk yugurib keladi.",
                "Cats are naturally curious animals.", "Mushuklar tabiatan qiziquvchan hayvonlardir.", WordLevel.BEGINNER, TargetLanguage.ENGLISH));

        words.add(new Word("drowsy", "[ˈdraʊzi]", "Uyqusiragan, mudragan", "Dori",
                "Kasal bo'lib kuchli tinchlantiruvchi dori ichgach, ko'zlari suzilib uyqusirab qoldi.",
                "The medication made him feel very drowsy.", "Dori uni juda uyqusiratib qo'ydi.", WordLevel.BEGINNER, TargetLanguage.ENGLISH));

        words.add(new Word("hesitate", "[ˈhezɪteɪt]", "Ikkilanmoq, taraddudlanmoq", "Hech aytolmaslik",
                "Sinf oldida turib to'g'ri javobni 'hech aytolmay' ikkilanib turgan o'quvchi.",
                "Do not hesitate to ask questions if you need help.", "Agar yordam kerak bo'lsa, savol berishga ikkilanmang.", WordLevel.BEGINNER, TargetLanguage.ENGLISH));

        words.add(new Word("fragile", "[ˈfrædʒaɪl]", "Mo'rt, tez sinuvchan", "Frajer / Freza",
                "Pochtadan kelgan qutiga 'Fragile' yozilgan, chunki ichidagi billur vaza juda nozik va tez sinadi.",
                "Be careful with that box, the glasses inside are fragile.", "Bu qutini ehtiyot qiling, ichidagi stakanlar mo'rt.", WordLevel.BEGINNER, TargetLanguage.ENGLISH));

        words.add(new Word("novice", "[ˈnɒvɪs]", "Yangi boshlovchi, havaskor", "Novvoy shogird",
                "Novvoyxonada non yopishni endigina o'rganayotgan yangi boshlovchi shogird.",
                "He is still a novice in computer programming.", "U dasturlashda hali yangi boshlovchi.", WordLevel.BEGINNER, TargetLanguage.ENGLISH));

        words.add(new Word("quench", "[kwentʃ]", "Qondirmoq (chanqoqni)", "Qaynoq choy / Kventin",
                "Jazirama issiqda muzdek ko'k choy ichib o'z chanqog'ini qondirdi.",
                "A glass of cold water will quench your thirst.", "Bir stakan sovuq suv chanqog'ingizni qondiradi.", WordLevel.BEGINNER, TargetLanguage.ENGLISH));

        words.add(new Word("obstacle", "[ˈɒbstəkl]", "To'siq, g'ov", "Ob-stakan (Katta stakan)",
                "Yugurish yo'lakchasida bahaybat stakan to'siq bo'lib turibdi va sportchi undan sakrab o'tdi.",
                "Fear is the biggest obstacle to success.", "Qo'rquv — muvaffaqiyat yo'lidagi eng katta to'siqdir.", WordLevel.BEGINNER, TargetLanguage.ENGLISH));

        words.add(new Word("marvellous", "[ˈmɑːvələs]", "Ajoyib, hayratlanarli", "Marvel",
                "Marvel kinolaridagi kabi hayratlanarli va ajoyib sehrli manzara.",
                "We had a marvellous time at the beach yesterday.", "Biz kecha plyajda ajoyib vaqt o'tkazdik.", WordLevel.BEGINNER, TargetLanguage.ENGLISH));

        words.add(new Word("candid", "[ˈkændɪd]", "Samimiy, ochiqko'ngil", "Kand / Konfet",
                "Do'stlariga shirin konfet ulashib, ko'nglidagi bor gapni ochiq va samimiy aytadigan inson.",
                "He gave a candid interview about his past mistakes.", "U o'tmishdagi xatolari haqida samimiy intervyu berdi.", WordLevel.BEGINNER, TargetLanguage.ENGLISH));

        words.add(new Word("frugal", "[ˈfruːɡl]", "Tejamkor, tejab sarflaydigan", "Frukta (Arzon meva)",
                "Bozordan faqat arzon frukta/meva sotib olib, har bir tiyinini tejaydigan odam.",
                "They lived a very frugal life to save for a new house.", "Ular yangi uyga pul yig'ish uchun juda tejamkor hayot kechirishdi.", WordLevel.BEGINNER, TargetLanguage.ENGLISH));

        words.add(new Word("carnivore", "[ˈkɑːnɪvɔːr]", "Go'shtxo'r hayvon", "Qorinda bor",
                "Go'shtxo'r sherning qornida faqat go'sht bor, u aslo o't yemaydi.",
                "Lions and tigers are examples of carnivores.", "Sherlar va yo'lbarslar go'shtxo'rlarga misoldir.", WordLevel.BEGINNER, TargetLanguage.ENGLISH));

        words.add(new Word("lucid", "[ˈluːsɪd]", "Ravshan, tiniq, tushunarli", "Lyustra",
                "Zaldagi yangi kristall lyustra yonganida butun xona tiniq va ravshan bo'lib ketdi.",
                "The professor gave a lucid explanation of the complex theory.", "Professor murakkab nazariyani ravshan va tushunarli tushuntirdi.", WordLevel.BEGINNER, TargetLanguage.ENGLISH));

        words.add(new Word("ancient", "[ˈeɪnʃənt]", "Qadimiy, ko'hna", "Anjir / Eshon",
                "Qadimiy eshonlar bog'idagi 500 yillik anjir daraxti ostida suhbatlashishdi.",
                "Rome is famous for its ancient monuments.", "Rim o'zining qadimiy yodgorliklari bilan mashhur.", WordLevel.BEGINNER, TargetLanguage.ENGLISH));

        words.add(new Word("brave", "[breɪv]", "Jasur, qo'rqmas, botir", "Beret / Breker",
                "Qizil beret kiyib daryoga cho'kayotgan bolani qutqarib qolgan jasur yigit.",
                "The brave firefighter rescued the cat from the fire.", "Jasur o't o'chiruvchi mushukni olovdan qutqardi.", WordLevel.BEGINNER, TargetLanguage.ENGLISH));

        words.add(new Word("calm", "[kɑːm]", "Xotirjam, tinch, osoyishta", "Qalam / Komil",
                "Qo'liga qalam olib, shovqin-suron orasida ham xotirjam rasm chizib o'tirgan Komil.",
                "Try to stay calm during the exam.", "Imtihon paytida xotirjam bo'lishga harakat qiling.", WordLevel.BEGINNER, TargetLanguage.ENGLISH));

        words.add(new Word("eager", "[ˈiːɡər]", "Ishtiyoqmand, chanqoq", "Igor / O'g'ri",
                "Yangi chet tilini o'rganishga o'ta ishtiyoqmand talaba har kuni 3 soat shug'ullanadi.",
                "The students were eager to start the new project.", "Talabalar yangi loyihani boshlashga ishtiyoqmand edilar.", WordLevel.BEGINNER, TargetLanguage.ENGLISH));

        words.add(new Word("fierce", "[fɪəs]", "Shiddatli, yirtqich, quturgan", "Fursat / Fikr",
                "O'rmonda kutilmaganda shiddatli va yirtqich qora bo'riga duch kelishdi.",
                "A fierce storm destroyed several houses in the village.", "Shiddatli bo'ron qishloqdagi bir nechta uylarni vayron qildi.", WordLevel.BEGINNER, TargetLanguage.ENGLISH));

        words.add(new Word("generous", "[ˈdʒenərəs]", "Saxiy, qo'li ochiq", "General",
                "O'zining barcha maoshini muhtojlarga ulashgan saxiy general.",
                "It was generous of you to pay for dinner.", "Kechki ovqat uchun to'laganingiz juda saxiy ish bo'ldi.", WordLevel.BEGINNER, TargetLanguage.ENGLISH));

        words.add(new Word("honest", "[ˈɒnɪst]", "Rostgo'y, to'g'riso'z, halol", "Onasi",
                "Onasiga hech qachon yolg'on gapirmaydigan, doim haqiqatni aytadigan rostgo'y bola.",
                "He gave an honest answer to the difficult question.", "U qiyin savolga rostgo'y javob berdi.", WordLevel.BEGINNER, TargetLanguage.ENGLISH));

        // =========================================================================
        // 🟡 O'RTA DARAJA (INTERMEDIATE: B1 - B2) — INGLIZCHA
        // =========================================================================
        words.add(new Word("ambitious", "[æmˈbɪʃəs]", "Shijoatli, intiluvchan", "Ambitsiya",
                "Katta ambitsiyasi bor yosh dasturchi xalqaro startap ochishga bel bog'ladi.",
                "She has ambitious plans for her business career.", "Uning biznes faoliyati uchun katta intilishlari bor.", WordLevel.INTERMEDIATE, TargetLanguage.ENGLISH));

        words.add(new Word("pragmatic", "[præɡˈmætɪk]", "Amaliy, ishbilarmon", "Praktika",
                "Faqat quruq nazariya emas, amaliy (praktik) yechimlarni tanlaydigan usta muhandis.",
                "We need to adopt a pragmatic approach to this problem.", "Biz bu muammoga amaliy yondashuvni qo'llashimiz kerak.", WordLevel.INTERMEDIATE, TargetLanguage.ENGLISH));

        words.add(new Word("feasible", "[ˈfiːzəbl]", "Amalga oshirsa bo'ladigan, real", "Fizika / Fazo",
                "Fazoga uchish ilgari xomxayol edi, ammo ilm-fan uni amalga oshadigan qildi.",
                "The project is ambitious but financially feasible.", "Loyiha shijoatli, ammo moliyaviy jihatdan amalga oshirish mumkin.", WordLevel.INTERMEDIATE, TargetLanguage.ENGLISH));

        words.add(new Word("deter", "[dɪˈtɜːr]", "To'xtatmoq, qaytarmoq", "Daftar / Detektor",
                "Xavfsizlik detektori o'g'rini do'kondan qimmatbaho buyumlarni o'g'irlashdan to'xtatib qoldi.",
                "High prices will deter customers from buying new cars.", "Qimmat narxlar xaridorlarni yangi mashina sotib olishdan to'xtatadi.", WordLevel.INTERMEDIATE, TargetLanguage.ENGLISH));

        words.add(new Word("diligent", "[ˈdɪlɪdʒənt]", "Tirishqoq, mehnatsevar", "Dilkash agent",
                "Har doim ishini sidqidildan, tirishqoqlik bilan bajaradigan dilkash agent.",
                "Leo is very diligent in his English studies.", "Leo ingliz tili o'rganishda juda tirishqoq.", WordLevel.INTERMEDIATE, TargetLanguage.ENGLISH));

        words.add(new Word("elated", "[iˈleɪtɪd]", "Boshy osmonda, benihoya xursand", "Elita / Lenta",
                "Musobaqa marra lentasini birinchi bo'lib kesib o'tgan yuguruvchi quvonchdan boshi osmonda edi.",
                "She was elated when she passed the exam with top marks.", "Imtihondan eng yuqori ball bilan o'tganida uning quvonchi cheksiz edi.", WordLevel.INTERMEDIATE, TargetLanguage.ENGLISH));

        words.add(new Word("lenient", "[ˈliːniənt]", "Yumshoq ko'ngil, rahmdil", "Liniya / Lenin",
                "O'quvchilarining kichik xatolarini kechirib, ularga juda yumshoq munosabatda bo'lgan o'qituvchi.",
                "The judge was lenient towards the young offender.", "Sudya yosh huquqbuzarga nisbatan rahmdillik qildi.", WordLevel.INTERMEDIATE, TargetLanguage.ENGLISH));

        words.add(new Word("vulnerable", "[ˈvʌlnərəbl]", "Zaif, himoyasiz, nozik", "Volan / Valera",
                "Katta bo'ronda qalin himoya devorisiz qolgan qishloq uylari juda zaif va himoyasiz edi.",
                "Small businesses are vulnerable during an economic crisis.", "Kichik bizneslar iqtisodiy inqiroz paytida zaif bo'lib qoladilar.", WordLevel.INTERMEDIATE, TargetLanguage.ENGLISH));

        words.add(new Word("reluctant", "[rɪˈlʌktənt]", "Istaksiz, ko'ngilsiz", "Rele / Lak",
                "Bozorga borishni istamay, erinib, istaksizgina oyog'ini sudrab yurgan bola.",
                "He was reluctant to discuss his personal feelings.", "U shaxsiy tuyg'ulari haqida gapirishga istaksiz edi.", WordLevel.INTERMEDIATE, TargetLanguage.ENGLISH));

        // =========================================================================
        // 🔴 YUQORI DARAJA (ADVANCED: C1 - C2 / IELTS) — INGLIZCHA
        // =========================================================================
        words.add(new Word("ubiquitous", "[juːˈbɪkwɪtəs]", "Hamma yerda hoziru nozir, keng tarqalgan", "Yubka / Yulduz",
                "Bugungi kunda smartfonlar xuddi havodek hamma yerda bor va keng tarqalgan.",
                "Smartphones have become ubiquitous in modern society.", "Zamonaviy jamiyatda smartfonlar hamma joyda uchraydigan bo'lib qoldi.", WordLevel.ADVANCED, TargetLanguage.ENGLISH));

        words.add(new Word("ephemeral", "[ɪˈfemərəl]", "O'tkinchi, qisqa muddatli", "Efir / Fermer",
                "Efirga uzatilgan chaqmoqdek yorqin, ammo bir zumda yo'qoladigan o'tkinchi kamalak.",
                "Fame in the social media era is often ephemeral.", "Ijtimoiy tarmoqlar asrida shon-shuhrat ko'pincha o'tkinchi bo'ladi.", WordLevel.ADVANCED, TargetLanguage.ENGLISH));

        words.add(new Word("meticulous", "[məˈtɪkjələs]", "O'ta sinchkov, mayda-chuydasigacha e'tiborli", "Metr / Qalam",
                "Har bir santimetrni sinchkovlik bilan o'lchab, mayda nuqsonlarni ham qoldirmaydigan zargar.",
                "The scientist kept meticulous records of every experiment.", "Olim har bir tajribaning o'ta sinchkovlik bilan hisobini yuritdi.", WordLevel.ADVANCED, TargetLanguage.ENGLISH));

        words.add(new Word("resilient", "[rɪˈzɪliənt]", "Chidamli, egiluvchan, qayta tiklanuvchan", "Rezinka",
                "Rezinkadek qattiq cho'zilsa ham uzilmay, darhol o'z holiga qaytadigan chidamli va matonatli inson.",
                "Local communities proved resilient after the earthquake.", "Mahalliy aholi zilziladan so'ng chidamli va matonatli ekanligini isbotladi.", WordLevel.ADVANCED, TargetLanguage.ENGLISH));

        words.add(new Word("pristine", "[ˈprɪstiːn]", "Bokira, toza, qo'l tegilmagan", "Pristan / Pristav",
                "Orollardagi odam oyog'i yetmagan, tiniq va musaffo bokira plyajlar.",
                "The divers explored pristine coral reefs in the ocean.", "G'avvoslar okeandagi bokira va qo'l tegilmagan marjon qoyalarini o'rgandilar.", WordLevel.ADVANCED, TargetLanguage.ENGLISH));

        words.add(new Word("eloquent", "[ˈeləkwənt]", "Notiq, so'zga usta, fasohatli", "Elektron / Kvant",
                "Sahnaga chiqib tinglovchilarni o'zining fasohatli nutqi bilan sehrlab qo'ygan so'zga usta notiq.",
                "She delivered an eloquent speech at the international summit.", "U xalqaro sammitda notiqlarcha ta'sirli nutq so'zladi.", WordLevel.ADVANCED, TargetLanguage.ENGLISH));

        words.add(new Word("tenacious", "[təˈneɪʃəs]", "Qat'iyatli, yengilmas, mahkam tutuvchi", "Tennischi",
                "Qiyin vaziyatda ham taslim bo'lmay, g'alaba sari intiladigan qat'iyatli professional tennischi.",
                "A tenacious defense attorney never gives up easily.", "Qat'iyatli advokat hech qachon osonlikcha taslim bo'lmaydi.", WordLevel.ADVANCED, TargetLanguage.ENGLISH));
    }

    private void initRussianWords() {
        // =========================================================================
        // 🟢 BOSHLANG'ICH DARAJA (BEGINNER: A1 - A2) — 20 TA RUSCHA SO'Z (1-KUNLIK TO'LIQ DARS)
        // =========================================================================
        words.add(new Word("Спасибо", "[спасúбо]", "Rahmat, tashakkur", "Spas / Qutqaruvchi",
                "Cho'kayotgan bolani qutqargan qutqaruvchiga (spasatel) hamma bir ovozdan 'Spasibo — Rahmat!' deb minnatdorchilik bildirdi.",
                "Большое спасибо за вашу тёплую помощь.", "Samimiy yordamingiz uchun katta rahmat.", WordLevel.BEGINNER, TargetLanguage.RUSSIAN));

        words.add(new Word("Вдруг", "[вдруг]", "To'satdan, kutilmaganda, birdaniga", "Va do'st (v drug)",
                "Ko'chada ketayotganimda, to'satdan va kutilmaganda eski do'stim (v drug) qarshimdan chiqib qoldi.",
                "Вдруг пошёл сильный дождь, и все побежали.", "To'satdan kuchli yomg'ir yog'ib yubordi va hamma qochdi.", WordLevel.BEGINNER, TargetLanguage.RUSSIAN));

        words.add(new Word("Мечта", "[мечтá]", "Orzu, armon", "Mech (Qilich)",
                "Qadimiy ritsar afsonaviy o'tkir qilichni (mech) qo'lga kiritishni bolaligidan orzu qilardi.",
                "Его главная мечта — стать известным врачом.", "Uning asosiy orzusi — taniqli shifokor bo'lish.", WordLevel.BEGINNER, TargetLanguage.RUSSIAN));

        words.add(new Word("Победа", "[побéда]", "G'alaba, zafar", "Pobeda (mashina/kema)",
                "Pobeda nomli tezyurar mashinadagi sportchilarimiz final poygasida buyuk g'alabani qo'lga kiritishdi.",
                "Наша команда одержала блестящую победу.", "Bizning jamoamiz ajoyib g'alabaga erishdi.", WordLevel.BEGINNER, TargetLanguage.RUSSIAN));

        words.add(new Word("Погода", "[погóда]", "Ob-havo", "Pa-goda (Yil davomida)",
                "Bugun shunday musaffo ob-havo bo'ldiki, bir yilda (v god) bir marta shunday bo'ladi.",
                "Завтра будет тёплая и солнечная погода.", "Ertaga iliq va quyoshli ob-havo bo'ladi.", WordLevel.BEGINNER, TargetLanguage.RUSSIAN));

        words.add(new Word("Осторожно", "[осторóжно]", "Ehtiyotkorlik bilan, ohista", "Storoj (Qorovul)",
                "Muzlagan ko'prikda qorovul (storoj) turib: 'Ehtiyotkorlik bilan yuring!' deb ogohlantirdi.",
                "Держите эту чашку осторожно, она горячая.", "Bu piyolani ehtiyotkorlik bilan ushlang, u issiq.", WordLevel.BEGINNER, TargetLanguage.RUSSIAN));

        words.add(new Word("Улыбка", "[улӹбка]", "Tabassum, kulgi", "Ulib / O'lib qolayozdi",
                "Uning yuzidagi beg'ubor tabassumni ko'rib, barcha bolalar quvonchdan xursand bo'lishdi.",
                "Её добрая улыбка подняла всем настроение.", "Uning samimiy tabassumi barchaning kayfiyatini ko'tardi.", WordLevel.BEGINNER, TargetLanguage.RUSSIAN));

        words.add(new Word("Дружба", "[дрýжба]", "Do'stlik, o'rtoqlik", "Drujina (Askarlar guruhi)",
                "Qadimgi drujina askarlari o'rtasidagi haqiqiy va sadoqatli do'stlik har qanday sinovdan o'tgan.",
                "Настоящая дружба проверяется временем и трудностями.", "Haqiqiy do'stlik vaqt va qiyinchiliklar bilan sinovdan o'tadi.", WordLevel.BEGINNER, TargetLanguage.RUSSIAN));

        words.add(new Word("Помощь", "[пóмощь]", "Yordam, ko'mak", "Po-moshch (Kuch-quvvat)",
                "Katta kuch-quvvatga (moshch) ega pahlavon qiyin ahvolda qolgan qariyaga yordam qo'lini cho'zdi.",
                "Мне срочно нужна ваша профессиональная помощь.", "Menga zudlik bilan sizning professional yordamingiz kerak.", WordLevel.BEGINNER, TargetLanguage.RUSSIAN));

        words.add(new Word("Сказка", "[скáзка]", "Ertak, afsona", "Skazka / Ochki-skaska",
                "Buvijonim ko'zoynak taqib, sham yorug'ida bizga sehrli ertak o'qib berar edilar.",
                "Эта добрая сказка учит детей честности.", "Ushbu samimiy ertak bolalarni halollikka o'rgatadi.", WordLevel.BEGINNER, TargetLanguage.RUSSIAN));

        words.add(new Word("Время", "[врéмя]", "Vaqt, soat", "Vremya / O'ram-ip",
                "Vaqt xuddi g'altakdagi yupqa ipdek tez va to'xtovsiz aylanib o'tib ketadi.",
                "У нас осталось совсем мало свободного времени.", "Bizda juda oz bo'sh vaqt qoldi.", WordLevel.BEGINNER, TargetLanguage.RUSSIAN));

        words.add(new Word("Надежда", "[надéжда]", "Umid, ishonch", "Nadejda / Kiyim (Odejda)",
                "Yangi oppoq bayramona kiyim (odejda) kiyib, imtihondan a'lo o'tishga katta umid bog'ladi.",
                "У нас есть твёрдая надежда на успех проекта.", "Bizda loyihaning muvaffaqiyatiga qat'iy umid bor.", WordLevel.BEGINNER, TargetLanguage.RUSSIAN));

        words.add(new Word("Счастье", "[счáстье]", "Baxt, saodat", "Schast / Qism (chast)",
                "Oila davrasida o'tkazilgan har bir shirin lahza — inson baxtining eng muhim qismidir.",
                "Семья и здоровье — это настоящее человеческое счастье.", "Oila va salomatlik — bu insonning haqiqiy baxtidir.", WordLevel.BEGINNER, TargetLanguage.RUSSIAN));

        words.add(new Word("Путешествие", "[путешéствие]", "Sayohat, safar", "Put (Yo'l) + Shestvie (Qadam)",
                "Katta yo'lga (put) qadam qo'yib, tog'lar va dengizlar bo'ylab unutilmas sayohat qildik.",
                "Путешествие в Самарканд оставило незабываемые впечатления.", "Samarqandga sayohat unutilmas taassurotlar qoldirdi.", WordLevel.BEGINNER, TargetLanguage.RUSSIAN));

        words.add(new Word("Внимание", "[внимáние]", "Diqqat, e'tibor", "Vnimat / Tinglamoq",
                "O'qituvchining so'zlariga butun diqqat va e'tibor bilan quloq tutgan a'lochi o'quvchi.",
                "Обратите особое внимание на эти правила.", "Ushbu qoidalarga alohida diqqat qarating.", WordLevel.BEGINNER, TargetLanguage.RUSSIAN));

        words.add(new Word("Правда", "[прáвда]", "Haqiqat, rost gap", "Pravo (Huquq, adolat)",
                "Har bir inson adolatli huquqqa (pravo) ega va haqiqat baribir g'alaba qozonadi.",
                "Всегда говори чистую правду, даже если это трудно.", "Doimo toza haqiqatni gapiring, hatto bu qiyin bo'lsa ham.", WordLevel.BEGINNER, TargetLanguage.RUSSIAN));

        words.add(new Word("Спокойствие", "[спокóйствие]", "Xotirjamlik, osoyishtalik", "Spokoy / Tinch suv",
                "Tog' daryosi bo'yida o'tirib, qalbida cheksiz xotirjamlik va sokinlikni his qildi.",
                "Сохраняйте спокойствие в любых сложных ситуациях.", "Har qanday murakkab vaziyatda ham xotirjamlikni saqlang.", WordLevel.BEGINNER, TargetLanguage.RUSSIAN));

        words.add(new Word("Успех", "[успéх]", "Muvaffaqiyat, yutuq", "Uspet (Ulgurmoq)",
                "Barcha darslarini o'z vaqtida bajarishga ulgurgan (uspet) tirishqoq talaba katta muvaffaqiyatga erishdi.",
                "Упорный труд непременно приведёт вас к успеху.", "Tirishqoq mehnat sizni albatta muvaffaqiyatga olib keladi.", WordLevel.BEGINNER, TargetLanguage.RUSSIAN));

        words.add(new Word("Завтра", "[зáвтра]", "Ertaga", "Zavtrak (Nonushta)",
                "Ertaga ertalab birgalikda mazali nonushta (zavtrak) qilib, yangi muzeyga boramiz.",
                "Завтра мы начнём совершенно новый урок.", "Ertaga biz mutlaqo yangi darsni boshlaymiz.", WordLevel.BEGINNER, TargetLanguage.RUSSIAN));

        words.add(new Word("Здравствуйте", "[здрáвствуйте]", "Assalomu alaykum, salom", "Zdorovie (Salomatlik)",
                "Keksalarga mustahkam salomatlik (zdorovie) tilab: 'Zdravstvuyte — Assalomu alaykum!' deb ta'zim qildi.",
                "Здравствуйте, дорогие друзья и коллеги!", "Assalomu alaykum, qadrli do'stlar va hamkasblar!", WordLevel.BEGINNER, TargetLanguage.RUSSIAN));

        // =========================================================================
        // 🟡 O'RTA DARAJA (INTERMEDIATE: B1 - B2) — RUSCHA
        // =========================================================================
        words.add(new Word("Препятствие", "[препÿтствие]", "To'siq, g'ov, to'g'anoq", "Pyat / To'xtab qolmoq",
                "Yo'lda katta tosh to'siq bo'lib chiqdi, ammo qat'iyatli sayyoh uni aylanib o'tdi.",
                "Никакое препятствие не остановит нашу команду.", "Hech qanday to'siq jamoamizni to'xtata olmaydi.", WordLevel.INTERMEDIATE, TargetLanguage.RUSSIAN));

        words.add(new Word("Вдохновение", "[вдохновéние]", "Ilhom, zavq", "Vdox (Nafas olmoq)",
                "Tog' cho'qqisida chuqur nafas (vdox) olgan musavvir yangi asari uchun cheksiz ilhom topdi.",
                "Природа дарит поэтам неиссякаемое вдохновение.", "Tabiat shoirlarga tuganmas ilhom baxsh etadi.", WordLevel.INTERMEDIATE, TargetLanguage.RUSSIAN));

        words.add(new Word("Решительность", "[решúтельность]", "Qat'iyat, dadillik", "Reshit (Hal qilmoq)",
                "Murakkab muammoni tezda hal qilish (reshit) uchun unga mustahkam qat'iyat va dadillik kerak bo'ldi.",
                "Его решительность помогла спасти весь проект.", "Uning qat'iyati butun loyihani qutqarishga yordam berdi.", WordLevel.INTERMEDIATE, TargetLanguage.RUSSIAN));

        words.add(new Word("Исследование", "[исследовáние]", "Tadqiqot, izlanish", "Sled (Iz)",
                "Olimlar o'tmish izidan (sled) borib, qadimiy sivilizatsiya bo'yicha chuqur ilmiy tadqiqot o'tkazdilar.",
                "Научное исследование подтвердило точность гипотезы.", "Ilmiy tadqiqot gipotezaning to'g'riligini tasdiqladi.", WordLevel.INTERMEDIATE, TargetLanguage.RUSSIAN));

        words.add(new Word("Любопытство", "[любопӹтство]", "Qiziquvchanlik, bilishga intilish", "Lyubit + Pit (Sevmoq)",
                "Yangi bilimlarni bilishga o'ta intiluvchan bolaning qiziquvchanligi barchani hayratga soldi.",
                "Детское любопытство двигает прогресс вперёд.", "Bolalarning qiziquvchanligi taraqqiyotni oldinga suradi.", WordLevel.INTERMEDIATE, TargetLanguage.RUSSIAN));

        words.add(new Word("Достижение", "[достижéние]", "Yutuq, natija, marra", "Dostich (Yetishmoq)",
                "Yillik mashaqqatli mehnat evaziga u o'zining eng katta ilmiy yutug'iga erishdi.",
                "Это грандиозное достижение для нашей лаборатории.", "Bu laboratoriyamiz uchun ulkan yutuqdir.", WordLevel.INTERMEDIATE, TargetLanguage.RUSSIAN));

        words.add(new Word("Уважение", "[уважéние]", "Hurmat, ehtirom", "Vajniy (Muhim)",
                "O'zgalarning fikrini muhim (vajniy) deb bilgan inson jamoada katta hurmat qozonadi.",
                "Взаимное уважение — основа крепкой семьи.", "O'zaro hurmat — mustahkam oilaning poydevoridir.", WordLevel.INTERMEDIATE, TargetLanguage.RUSSIAN));

        words.add(new Word("Сомнение", "[сомнéние]", "Shubha, gumon", "Mnenie (Fikr ikkilanishi)",
                "Fikrlar (mnenie) to'qnashib, qalbida kichik shubha va ikkilanish paydo bo'ldi.",
                "У меня нет никаких сомнений в его честности.", "Menda uning halolligiga hech qanday shubha yo'q.", WordLevel.INTERMEDIATE, TargetLanguage.RUSSIAN));

        words.add(new Word("Ответственность", "[отвéтственность]", "Mas'uliyat, javobgarlik", "Otvet (Javob bermoq)",
                "Har bir so'z va harakatiga to'liq javob (otvet) bera oladigan inson yuksak mas'uliyatga ega.",
                "Лидер несёт персональную ответственность за результат.", "Yetakchi natija uchun shaxsiy mas'uliyatni o'z zimmasiga oladi.", WordLevel.INTERMEDIATE, TargetLanguage.RUSSIAN));

        words.add(new Word("Доверие", "[довéрие]", "Ishonch, e'tiqod", "Vera (Ishonch)",
                "Hamkorlar o'rtasidagi samimiy ishonch (vera) muvaffaqiyatli biznesning kafolatidir.",
                "Завоевать доверие людей требует много времени.", "Odamlarning ishonchini qozonish ko'p vaqt talab qiladi.", WordLevel.INTERMEDIATE, TargetLanguage.RUSSIAN));

        // =========================================================================
        // 🔴 YUQORI DARAJA (ADVANCED: C1 - C2) — RUSCHA
        // =========================================================================
        words.add(new Word("Безупречный", "[безупрéчный]", "Nuqsonsiz, benuqson, bekamu ko'st", "Bez upryoka (Minnatsiz)",
                "Zargar yasagan olmos toj shu qadar benuqson ediki, unga hech kim minnat yoki ayb qo'yolmasdi.",
                "Его безупречная репутация известна во всём мире.", "Uning benuqson obro'si butun dunyoga ma'lum.", WordLevel.ADVANCED, TargetLanguage.RUSSIAN));

        words.add(new Word("Красноречивый", "[красноречúвый]", "Fasohatli, notiq, so'zamol", "Krasivaya rech (Go'zal nutq)",
                "Go'zal va ta'sirli nutq (rech) so'zlab, zaldagi barcha tinglovchilarni rom etgan so'zamol notiq.",
                "Дипломат выступил с красноречивым заявлением.", "Diplomat fasohatli va ta'sirchan bayonot bilan chiqdi.", WordLevel.ADVANCED, TargetLanguage.RUSSIAN));

        words.add(new Word("Кропотливый", "[кропотлúвый]", "Mashaqqatli, qunt talab qiladigan, sinchkov", "Kropotat / Kichik detallar",
                "Qadimiy qo'lyozmalarni tiklash oylar davomida o'ta sinchkov va mashaqqatli mehnatni talab qildi.",
                "Это результат многолетнего кропотливого труда.", "Bu ko'p yillik mashaqqatli va sinchkov mehnatning samarasidir.", WordLevel.ADVANCED, TargetLanguage.RUSSIAN));

        words.add(new Word("Незыблемый", "[незӹблемый]", "Qat'iy, o'zgarmas, mustahkam, yiqilmas", "Zыbkiy emas (Qimirlamas)",
                "Ko'p asrlik adolat tamoyillari davlatning eng mustahkam va o'zgarmas poydevori bo'lib qoladi.",
                "Справедливость — незыблемая основа правового государства.", "Adolat — huquqiy davlatning o'zgarmas poydevoridir.", WordLevel.ADVANCED, TargetLanguage.RUSSIAN));

        words.add(new Word("Скоротечный", "[скоротéчный]", "Tez o'tuvchi, ko'z ochib yumguncha o'tadigan", "Skoro (Tez) + Tech (Oqmoq)",
                "Tog' daryosidek tez oqib (tech) o'tib ketadigan go'zal bahor fasli.",
                "Жизнь полна прекрасных, но скоротечных мгновений.", "Hayot go'zal, ammo tez o'tuvchi lahzalarga to'la.", WordLevel.ADVANCED, TargetLanguage.RUSSIAN));

        words.add(new Word("Утончённый", "[утончённый]", "Nafis, nozik didli, saralangan", "Tonkiy (Yupqa, nozik)",
                "Har bir detalida noziklik va nafislik aks etgan saroy me'morchiligi.",
                "Она обладает удивительно утончённым вкусом.", "U ajoyib darajada nafis va nozik didga ega.", WordLevel.ADVANCED, TargetLanguage.RUSSIAN));

        words.add(new Word("Непреодолимый", "[непреодолúмый]", "Yengib bo'lmas, yengilmas", "Preodolevat (Yengmoq)",
                "Jasur alpinistlar eng murakkab va yengib bo'lmas ko'ringan qoyani ham zabt etishdi.",
                "Нет непреодолимых преград для тех, кто верит в цель.", "Maqsadiga ishonganlar uchun yengib bo'lmas to'siqlar yo'q.", WordLevel.ADVANCED, TargetLanguage.RUSSIAN));
    }

    public List<Word> getAllWords() {
        return Collections.unmodifiableList(words);
    }

    public List<Word> getWordsByLanguage(TargetLanguage language) {
        TargetLanguage lang = (language != null) ? language : TargetLanguage.ENGLISH;
        return words.stream()
                .filter(w -> w.getLanguage() == lang)
                .collect(Collectors.toList());
    }

    public int getTotalDays() {
        return (int) Math.ceil((double) words.size() / WORDS_PER_DAY);
    }

    public List<Word> getWordsByLevel(WordLevel level) {
        return getWordsByLevel(level, TargetLanguage.ENGLISH);
    }

    public List<Word> getWordsByLevel(WordLevel level, TargetLanguage language) {
        TargetLanguage lang = (language != null) ? language : TargetLanguage.ENGLISH;
        if (level == null) {
            return getWordsByLanguage(lang);
        }
        return words.stream()
                .filter(w -> w.getLanguage() == lang && w.getLevel() == level)
                .collect(Collectors.toList());
    }

    /**
     * Foydalanuvchining tanlagan darajasi va tiliga mos 20 ta so'zni qaytaradi
     */
    public List<Word> getWordsForDayAndLevel(int dayIndex, WordLevel level) {
        return getWordsForDayAndLevel(dayIndex, level, TargetLanguage.ENGLISH);
    }

    public List<Word> getWordsForDayAndLevel(int dayIndex, WordLevel level, TargetLanguage language) {
        TargetLanguage lang = (language != null) ? language : TargetLanguage.ENGLISH;
        List<Word> levelWords = getWordsByLevel(level, lang);
        if (levelWords.isEmpty()) {
            levelWords = getWordsByLanguage(lang);
        }
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
        return getRandomWord(TargetLanguage.ENGLISH);
    }

    public Optional<Word> getRandomWord(TargetLanguage language) {
        List<Word> langWords = getWordsByLanguage(language);
        if (langWords.isEmpty()) return Optional.empty();
        return Optional.of(langWords.get(random.nextInt(langWords.size())));
    }

    public Optional<Word> getRandomWordByLevel(WordLevel level) {
        return getRandomWordByLevel(level, TargetLanguage.ENGLISH);
    }

    public Optional<Word> getRandomWordByLevel(WordLevel level, TargetLanguage language) {
        List<Word> filtered = getWordsByLevel(level, language);
        if (filtered.isEmpty()) {
            return getRandomWord(language);
        }
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

    public Optional<Word> findByKeyword(String query, TargetLanguage language) {
        if (query == null || query.trim().isEmpty()) return Optional.empty();
        String normalized = query.trim().toLowerCase();
        TargetLanguage lang = (language != null) ? language : TargetLanguage.ENGLISH;

        return words.stream()
                .filter(w -> w.getLanguage() == lang)
                .filter(w -> w.getEnglishWord().toLowerCase().equals(normalized)
                        || w.getEnglishWord().toLowerCase().contains(normalized)
                        || w.getUzbekMeaning().toLowerCase().contains(normalized)
                        || w.getMnemonicHook().toLowerCase().contains(normalized))
                .findFirst();
    }
}
