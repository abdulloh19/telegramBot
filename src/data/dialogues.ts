import { DailyDialogue } from '@/types';

export const DAILY_DIALOGUES: DailyDialogue[] = [
  // ==========================================
  // 🇬🇧 INGLIZ TILI DIALOGLARI
  // ==========================================
  {
    id: 'en_beginner_day_1',
    title: 'At the Café (Qahvaxonadagi uchrashuv)',
    situationTarget: 'Two friends meeting at a café discussing their day and travel plans.',
    situationUz: 'Ikki do\'st qahvaxonada ko\'rishib, o\'z kunlari va sayohat rejalari haqida suhbatlashmoqda.',
    level: 'BEGINNER',
    dayIndex: 1,
    language: 'en',
    lines: [
      {
        speaker: 'Alex',
        speakerIcon: '🧔',
        textTarget: 'Hey Emma, cats are naturally curious, but why are you looking at that box? Be careful, the glasses inside are fragile!',
        textUz: 'Hey Emma, mushuklar tabiatan qiziquvchan, lekin nega bu qutiga qarayapsan? Ehtiyot bo\'l, ichidagi stakanlar mo\'rt!'
      },
      {
        speaker: 'Emma',
        speakerIcon: '👩',
        textTarget: 'I was eager to see what was inside! Don\'t hesitate to tell me if you need help with moving.',
        textUz: 'Men shunchaki ichida nima borligini ko\'rishga ishtiyoqmand edim! Agar yordam kerak bo\'lsa, aytishga ikkilanma.'
      },
      {
        speaker: 'Alex',
        speakerIcon: '🧔',
        textTarget: 'Thanks Emma! Heavy traffic was a big obstacle this morning. Let\'s quench our thirst with cold tea and stay calm.',
        textUz: 'Rahmat Emma! Bugun ertalab tirbandlik katta to\'siq bo\'ldi. Keling, sovuq choy bilan chanqog\'imizni qondiraylik va xotirjam bo\'laylik.'
      },
      {
        speaker: 'Emma',
        speakerIcon: '👩',
        textTarget: 'That sounds like a marvellous idea! You are always so generous and honest with your friends.',
        textUz: 'Bu ajoyib g\'oya! Sen har doim do\'stlaring bilan juda saxiy va rostgo\'ysan.'
      },
      {
        speaker: 'Alex',
        speakerIcon: '🧔',
        textTarget: 'To be candid, living a frugal life helps me save money to visit ancient historical monuments!',
        textUz: 'Samimiy aytsam, tejamkor hayot kechirishim menga qadimiy tarixiy yodgorliklarni borib ko\'rishga pul yig\'ishga yordam beradi!'
      }
    ],
    targetWords: ['curious', 'fragile', 'eager', 'hesitate', 'obstacle', 'quench', 'calm', 'marvellous', 'generous', 'honest', 'candid', 'frugal', 'ancient']
  },
  {
    id: 'en_intermediate_day_1',
    title: 'Startup Strategy Meeting (Startap strategiya yig\'ilishi)',
    situationTarget: 'Two project managers discussing strategic plans and security in an office.',
    situationUz: 'Ikki loyiha menejeri ofisda strategik rejalar va xavfsizlik haqida suhbatlashmoqda.',
    level: 'INTERMEDIATE',
    dayIndex: 1,
    language: 'en',
    lines: [
      {
        speaker: 'David',
        speakerIcon: '👨‍💼',
        textTarget: 'Sarah, our investors are ambitious, but we need a pragmatic and feasible plan for next month.',
        textUz: 'Sarah, investorlarimiz shijoatli va talabchan, lekin bizga keyingi oy uchun amaliy va real reja kerak.'
      },
      {
        speaker: 'Sarah',
        speakerIcon: '👩‍💼',
        textTarget: 'I agree. High market competition will not deter us if our team remains diligent every day.',
        textUz: 'Qo\'shilaman. Agar jamoamiz har kuni tirishqoq va mehnatsevar bo\'lsa, kuchli bozor raqobati bizni to\'xtatib qololmaydi.'
      },
      {
        speaker: 'David',
        speakerIcon: '👨‍💼',
        textTarget: 'Exactly! Everyone was elated when we launched the first prototype yesterday.',
        textUz: 'Aynan shunday! Kecha birinchi prototipni ishga tushirganimizda butun jamoamiz quvonchdan boshlari osmonda edi.'
      }
    ],
    targetWords: ['ambitious', 'pragmatic', 'feasible', 'deter', 'diligent']
  },

  // ==========================================
  // 🇷🇺 RUS TILI DIALOGLARI
  // ==========================================
  {
    id: 'ru_beginner_day_1',
    title: 'Встреча в уютном кафе (Qahvaxonadagi samimiy uchrashuv)',
    situationTarget: 'Два друга встретились в кафе, чтобы обсудить поездку, погоду и свои мечты.',
    situationUz: 'Ikki do\'st qahvaxonada uchrashib, sayohat, ob-havo va orzulari haqida suhbatlashmoqda.',
    level: 'BEGINNER',
    dayIndex: 1,
    language: 'ru',
    lines: [
      {
        speaker: 'Иван',
        speakerIcon: '🧔',
        textTarget: 'Здравствуйте, Анна! Какая сегодня прекрасная погода! Вдруг пойдёт дождь, держите зонт осторожно.',
        textUz: 'Assalomu alaykum, Anna! Bugun qanday ajoyib ob-havo! To\'satdan yomg\'ir yog\'ib qolsa, soyabonni ehtiyotkorlik bilan ushlang.'
      },
      {
        speaker: 'Анна',
        speakerIcon: '👩',
        textTarget: 'Большое спасибо, Иван! Ваша тёплая улыбка и помощь всегда поднимают мне настроение.',
        textUz: 'Katta rahmat, Ivan! Sizning samimiy tabassumingiz va yordamingiz doimo kayfiyatimni ko\'taradi.'
      },
      {
        speaker: 'Иван',
        speakerIcon: '🧔',
        textTarget: 'Для меня наша дружба — настоящее счастье! У нас есть время выпить горячий чай и верить в успех.',
        textUz: 'Men uchun do\'stligimiz — haqiqiy baxt! Bizda qaynoq choy ichishga va muvaffaqiyatga ishonishga vaqt bor.'
      },
      {
        speaker: 'Анна',
        speakerIcon: '👩',
        textTarget: 'Это чистая правда! Моя заветная мечта — отправиться в сказочное путешествие уже завтра.',
        textUz: 'Bu toza haqiqat! Mening ezgu orzum — ertagayoq ertaknamo ajoyib sayohatga otlanish.'
      },
      {
        speaker: 'Иван',
        speakerIcon: '🧔',
        textTarget: 'Прекрасно! Обратите внимание на маршрут, а вера и надежда приведут нас к победе!',
        textUz: 'Ajoyib! Marshrutga diqqat qarating, ishonch va umid esa bizni g\'alabaga yetaklaydi!'
      }
    ],
    targetWords: ['Спасибо', 'Вдруг', 'Мечта', 'Победа', 'Погода', 'Осторожно', 'Улыбка', 'Дружба', 'Помощь', 'Сказка', 'Время', 'Надежда', 'Счастье', 'Путешествие', 'Внимание', 'Правда', 'Успех', 'Завtra', 'Здравствуйте']
  },
  {
    id: 'ru_intermediate_day_1',
    title: 'Стратегия стартапа (Startap va strategiya)',
    situationTarget: 'Два руководителя обсуждают исследование рынка и преодоление препятствий.',
    situationUz: 'Ikki rahbar bozor tadqiqoti va to\'siqlarni yengib o\'tishni muhokama qilmoqda.',
    level: 'INTERMEDIATE',
    dayIndex: 1,
    language: 'ru',
    lines: [
      {
        speaker: 'Михаил',
        speakerIcon: '👨‍💼',
        textTarget: 'Никакое препятствие не сломит нас, если в команде есть решительность и вдохновение.',
        textUz: 'Agar jamoada qat\'iyat va ilhom bo\'lsa, hech qanday to\'siq bizni sindira olmaydi.'
      },
      {
        speaker: 'Елена',
        speakerIcon: '👩‍💼',
        textTarget: 'Согласна! Наше научное исследование доказало, что любопытство клиентов стимулирует спрос.',
        textUz: 'Qo\'shilaman! Bizning ilmiy tadqiqotimiz mijozlarning qiziquvchanligi talabni rag\'batlantirishini isbotladi.'
      },
      {
        speaker: 'Михаил',
        speakerIcon: '👨‍💼',
        textTarget: 'Это грандиозное достижение! Взаимное уважение и полное доверие инвесторов укрепят позиции.',
        textUz: 'Bu ulkan yutuq! O\'zaro hurmat va investorlarning to\'liq ishonchi pozitsiyamizni mustahkamlaydi.'
      }
    ],
    targetWords: ['Препятствие', 'Вдохновение', 'Решительность', 'Исследование', 'Любопытство', 'Достижение']
  },

  // ==========================================
  // 🛒 KUNDALIK HAYOTIY DIALOGLAR (YANGI)
  // ==========================================
  {
    id: 'ru_daily_supermarket',
    title: 'В супермаркете и на кассе (Do\'konda xarid)',
    situationTarget: 'Покупатель выбирает свежие продукты и оплачивает покупки на кассе.',
    situationUz: 'Xaridor yangi mahsulotlarni tanlab, kassada to\'lov qilmoqda.',
    level: 'BEGINNER',
    dayIndex: 2,
    language: 'ru',
    lines: [
      {
        speaker: 'Покупатель',
        speakerIcon: '🧔',
        textTarget: 'Здравствуйте! Скажите, пожалуйста, где у вас свежий хлеб и сколько стоит эта вода?',
        textUz: 'Assalomu alaykum! Ayting-chi, iltimos, yangi non qayerda va bu suv qancha turadi?'
      },
      {
        speaker: 'Кассир',
        speakerIcon: '👩',
        textTarget: 'Добрый день! Хлеб в третьем ряду. Вода стоит пятьдесят рублей. Вам нужен пакет?',
        textUz: 'Xayrli kun! Non uchinchi qatorda. Suv ellik rubl turadi. Sizga paket kerakmi?'
      },
      {
        speaker: 'Покупатель',
        speakerIcon: '🧔',
        textTarget: 'Да, маленький пакет, пожалуйста. Можно оплатить картой? И дайте, пожалуйста, чек.',
        textUz: 'Ha, kichkina paket, iltimos. Karta bilan to\'lasam bo\'ladimi? Va iltimos, chekni ham bering.'
      },
      {
        speaker: 'Кассир',
        speakerIcon: '👩',
        textTarget: 'Конечно, приложите карту к терминалу. Вот ваш чек. Спасибо за покупку, хорошего дня!',
        textUz: 'Albatta, kartani terminalga tekkizing. Mana chekingiz. Xaridingiz uchun rahmat, kuningiz xayrli o\'tsin!'
      }
    ],
    targetWords: ['Здравствуйте', 'Пожалуйста', 'Сколько', 'Вода', 'Пакет', 'Карта', 'Чек', 'Спасибо']
  },
  {
    id: 'ru_daily_taxi',
    title: 'Поездка на городском такси (Taksida yo\'l yurish)',
    situationTarget: 'Пассажир садится в такси и объясняет водителю маршрут до вокзала.',
    situationUz: 'Yo\'lovchi taksiga o\'tirib, haydovchiga vokzalgacha bo\'lgan yo\'lni tushuntirmoqda.',
    level: 'BEGINNER',
    dayIndex: 3,
    language: 'ru',
    lines: [
      {
        speaker: 'Пассажир',
        speakerIcon: '👨',
        textTarget: 'Добрый вечер! До центрального вокзала, пожалуйста. Мы успеем до отправления поезда?',
        textUz: 'Xayrli kech! Markaziy vokzalgacha, iltimos. Poyezd jo\'naguncha ulguramizmi?'
      },
      {
        speaker: 'Водитель',
        speakerIcon: '🚕',
        textTarget: 'Здравствуйте! Сейчас на проспекте небольшая пробка, но через пятнадцать минут будем на месте.',
        textUz: 'Assalomu alaykum! Hozir shoh ko\'chada kichik tirbandlik bor, ammo o\'n besh daqiqada yetib boramiz.'
      },
      {
        speaker: 'Пассажир',
        speakerIcon: '👨',
        textTarget: 'Отлично! Поверните направо на светофоре и остановите прямо у главного входа.',
        textUz: 'Ajoyib! Svetoforda o\'ngga buriling va to\'g\'ridan-to\'g\'ri bosh kirish eshigi oldida to\'xtating.'
      },
      {
        speaker: 'Водитель',
        speakerIcon: '🚕',
        textTarget: 'Договорились! Приехали. С вас триста рублей. Вот ваша сдача, счастливого пути!',
        textUz: 'Kelishdik! Yetib keldik. Sizdan uch yuz rubl. Mana qaytim, oq yo\'l!'
      }
    ],
    targetWords: ['Добрый вечер', 'Пожалуйста', 'Пробка', 'Остановите', 'Сдача', 'Здравствуйте']
  },
  {
    id: 'en_daily_supermarket',
    title: 'At the Grocery Store (Do\'konda kundalik xarid)',
    situationTarget: 'A customer buying daily necessities and asking about discounts.',
    situationUz: 'Xaridor kundalik kerakli mahsulotlarni sotib olmoqda va chegirmalar haqida so\'ramoqda.',
    level: 'BEGINNER',
    dayIndex: 2,
    language: 'en',
    lines: [
      {
        speaker: 'Customer',
        speakerIcon: '🧔',
        textTarget: 'Excuse me! Could you tell me where I can find fresh dairy products and cold juice?',
        textUz: 'Kechirasiz! Yangi sut mahsulotlari va sovuq sharbatni qayerdan topsam bo\'ladi?'
      },
      {
        speaker: 'Shop Assistant',
        speakerIcon: '👩',
        textTarget: 'Sure! Aisle four on the left. Today we have a special ten percent discount on bakery items.',
        textUz: 'Albatta! Chap tomondagi to\'rtinchi qatorda. Bugun novvoyxona mahsulotlariga o\'n foizli maxsus chegirma bor.'
      },
      {
        speaker: 'Customer',
        speakerIcon: '🧔',
        textTarget: 'That is wonderful! Do you accept contactless credit cards at the checkout counter?',
        textUz: 'Bu juda ajoyib! Kassada kontaktsiz plastik kartalarni qabul qilasizmi?'
      },
      {
        speaker: 'Shop Assistant',
        speakerIcon: '👩',
        textTarget: 'Yes, absolutely. Here is your digital receipt and paper bag. Have a wonderful day!',
        textUz: 'Ha, albatta. Mana sizning elektron chekingiz va qog\'oz paketingiz. Kuningiz ajoyib o\'tsin!'
      }
    ],
    targetWords: ['Excuse me', 'Discount', 'Receipt', 'Fresh', 'Special']
  },
  {
    id: 'en_daily_taxi',
    title: 'Taking a City Taxi (Shaharda taksiga chiqish)',
    situationTarget: 'A passenger giving directions and checking arrival time.',
    situationUz: 'Yo\'lovchi manzilni tushuntirmoqda va yetib borish vaqtini so\'ramoqda.',
    level: 'BEGINNER',
    dayIndex: 3,
    language: 'en',
    lines: [
      {
        speaker: 'Passenger',
        speakerIcon: '👨',
        textTarget: 'Hello! I need to go to the international airport, please. How long will the trip take?',
        textUz: 'Salom! Men xalqaro aeroportga borishim kerak, iltimos. Safar qancha vaqt oladi?'
      },
      {
        speaker: 'Driver',
        speakerIcon: '🚕',
        textTarget: 'Good morning! If traffic is smooth, we should arrive in about twenty minutes.',
        textUz: 'Xayrli tong! Agar yo\'lda tirbandlik bo\'lmasa, taxminan yigirma daqiqada yetib boramiz.'
      },
      {
        speaker: 'Passenger',
        speakerIcon: '👨',
        textTarget: 'Great! Please drop me off right in front of Terminal Two. Keep the change!',
        textUz: 'Ajoyib! Meni ikkinchi terminal ro\'parasida qoldiring, iltimos. Qaytimi sizda qolsin!'
      }
    ],
    targetWords: ['Traffic', 'Drop off', 'Airport', 'Change', 'Smooth']
  }
];
