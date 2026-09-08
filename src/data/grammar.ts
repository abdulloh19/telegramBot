import { GrammarTopic } from '@/types';

export const GRAMMAR_TOPICS: GrammarTopic[] = [
  // ==================== RUSSIAN GRAMMAR ====================
  {
    id: 'ru_gram_rod',
    title: 'Род имён существительных',
    titleUz: 'Otlar rodi (Мужской, Женский, Средний)',
    category: 'Morfologiya',
    level: 'BEGINNER',
    language: 'ru',
    icon: '👥',
    mnemonicRule: 'Oxirgi harfga qara: Undosh = Erkak (М), А/Я = Ayol (Ж), О/Е = O\'rta (С)!',
    detailedExplanation: 
      'Rus tilida barcha otlar 3 ta rodga bo\'linadi. Buni aniqlash juda oson:\n' +
      '1. Мужской род (Он): Oxiri undosh harf bilan tugaydi (стол, дом, брат, друг).\n' +
      '2. Женский род (Она): Oxiri -А yoki -Я bilan tugaydi (книга, мама, песня, сестра).\n' +
      '3. Средний род (Оно): Oxiri -О yoki -Е bilan tugaydi (окно, море, письмо, солнце).\n' +
      '💡 ISTISNO: Erkak jinsidagi shaxslar -А/-Я bilan tugasa ham Мужской bo\'ladi: папа, дедушка, дядя (chunki ular erkak!).',
    formula: 'Undosh ➔ Он (М)  |  -А / -Я ➔ Она (Ж)  |  -О / -Е ➔ Оно (С)',
    examples: [
      { target: 'Этот стол новый.', uzbek: 'Bu stol yangi. (стол — undosh, мужской род)', note: 'Мужской род' },
      { target: 'Моя мама добрая.', uzbek: 'Mening onam mehribon. (мама — -а, женский род)', note: 'Женский род' },
      { target: 'Это окно открыто.', uzbek: 'Bu deraza ochiq. (окно — -о, средний род)', note: 'Средний род' },
      { target: 'Мой папа работает.', uzbek: 'Mening dadam ishlaydi. (папа — erkak kishi, erkak rodi)', note: 'Istisno' },
    ],
    quiz: {
      question: '"Яблоко" (olma) so\'zi qaysi rodga kiradi?',
      options: ['Мужской род (Он)', 'Женский род (Она)', 'Средний rod (Оно)', 'Rodi yo\'q'],
      correctIndex: 2,
      explanation: '"Яблоко" so\'zi -О harfi bilan tugaydi, demak u Средний род (Оно).',
    },
  },

  {
    id: 'ru_gram_padeji',
    title: '6 Падежей в русском языке',
    titleUz: 'Rus tilida 6 ta kelishik (Падежи)',
    category: 'Sintaksis & Kelishik',
    level: 'BEGINNER',
    language: 'ru',
    icon: '🎯',
    mnemonicRule: '«Иван Родил Девчонку, Велел Тащить Пелёнку» — Bosh harflar kelishiklar tartibi!',
    detailedExplanation:
      'Rus tilidagi 6 ta kelishikni chalkashtirmaslik uchun rus bolalari mashhur mnemonik she\'rdan foydalanishadi:\n' +
      '• И — Именительный (Кто? Что?) ➔ Bosh kelishik: Иван\n' +
      '• Р — Родительный (Кого? Чего? Откуда?) ➔ Qaratqich / Chiqish: Родил\n' +
      '• Д — Дательный (Кому? Чему?) ➔ Jo\'nalish (Berish): Девчонку\n' +
      '• В — Винительный (Кого? Что? Куда?) ➔ Tushum (Ayblov): Велел\n' +
      '• Т — Творительный (Кем? Чем?) ➔ Qurol / Birgalik: Тащить\n' +
      '• П — Предложный (О ком? О чём? Где?) ➔ O\'rin-payt: Пелёнку',
    formula: 'И (Кто?) ➔ Р (Кого?) ➔ Д (Кому?) ➔ В (Кого/Что?) ➔ Т (Кем/Чем?) ➔ П (О ком/Где?)',
    examples: [
      { target: 'Книга лежит на столе.', uzbek: 'Kitob stol ustida yotibdi. (Предложный: Где?)', note: 'Предложный' },
      { target: 'Я звоню другу.', uzbek: 'Men do\'stimga qo\'ng\'iroq qilyapman. (Дательный: Кому?)', note: 'Дательный' },
      { target: 'У меня нет времени.', uzbek: 'Menda vaqt yo\'q. (Родительный: Чего?)', note: 'Родительный' },
      { target: 'Я пишу ручкой.', uzbek: 'Men ruchka bilan yozmoqdaman. (Творительный: Чем?)', note: 'Творительный' },
    ],
    quiz: {
      question: '"Я иду к врачу" jumlasidagi "к врачу" qaysi kelishikda?',
      options: ['Именительный', 'Дательный (Кому?)', 'Родительный', 'Творительный'],
      correctIndex: 1,
      explanation: '"К врачу" (kimga? shifokorga) — "К" predlogi doim Дательный kelishigi (Кому?) bilan ishlatiladi.',
    },
  },

  {
    id: 'ru_gram_predlogi',
    title: 'Предлоги В / НА и ИЗ / С',
    titleUz: 'Yo\'nalish predloglari juftligi (В-ИЗ vs НА-С)',
    category: 'Predloglar',
    level: 'BEGINNER',
    language: 'ru',
    icon: '🔄',
    mnemonicRule: '«В» ga borgan «ИЗ» dan qaytadi, «НА» ga borgan «С» dan qaytadi!',
    detailedExplanation:
      'Qayerga borish va qayerdan qaytishni rus tilida hech qachon adashtirmaslik siri:\n' +
      '1. Ichkariga (yopiq joyga): В школу ➔ Qaytish: ИЗ школы (В ➔ ИЗ juftligi)\n' +
      '2. Yuzaga, ochiq joyga yoki tadbirga: НА работу, НА концерт ➔ Qaytish: С работы, С концерта (НА ➔ С juftligi)\n' +
      '❌ Hech qachon "с школы" yoki "из работы" deb aytilmaydi!',
    formula: 'В ➔ ИЗ  (ichiga / ichidan)  |  НА ➔ С  (ustiga, tadbirga / u yerdan)',
    examples: [
      { target: 'Он пошёл в театр, а потом вернулся из театра.', uzbek: 'U teatrga bordi va keyin teatrdan qaytdi.', note: 'В ➔ ИЗ' },
      { target: 'Я сейчас на работе, приду с работы в шесть.', uzbek: 'Men hozir ishdamon, ishdan soat oltida kelaman.', note: 'НА ➔ С' },
      { target: 'Мы едем в Ташкент.', uzbek: 'Biz Toshkentga ketyapmiz.', note: 'Shahar va davlatlarga В' },
    ],
    quiz: {
      question: 'Agar siz "в университет" ga borgan bo\'lsangiz, qayerdan qaytasiz?',
      options: ['С университета', 'Из университета', 'От университета', 'К университету'],
      correctIndex: 1,
      explanation: 'Qoida: "В" bilan borgan joyingizdan "ИЗ" bilan qaytasiz: в университет ➔ из университета!',
    },
  },

  {
    id: 'ru_gram_vidy',
    title: 'Виды глагола (СВ vs НСВ)',
    titleUz: 'Fe\'l vidlari: Natija (СВ) vs Jarayon (НСВ)',
    category: 'Fe\'llar',
    level: 'INTERMEDIATE',
    language: 'ru',
    icon: '🎬',
    mnemonicRule: 'НСВ = Jarayon kinofilmi (qilyapti), СВ = Natija fotosurati (qilib bo\'ldi)!',
    detailedExplanation:
      'Rus tilida har bir fe\'lning ikkita ko\'rinishi bor:\n' +
      '1. Несовершенный вид (НСВ): Harakat davom etmoqda, jarayon yoki odat tusiga kirgan takroriy ish.\n' +
      '   Savoli: Что делать? (читать, писать, учить)\n' +
      '2. Совершенный вид (СВ): Harakat tugagan, aniq natijasi bor yoki bir martalik aniq ish.\n' +
      '   Savoli: Что сделать? (прочитать, написать, выучить)',
    formula: 'НСВ: Jarayon / Takrorlanish (Что делать?)  |  СВ: Natija / Yakun (Что сделать?)',
    examples: [
      { target: 'Я читал книгу два часа.', uzbek: 'Men ikki soat kitob o\'qidim. (Jarayon — НСВ)', note: 'Jarayon' },
      { target: 'Я прочитал эту книгу вчера.', uzbek: 'Men bu kitobni kecha o\'qib bo\'ldim. (Natija bor — СВ)', note: 'Natija' },
      { target: 'Каждый день я делаю зарядку.', uzbek: 'Har kuni men badantarbiya qilaman. (Takroriy odat — НСВ)', note: 'Odat' },
    ],
    quiz: {
      question: 'Qaysi jumla harakatning to\'liq yakunlangani va natijasi borligini bildiradi?',
      options: ['Я писал письмо.', 'Я написал письмо.', 'Я долго писал.', 'Я обычно пишу.'],
      correctIndex: 1,
      explanation: '"Я написал письмо" — СВ (Что сделал?), maktub tayyor yozib bo\'lingan, natija mavjud.',
    },
  },

  {
    id: 'ru_gram_prilag',
    title: 'Окончания прилагательных',
    titleUz: 'Sifat qo\'shimchalarining oson siri',
    category: 'Sifatlar',
    level: 'BEGINNER',
    language: 'ru',
    icon: '🎨',
    mnemonicRule: 'Savolning oxiriga qara: Какой? ➔ -ый/-ой, Какая? ➔ -ая, Какое? ➔ -ое!',
    detailedExplanation:
      'Sifat qaysi otga bog\'lansa, o\'sha otning rodi va savoliga moslashadi:\n' +
      '• Какой? (Erkak rodi): новый дом, большой город (-ый, -ий, -ой)\n' +
      '• Какая? (Ayol rodi): новая машина, красивая песня (-ая, -яя)\n' +
      '• Какое? (O\'rta rod): новое слово, синее море (-ое, -ее)\n' +
      '• Какие? (Ko\'plik): новые друзья, умные люди (-ые, -ие)\n' +
      'Ko\'rib turganingizdek, savol qanday ohangda tugasa, javob ham shunday jaranglaydi!',
    formula: 'КакОЙ? ➔ -ый/-ой  |  КакАЯ? ➔ -ая/-яя  |  КакОЕ? ➔ -ое/-ее',
    examples: [
      { target: 'Это очень интересный фильм.', uzbek: 'Bu juda qiziqarli film. (фильм — мужской род, какой?)', note: 'Мужской' },
      { target: 'Она носит красивую куртку.', uzbek: 'U chiroyli kurtka kiyib yuradi.', note: 'Женский' },
      { target: 'Здесь чистое озеро.', uzbek: 'Bu yerda toza ko\'l bor. (озеро — средний род, какое?)', note: 'Средний' },
    ],
    quiz: {
      question: '"Машина" (mashina - ayol rodi) so\'ziga qaysi sifat to\'g\'ri keladi?',
      options: ['Красный машина', 'Красная машина', 'Красное машина', 'Красные машина'],
      correctIndex: 1,
      explanation: '"Машина" — женский род (Какая?), demak qo\'shimcha -ая: "Красная машина".',
    },
  },

  // ==================== ENGLISH GRAMMAR ====================
  {
    id: 'en_gram_tenses',
    title: 'Present Simple vs Present Continuous',
    titleUz: 'Hozirgi oddiy vs Hozirgi davomli zamon',
    category: 'Tenses (Zamonlar)',
    level: 'BEGINNER',
    language: 'en',
    icon: '⏱️',
    mnemonicRule: 'Simple = Fotosurat (odat, doim), Continuous = Jonli video (ayni hozir)!',
    detailedExplanation:
      'Bu ikki zamonni ajratish juda oson:\n' +
      '1. Present Simple (V / V+s): Har kuni, doim, odatda sodir bo\'ladigan voqealar yoki qonuniyatlar.\n' +
      '   Kalit so\'zlar: always, usually, every day, often, never.\n' +
      '2. Present Continuous (am/is/are + V-ing): Aynan hozir, ayni gapirilayotgan soniyada bo\'layotgan harakat.\n' +
      '   Kalit so\'zlar: right now, at the moment, look!, listen!.',
    formula: 'Simple: Subject + V(s)  |  Continuous: Subject + am/is/are + V-ing',
    examples: [
      { target: 'I drink coffee every morning.', uzbek: 'Men har kuni ertalab qahva ichaman. (Odat — Simple)', note: 'Present Simple' },
      { target: 'I am drinking coffee right now.', uzbek: 'Men ayni damda qahva ichyapman. (Ayni payt — Continuous)', note: 'Present Continuous' },
      { target: 'He plays football on Sundays.', uzbek: 'U yakshanba kunlari futbol o\'ynaydi.', note: 'Odat' },
      { target: 'Look! It is raining outside.', uzbek: 'Qarang! Tashqarida yomg\'ir yog\'yapti.', note: 'Hozir sodir bo\'lyapti' },
    ],
    quiz: {
      question: '"Listen! Someone ________ the piano."',
      options: ['plays', 'is playing', 'play', 'are playing'],
      correctIndex: 1,
      explanation: '"Listen!" (Quloq sol!) ayni shu soniyada sodir bo\'layotganini bildiradi, shuning uchun Present Continuous: "is playing".',
    },
  },

  {
    id: 'en_gram_articles',
    title: 'Articles: A, An vs THE',
    titleUz: 'Artikllar siri (A / An vs THE)',
    category: 'Grammar Basics',
    level: 'BEGINNER',
    language: 'en',
    icon: '🎪',
    mnemonicRule: 'A / An = Begona (har qanday bittasi), THE = Qadrdon (ikkalamiz bilgan o\'sha narsa)!',
    detailedExplanation:
      'Artikllar bilan do\'stlashish siri:\n' +
      '1. A / An (Noaniq): Faqat birlikdagi sanaladigan otlar oldidan. Tinglovchiga hali notanish bo\'lgan har qanday bir buyum.\n' +
      '   • Undosh tovushdan oldin ➔ A (a book, a car, a university)\n' +
      '   • Unli tovushdan oldin ➔ AN (an apple, an hour)\n' +
      '2. THE (Aniq): Ikkalamiz ham aynan qaysi buyum haqida gap ketayotganini bilsak yoki dunyoda yagona bo\'lsa (the sun, the moon, the world).',
    formula: 'A/An + Notanish birlik ot  |  THE + Aniq ma\'lum ot (birlik yoki ko\'plik)',
    examples: [
      { target: 'I bought a car yesterday. The car is red.', uzbek: 'Men kecha mashina sotib oldim (birinchi marta tanishtiruv - a). Mashina qizil rangda (ikkalamiz bilgan o\'sha mashina - the).', note: 'A ➔ THE zanjiri' },
      { target: 'Can you close the door, please?', uzbek: 'Eshikni yopib yubora olmaysizmi? (Xonadagi aynan o\'sha eshik)', note: 'Aniq eshik' },
      { target: 'She ate an apple.', uzbek: 'U bitta olma yedi.', note: 'Unli tovush oldidan AN' },
    ],
    quiz: {
      question: '"Look at _______ moon tonight! It is so bright."',
      options: ['a', 'an', 'the', '-'],
      correctIndex: 2,
      explanation: 'Oy (moon) tabiatda bitta va yagona, shuning uchun faqat "THE" artikli qo\'yiladi.',
    },
  },

  {
    id: 'en_gram_prepositions',
    title: 'Prepositions: IN, ON, AT (Pyramid Rule)',
    titleUz: 'Vaqt va Joy predloglari (IN, ON, AT piramidasi)',
    category: 'Predloglar',
    level: 'BEGINNER',
    language: 'en',
    icon: '🔺',
    mnemonicRule: 'Piramida: IN = Katta/Keng, ON = O\'rta/Torroq, AT = Aniq Nuqta!',
    detailedExplanation:
      'Vaqt va joy predloglarini teskari uchburchak piramidasi orqali eslab qoling:\n' +
      '1. IN (Eng katta, keng):\n' +
      '   • Vaqt: Yillar, oylar, fasllar, asrlar (in 2026, in summer, in May)\n' +
      '   • Joy: Mamlakatlar, shaharlar (in Uzbekistan, in London)\n' +
      '2. ON (O\'rta daraja):\n' +
      '   • Vaqt: Kunlar, sanalar (on Monday, on my birthday, on May 5th)\n' +
      '   • Joy: Ko\'chalar, sirtlar (on Navoi street, on the table)\n' +
      '3. AT (Aniq nuqta):\n' +
      '   • Vaqt: Aniq soat (at 7:00 PM, at midnight)\n' +
      '   • Joy: Aniq manzil, nuqta (at home, at school, at the bus stop)',
    formula: 'IN (Keng: Yil / Shahar) ➔ ON (Kun / Ko\'cha) ➔ AT (Soat / Nuqta)',
    examples: [
      { target: 'I was born in 2000 in Tashkent.', uzbek: 'Men 2000-yilda Toshkentda tug\'ilganman.', note: 'Katta: Yil va Shahar (IN)' },
      { target: 'See you on Friday on Amir Temur street.', uzbek: 'Juma kuni Amir Temur ko\'chasida ko\'rishguncha.', note: 'O\'rta: Kun va Ko\'cha (ON)' },
      { target: 'The train arrives at 9:30 AM at the station.', uzbek: 'Poyezd 9:30 da vokzalga yetib keladi.', note: 'Aniq nuqta va soat (AT)' },
    ],
    quiz: {
      question: '"The concert starts _______ 8 o\'clock _______ Saturday."',
      options: ['in / on', 'at / on', 'on / at', 'at / in'],
      correctIndex: 1,
      explanation: 'Aniq soat bilan "AT" (at 8 o\'clock), hafta kunlari bilan "ON" (on Saturday) ishlatiladi.',
    },
  },

  {
    id: 'en_gram_modals',
    title: 'Modal Verbs: Can, Must, Should',
    titleUz: 'Modal fe\'llar (Can, Must, Should)',
    category: 'Modals',
    level: 'INTERMEDIATE',
    language: 'en',
    icon: '⚡',
    mnemonicRule: 'CAN = Qodirlik (qo\'ldan keladi), MUST = 100% Majburiyat, SHOULD = Do\'stona Maslahat!',
    detailedExplanation:
      'Modal fe\'llardan keyin doimo fe\'lning asil o\'zagi (to qo\'shimchasisiz V1) keladi:\n' +
      '• CAN: Jismoniy yoki aqliy qobiliyat ("Qila olaman"). Inkor: cannot / can\'t.\n' +
      '• MUST: Qat\'iy qoida, qonun yoki ichki burch ("Qilishim shart/majburman").\n' +
      '• SHOULD: Yumshoq maslahat yoki tavsiya ("Qilsang yaxshi bo\'lardi").\n' +
      '• MAY / MIGHT: Ehtimollik yoki ruxsat so\'rash ("Mumkin / balki").',
    formula: 'Subject + Modal (Can / Must / Should) + V1 (fe\'l o\'zagi)',
    examples: [
      { target: 'You should drink more water.', uzbek: 'Ko\'proq suv ichishingiz kerak. (Maslahat)', note: 'Maslahat (Should)' },
      { target: 'Drivers must stop at red lights.', uzbek: 'Haydovchilar qizil chiroqda to\'xtashlari shart. (Qonun-qoida)', note: 'Majburiyat (Must)' },
      { target: 'She can speak three languages.', uzbek: 'U uchta tilda gaplasha oladi. (Qobiliyat)', note: 'Qodirlik (Can)' },
    ],
    quiz: {
      question: '"You have a high fever. You ________ see a doctor."',
      options: ['should', 'can', 'may', 'will'],
      correctIndex: 0,
      explanation: 'Kasallik va shifokorga ko\'rinish bo\'yicha eng to\'g\'ri do\'stona tavsiya bu "should" (ko\'rinsangiz yaxshi bo\'lardi).',
    },
  },

  {
    id: 'en_gram_conditionals',
    title: 'Conditionals (If gaplari)',
    titleUz: 'Shart ergash gaplar (1st va 2nd Conditionals)',
    category: 'Murakkab jumlalar',
    level: 'INTERMEDIATE',
    language: 'en',
    icon: '🔮',
    mnemonicRule: '1st = Real Kelajak (If + Present, will), 2nd = Xayoliy Orzu (If + Past, would)!',
    detailedExplanation:
      'Shart gaplarni 2 ta asosiy hayotiy holatda o\'rganing:\n' +
      '1. First Conditional (Haqiqiy bo\'lishi mumkin bo\'lgan shart):\n' +
      '   Formula: If + Present Simple, will + V1\n' +
      '   Misol: If it rains, I will take an umbrella. (Agar yomg\'ir yog\'sa, soyabon olaman — bu real bo\'lishi mumkin).\n' +
      '2. Second Conditional (Noaniq, xayoliy yoki orzu qilingan shart):\n' +
      '   Formula: If + Past Simple, would + V1\n' +
      '   Misol: If I had a million dollars, I would travel the world. (Agar million dollarim bo\'lganida, dunyo bo\'ylab sayohat qilardim — ayni damda yo\'q, orzu).',
    formula: 'Real: If + V(s) ➔ will + V  |  Orzu: If + V2/ed ➔ would + V',
    examples: [
      { target: 'If you study hard, you will pass the exam.', uzbek: 'Agar qattiq o\'qisang, imtihondan o\'tasan. (Real kelajak)', note: '1st Conditional' },
      { target: 'If I were you, I would accept the job.', uzbek: 'Agar sening o\'rningda bo\'lganimda, bu ishni qabul qilardim. (Maslahat / xayoliy)', note: '2nd Conditional' },
      { target: 'If we hurry, we will catch the bus.', uzbek: 'Agar shoshilsak, avtobusga ulguramiz.', note: 'Real shart' },
    ],
    quiz: {
      question: '"If she _______ free tomorrow, she will visit us."',
      options: ['is', 'will be', 'was', 'were'],
      correctIndex: 0,
      explanation: '1st Conditional qoidasiga ko\'ra "If" qismida kelasi zamon ("will") ishlatilmaydi, uning o\'rniga Present Simple ("is") qo\'yiladi.',
    },
  },
];
