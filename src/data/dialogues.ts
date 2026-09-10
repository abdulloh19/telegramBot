import { DailyDialogue } from '@/types';

export const DAILY_DIALOGUES: DailyDialogue[] = [
  // =========================================================================
  // 🇬🇧 INGLIZ TILI KUNDALIK HAYOTIY DIALOGLARI (5 MAVZU + 1 COMBO)
  // =========================================================================
  {
    id: 'en_daily_taxi',
    title: 'Taking a City Taxi (Taksida yo\'l yurish)',
    situationTarget: 'A passenger hails a taxi, gives directions to the train station, and discusses the route with the driver.',
    situationUz: 'Yo\'lovchi taksi to\'xtatib, haydovchiga vokzalga borish yo\'lini tushuntirmoqda va to\'lov qilmoqda.',
    level: 'BEGINNER',
    dayIndex: 1,
    language: 'en',
    category: 'taxi',
    lines: [
      {
        speaker: 'Passenger',
        speakerIcon: '👨',
        textTarget: 'Good morning! Are you available? I need to go to the Central Train Station as soon as possible.',
        textUz: 'Xayrli tong! Bo\'shmisiz? Men imkon qadar tezroq Markaziy Vokzalga borishim kerak.'
      },
      {
        speaker: 'Driver',
        speakerIcon: '🚕',
        textTarget: 'Hello! Yes, please get in. There is some morning traffic on the main avenue, but we can take a shortcut.',
        textUz: 'Salom! Ha, marhamat o\'tiring. Bosh shoh ko\'chada ozgina ertalabki tirbandlik bor, ammo aylanma qisqa yo\'ldan borsak bo\'ladi.'
      },
      {
        speaker: 'Passenger',
        speakerIcon: '👨',
        textTarget: 'That is great! How long will the ride take? My train departs in thirty-five minutes.',
        textUz: 'Juda soz! Yo\'l qancha vaqt oladi? Poyezdim o\'ttiz besh daqiqada jo\'nab ketadi.'
      },
      {
        speaker: 'Driver',
        speakerIcon: '🚕',
        textTarget: 'Don\'t worry, we will arrive in about fifteen minutes. Please fasten your seatbelt.',
        textUz: 'Xavotir olmang, o\'n besh daqiqalarda yetib boramiz. Iltimos, xavfsizlik kamarini taqing.'
      },
      {
        speaker: 'Passenger',
        speakerIcon: '👨',
        textTarget: 'Here we are! Could you please stop right in front of the main entrance? Can I pay with credit card?',
        textUz: 'Mana yetib keldik! Bosh kirish eshigi oldida to\'xtata olasizmi? Plastik karta orqali to\'lasam bo\'ladimi?'
      },
      {
        speaker: 'Driver',
        speakerIcon: '🚕',
        textTarget: 'Certainly! Tap your card on the terminal. The total is eight dollars. Here is your receipt, have a pleasant trip!',
        textUz: 'Albatta! Kartangizni terminalga tekkizing. Jami sakkiz dollar. Mana chekingiz, safaringiz xayrli bo\'lsin!'
      }
    ],
    targetWords: ['Available', 'Traffic', 'Shortcut', 'Depart', 'Fasten', 'Receipt', 'Pleasant']
  },

  {
    id: 'en_daily_travel',
    title: 'Airport Check-in & Travel (Aeroport va Sayohat)',
    situationTarget: 'A traveler checks in for a flight at the airport and asks about baggage allowances.',
    situationUz: 'Sayohatchi aeroportda reysga ro\'yxatdan o\'tib, bagaj va parvoz haqida so\'ramoqda.',
    level: 'BEGINNER',
    dayIndex: 2,
    language: 'en',
    category: 'travel',
    lines: [
      {
        speaker: 'Traveler',
        speakerIcon: '🧳',
        textTarget: 'Good afternoon! Here is my passport and booking confirmation for flight BA 204 to London.',
        textUz: 'Xayrli kun! Mana pasportim va London tomon BA 204 reysiga band qilingan chiptam.'
      },
      {
        speaker: 'Agent',
        speakerIcon: '👩‍💼',
        textTarget: 'Welcome! Thank you. Are you checking any luggage today, or do you only have carry-on bags?',
        textUz: 'Xush kelibsiz! Rahmat. Bugun biror yuk (bagaj) topshirasizmi yoki faqat qo\'l yuki bormi?'
      },
      {
        speaker: 'Traveler',
        speakerIcon: '🧳',
        textTarget: 'I have one large suitcase to check and a small backpack. Would it be possible to get a window seat?',
        textUz: 'Bitta katta chamadonim bor va kichik ryukzak. Oyna yonidan (window seat) joy olsa bo\'ladimi?'
      },
      {
        speaker: 'Agent',
        speakerIcon: '👩‍💼',
        textTarget: 'Let me check... Yes, seat 14A is open. Here is your boarding pass. Boarding begins at gate B7 in forty minutes.',
        textUz: 'Tekshirib ko\'ray... Ha, 14A o\'rindig\'i bo\'sh ekan. Mana sizning samolyotga chiqish taloningiz. Chiqish B7 darvozasida qirq daqiqada boshlanadi.'
      },
      {
        speaker: 'Traveler',
        speakerIcon: '🧳',
        textTarget: 'Wonderful! Where is the security check area?',
        textUz: 'Ajoyib! Xavfsizlik nazorati joyi qayerda?'
      },
      {
        speaker: 'Agent',
        speakerIcon: '👩‍💼',
        textTarget: 'Go straight ahead and turn right at the duty-free shop. Have a wonderful and safe journey!',
        textUz: 'To\'g\'riga boring va bojsiz savdo do\'koni yonidan o\'ngga buriling. Ajoyib va xavfsiz parvoz tilayman!'
      }
    ],
    targetWords: ['Confirmation', 'Luggage', 'Carry-on', 'Boarding pass', 'Gate', 'Security', 'Journey']
  },

  {
    id: 'en_daily_market',
    title: 'At the Grocery Store & Market (Bozor va do\'konda xarid)',
    situationTarget: 'A shopper selects fresh produce, asks about discounts, and pays at the checkout counter.',
    situationUz: 'Xaridor yangi meva-sabzavotlarni tanlab, narx va chegirmalar haqida so\'rab xarid qilmoqda.',
    level: 'BEGINNER',
    dayIndex: 3,
    language: 'en',
    category: 'market',
    lines: [
      {
        speaker: 'Customer',
        speakerIcon: '🛒',
        textTarget: 'Excuse me! Are these organic apples fresh, and what is the price per kilogram?',
        textUz: 'Kechirasiz! Bu tabiiy olmadan yangimi va bir kilogrami qancha turadi?'
      },
      {
        speaker: 'Seller',
        speakerIcon: '👨‍🌾',
        textTarget: 'Yes, they arrived this morning from local orchards! They are three dollars a kilo, but if you buy two, you get twenty percent off.',
        textUz: 'Ha, ular bugun ertalab mahalliy bog\'lardan keltirildi! Bir kilosi uch dollar, lekin ikkita olsangiz yigirma foiz chegirma bor.'
      },
      {
        speaker: 'Customer',
        speakerIcon: '🛒',
        textTarget: 'That sounds like a great deal. I will also take two cartons of almond milk and whole grain bread.',
        textUz: 'Bu juda yaxshi taklif. Yana ikkita bodom suti va butun donli nondan ham olaman.'
      },
      {
        speaker: 'Cashier',
        speakerIcon: '👩',
        textTarget: 'Your total comes to fifteen dollars and fifty cents. Would you like a paper shopping bag?',
        textUz: 'Jami o\'n besh dollar-u ellik sent bo\'ldi. Qog\'oz xarid paketi kerakmi?'
      },
      {
        speaker: 'Customer',
        speakerIcon: '🛒',
        textTarget: 'Yes please. I brought my loyalty rewards card. Can I scan it here?',
        textUz: 'Ha, iltimos. Men doimiy mijoz kartamni ham olganman. Bu yerda skaner qilsam bo\'ladimi?'
      },
      {
        speaker: 'Cashier',
        speakerIcon: '👩',
        textTarget: 'Of course! You just earned fifty loyalty points. Thank you for shopping with us!',
        textUz: 'Albatta! Siz hozir ellikta bonus ball to\'pladingiz. Bizdan xarid qilganingiz uchun tashakkur!'
      }
    ],
    targetWords: ['Organic', 'Orchard', 'Discount', 'Deal', 'Loyalty card', 'Reward', 'Produce']
  },

  {
    id: 'en_daily_cafe',
    title: 'At the Cozy Café (Qahvaxona va tushlik buyurtmasi)',
    situationTarget: 'Two friends ordering coffee and delicious pastries at a cozy city coffee shop.',
    situationUz: 'Ikki do\'st qahvaxonada o\'tirib, qahva va pishiriqlar buyurtma qilmoqda.',
    level: 'BEGINNER',
    dayIndex: 4,
    language: 'en',
    category: 'cafe',
    lines: [
      {
        speaker: 'Barista',
        speakerIcon: '☕',
        textTarget: 'Welcome to Aroma Café! What can I brew for you today?',
        textUz: 'Aroma kafesiga xush kelibsiz! Bugun sizga qanday ichimlik tayyorlab beray?'
      },
      {
        speaker: 'Guest',
        speakerIcon: '👩',
        textTarget: 'Hi! Could I have an iced caramel latte with oat milk, and a double espresso for my colleague?',
        textUz: 'Salom! Menga suli sutli muzdek karamel latte, hamkasbimga esa qo\'shaloq espresso bera olasizmi?'
      },
      {
        speaker: 'Barista',
        speakerIcon: '☕',
        textTarget: 'Excellent choice! Would you like extra syrup, or less sugar?',
        textUz: 'Ajoyib tanlov! Qo\'shimcha sirop xohlaysizmi yoki kamroq shakar bilanmi?'
      },
      {
        speaker: 'Guest',
        speakerIcon: '👩',
        textTarget: 'Less sugar, please. Do you have any gluten-free pastries or fresh croissants today?',
        textUz: 'Kamroq shakar, iltimos. Bugun glyutensiz pishiriqlar yoki yangi krussan bormi?'
      },
      {
        speaker: 'Barista',
        speakerIcon: '☕',
        textTarget: 'Yes, our almond croissants just came out of the oven! They are delightfully warm and crisp.',
        textUz: 'Ha, bodomli krussanlarimiz hozirgina pechdan chiqdi! Ular juda issiq va qarsildoq.'
      },
      {
        speaker: 'Guest',
        speakerIcon: '👩',
        textTarget: 'We will take two of those! Can we sit at the patio table outside?',
        textUz: 'Unday bo\'lsa ikkita olamiz! Tashqaridagi ayvon stollarida o\'tirsak bo\'ladimi?'
      },
      {
        speaker: 'Barista',
        speakerIcon: '☕',
        textTarget: 'Absolutely! I will bring your order over as soon as it is ready. Enjoy your break!',
        textUz: 'Albatta! Buyurtmangiz tayyor bo\'lishi bilan yetkazib beraman. Yoqimli dam oling!'
      }
    ],
    targetWords: ['Brew', 'Pastry', 'Delightfully', 'Crisp', 'Patio', 'Colleague', 'Croissant']
  },

  {
    id: 'en_daily_pharmacy',
    title: 'At the Pharmacy (Dorixonada salomatlik va maslahat)',
    situationTarget: 'A customer visits a local pharmacy seeking relief for a headache and asking for vitamins.',
    situationUz: 'Mijoz dorixonaga kirib, bosh og\'rig\'iga dori va immunitet uchun vitamin so\'ramoqda.',
    level: 'BEGINNER',
    dayIndex: 5,
    language: 'en',
    category: 'pharmacy',
    lines: [
      {
        speaker: 'Customer',
        speakerIcon: '🤒',
        textTarget: 'Hello! I have had a severe migraine and slight fever since yesterday. Can you recommend something effective?',
        textUz: 'Salom! Kechadan beri qattiq bosh og\'rig\'i va ozgina isitmam bor. Biror samarali dori tavsiya eta olasizmi?'
      },
      {
        speaker: 'Pharmacist',
        speakerIcon: '👩‍⚕️',
        textTarget: 'Good day! I am sorry to hear that. Do you have any known allergies or sensitive stomach conditions?',
        textUz: 'Xayrli kun! Afsusdaman. Sizda biror dori vositasiga allergiya yoki oshqozon sezuvchanligi bormi?'
      },
      {
        speaker: 'Customer',
        speakerIcon: '🤒',
        textTarget: 'No, no allergies. I just need quick relief so I can concentrate on my studies.',
        textUz: 'Yo\'q, allergiyam yo\'q. Faqat darslarimga diqqatimni jamlay olishim uchun tez yordam bersa bo\'ldi.'
      },
      {
        speaker: 'Pharmacist',
        speakerIcon: '👩‍⚕️',
        textTarget: 'These pain-relief tablets work within twenty minutes. Take one tablet after meals, twice a day, with plenty of water.',
        textUz: 'Ushbu og\'riq qoldiruvchi tabletkalar yigirma daqiqada ta\'sir qiladi. Ovqatdan keyin kuniga ikki marta ko\'p suv bilan iching.'
      },
      {
        speaker: 'Customer',
        speakerIcon: '🤒',
        textTarget: 'Understood. Could you also recommend some Vitamin C or multivitamin supplements for energy?',
        textUz: 'Tushundim. Quvvat va immunitet uchun Vitamin C yoki multivitamin ham tavsiya qila olasizmi?'
      },
      {
        speaker: 'Pharmacist',
        speakerIcon: '👩‍⚕️',
        textTarget: 'Here is our bestselling mineral complex. It boosts your immune defense and reduces fatigue. Get well soon!',
        textUz: 'Mana bizning eng xaridorgir mineral kompleksimiz. U immunitetni kuchaytiradi va charchoqni ketkazadi. Tezroq tuzalib keting!'
      }
    ],
    targetWords: ['Migraine', 'Fever', 'Allergy', 'Relief', 'Concentrate', 'Supplement', 'Immune', 'Fatigue']
  },

  {
    id: 'en_daily_combo',
    title: 'A Full Day in Town (Birlashgan Hayotiy Katta Dialog)',
    situationTarget: 'Combining vocabulary from Taxi, Airport, Market, Café and Pharmacy into a connected real-life story.',
    situationUz: 'Taksi, Aeroport, Bozor, Kafe va Dorixona so\'zlarini birlashtirgan katta hayotiy muloqot.',
    level: 'BEGINNER',
    dayIndex: 6,
    language: 'en',
    category: 'combo',
    lines: [
      {
        speaker: 'Mark',
        speakerIcon: '🧔',
        textTarget: 'What an eventful day! First we took a quick taxi through morning traffic, and now we are at this cozy café.',
        textUz: 'Qanday sermazmun kun! Avvaliga ertalabki tirbandlikda taksida keldik, hozir esa mana bu shinam qahvaxonadamiz.'
      },
      {
        speaker: 'Lisa',
        speakerIcon: '👩',
        textTarget: 'Yes! After our breakfast croissants, we should stop by the grocery market to buy fresh produce and bottled water.',
        textUz: 'Ha! Nonushta krussanlaridan so\'ng, yangi mevalar va suv sotib olish uchun bozorga kirib o\'tishimiz kerak.'
      },
      {
        speaker: 'Mark',
        speakerIcon: '🧔',
        textTarget: 'Don\'t forget we need to visit the pharmacy too. I need those vitamin supplements before our long flight tomorrow.',
        textUz: 'Dorixonaga kirishni ham esdan chiqarma. Ertangi uzoq parvozimizdan oldin vitaminlar olishim kerak.'
      },
      {
        speaker: 'Lisa',
        speakerIcon: '👩',
        textTarget: 'Right! I already checked our flight confirmation and baggage luggage limits online. We are completely prepared!',
        textUz: 'To\'g\'ri! Men internet orqali reys tasdig\'i va bagaj me\'yorlarini tekshirib qo\'ydim. Biz to\'liq tayyormiz!'
      },
      {
        speaker: 'Mark',
        speakerIcon: '🧔',
        textTarget: 'Fantastic! Learning practical words and applying them in everyday situations makes speaking so effortless and natural.',
        textUz: 'Qoyilmaqom! Amaliy so\'zlarni o\'rganib, ularni kundalik vaziyatlarda qo\'llash erkin muloqot qilishni shunchalik osonlashtirar ekan.'
      }
    ],
    targetWords: ['Traffic', 'Croissant', 'Produce', 'Pharmacy', 'Confirmation', 'Luggage', 'Prepared', 'Effortless']
  },

  // =========================================================================
  // 🇷🇺 RUS TILI KUNDALIK HAYOTIY DIALOGLARI (5 MAVZU + 1 COMBO)
  // =========================================================================
  {
    id: 'ru_daily_taxi',
    title: 'Поездка на городском такси (Taksida yo\'l yurish)',
    situationTarget: 'Пассажир садится в такси, называет адрес и просит водителя успеть до отправления поезда.',
    situationUz: 'Yo\'lovchi taksiga o\'tirib, manzilni aytmoqda va poyezd jo\'naguncha yetib borishni so\'ramoqda.',
    level: 'BEGINNER',
    dayIndex: 1,
    language: 'ru',
    category: 'taxi',
    lines: [
      {
        speaker: 'Пассажир',
        speakerIcon: '👨',
        textTarget: 'Добрый день! До Центрального вокзала, пожалуйста. Мы успеем за полчаса?',
        textUz: 'Xayrli kun! Markaziy vokzalgacha, iltimos. Yarim soatda ulguramizmi?'
      },
      {
        speaker: 'Водитель',
        speakerIcon: '🚕',
        textTarget: 'Здравствуйте! Садитесь, пожалуйста. Сейчас на мосту небольшая пробка, но мы поедем в объезд.',
        textUz: 'Assalomu alaykum! O\'tiring, iltimos. Hozir ko\'prikda kichik tirbandlik bor, ammo aylanma yo\'ldan boramiz.'
      },
      {
        speaker: 'Пассажир',
        speakerIcon: '👨',
        textTarget: 'Отлично! Мой поезд отправляется через сорок минут. Не хочется опаздывать.',
        textUz: 'Ajoyib! Poyezdim qirq daqiqada jo\'nab ketadi. Kechikishni xohlamayman.'
      },
      {
        speaker: 'Водитель',
        speakerIcon: '🚕',
        textTarget: 'Не волнуйтесь, доедем быстро и безопасно. Пожалуйста, пристегните ремень безопасности.',
        textUz: 'Xavotir olmang, tez va xavfsiz yetib boramiz. Iltimos, xavfsizlik kamarini taqing.'
      },
      {
        speaker: 'Пассажир',
        speakerIcon: '👨',
        textTarget: 'Вот мы и приехали! Остановите, пожалуйста, у главного входа. Можно оплатить картой?',
        textUz: 'Mana yetib keldik! Iltimos, bosh kirish eshigi yonida to\'xtating. Karta orqali to\'lasam bo\'ladimi?'
      },
      {
        speaker: 'Водитель',
        speakerIcon: '🚕',
        textTarget: 'Да, конечно, приложите карту к терминалу. С вас триста рублей. Спасибо, счастливого пути!',
        textUz: 'Ha, albatta, kartani terminalga tekkizing. Sizdan uch yuz rubl. Rahmat, oq yo\'l!'
      }
    ],
    targetWords: ['Вокзал', 'Пробка', 'Объезд', 'Отправляется', 'Безопасно', 'Ремень', 'Счастливого пути']
  },

  {
    id: 'ru_daily_travel',
    title: 'В аэропорту и в отеле (Aeroport va Sayohat)',
    situationTarget: 'Путешественник регистрируется на авиарейс и уточняет детали багажа.',
    situationUz: 'Sayohatchi aeroportda ro\'yxatdan o\'tib, bagaj va joylashuv haqida gaplashmoqda.',
    level: 'BEGINNER',
    dayIndex: 2,
    language: 'ru',
    category: 'travel',
    lines: [
      {
        speaker: 'Пассажир',
        speakerIcon: '🧳',
        textTarget: 'Здравствуйте! Вот мой паспорт и электронный билет на рейс в Санкт-Петербург.',
        textUz: 'Assalomu alaykum! Mana mening pasportim va Sankt-Peterburgga bo\'lgan reys elektron chiptasi.'
      },
      {
        speaker: 'Сотрудник',
        speakerIcon: '👩‍💼',
        textTarget: 'Добрый день! Поставьте, пожалуйста, ваш чемодан на весы. Есть ли у вас ручная кладь?',
        textUz: 'Xayrli kun! Iltimos, chamadoningizni taroziga qo\'ying. Sizda qo\'l yuki bormi?'
      },
      {
        speaker: 'Пассажир',
        speakerIcon: '🧳',
        textTarget: 'Да, у меня только небольшой рюкзак. Можно место около окна?',
        textUz: 'Ha, menda faqat kichik ryukzak bor. Oyna yonidan joy olsa bo\'ladimi?'
      },
      {
        speaker: 'Сотрудник',
        speakerIcon: '👩‍💼',
        textTarget: 'Конечно! Ваше место 12А. Вот ваш посадочный талон. Выход на посадку номер семь.',
        textUz: 'Albatta! Sizning o\'rningiz 12A. Mana samolyotga chiqish taloningiz. Chiqish yettinchi darvozada.'
      },
      {
        speaker: 'Пассажир',
        speakerIcon: '🧳',
        textTarget: 'Большое спасибо! Подскажите, где находится зона таможенного и паспортного контроля?',
        textUz: 'Katta rahmat! Ayting-chi, bojxona va pasport nazorati hududi qayerda joylashgan?'
      },
      {
        speaker: 'Сотрудник',
        speakerIcon: '👩‍💼',
        textTarget: 'Прямо по коридору и налево. Желаем вам приятного полёта и мягкой посадки!',
        textUz: 'Yo\'lak bo\'ylab to\'g\'riga va chapga. Yoqimli parvoz va yengil qo\'nish tilaymiz!'
      }
    ],
    targetWords: ['Билет', 'Чемодан', 'Ручная кладь', 'Посадочный талон', 'Выход', 'Контроль', 'Посадка']
  },

  {
    id: 'ru_daily_market',
    title: 'В супермаркете и на рынке (Do\'konda xarid)',
    situationTarget: 'Покупатель выбирает свежие продукты, интересуется скидками и оплачивает покупки на кассе.',
    situationUz: 'Xaridor yangi mahsulotlarni tanlab, chegirmalarni so\'rab, kassada to\'lov qilmoqda.',
    level: 'BEGINNER',
    dayIndex: 3,
    language: 'ru',
    category: 'market',
    lines: [
      {
        speaker: 'Покупатель',
        speakerIcon: '🛒',
        textTarget: 'Здравствуйте! Скажите, пожалуйста, где у вас свежие молочные продукты и натуральный сок?',
        textUz: 'Assalomu alaykum! Ayting-chi, iltimos, yangi sut mahsulotlari va tabiiy sharbat qayerda?'
      },
      {
        speaker: 'Продавец',
        speakerIcon: '👩',
        textTarget: 'Добрый день! Молочный отдел во втором ряду. Сегодня у нас действует отличная скидка на сыр и творог.',
        textUz: 'Xayrli kun! Sut bo\'limi ikkinchi qatorda. Bugun bizda pishloq va tvorokka ajoyib chegirma bor.'
      },
      {
        speaker: 'Покупатель',
        speakerIcon: '🛒',
        textTarget: 'Замечательно! Взвесьте мне, пожалуйста, ещё килограмм спелых яблок и свежий хлеб.',
        textUz: 'Ajoyib! Menga yana bir kilogramm pishgan olma va issiq nondan ham tortib bersangiz, iltimos.'
      },
      {
        speaker: 'Кассир',
        speakerIcon: '👩‍💼',
        textTarget: 'Сумма вашей покупки — четыреста пятьдесят рублей. Вам понадобится фирменный пакет?',
        textUz: 'Xaridingiz summasi — to\'rt yuz ellik rubl bo\'ldi. Sizga maxsus paket kerakmi?'
      },
      {
        speaker: 'Покупатель',
        speakerIcon: '🛒',
        textTarget: 'Да, средний пакет, пожалуйста. У меня есть скидочная карта постоянного покупателя.',
        textUz: 'Ha, o\'rtacha paket, iltimos. Menda doimiy xaridor chegirma kartasi ham bor.'
      },
      {
        speaker: 'Кассир',
        speakerIcon: '👩‍💼',
        textTarget: 'Скидка начислена! Вот ваш фискальный чек и сдача. Спасибо за визит, приходите снова!',
        textUz: 'Chegirma hisoblandi! Mana kassa chekingiz va qaytimingiz. Tashrifingiz uchun rahmat, yana keling!'
      }
    ],
    targetWords: ['Продукты', 'Отдел', 'Скидка', 'Взвесьте', 'Сумма', 'Пакет', 'Чек', 'Сдача']
  },

  {
    id: 'ru_daily_cafe',
    title: 'В уютном кафе и заказ еды (Kafe va taom buyurtmasi)',
    situationTarget: 'Гости заказывают ароматный кофе, десерты и горячие блюда в городском ресторане.',
    situationUz: 'Mehmonlar restoranda xushbo\'y qahva, shirinliklar va issiq taomlar buyurtma qilmoqda.',
    level: 'BEGINNER',
    dayIndex: 4,
    language: 'ru',
    category: 'cafe',
    lines: [
      {
        speaker: 'Официант',
        speakerIcon: '🤵',
        textTarget: 'Добрый вечер! Рады видеть вас в нашем заведении. Вот меню, готовы сделать заказ?',
        textUz: 'Xayrli kech! Muassasamizda ko\'rib turganimizdan xursandmiz. Mana menyu, buyurtma berishga tayyormisiz?'
      },
      {
        speaker: 'Гость',
        speakerIcon: '👩',
        textTarget: 'Здравствуйте! Посоветуйте, пожалуйста, фирменное горячее блюдо и безалкогольный напиток.',
        textUz: 'Assalomu alaykum! Iltimos, muassasangizning maxsus issiq taomini va yaxna ichimlik tavsiya qiling.'
      },
      {
        speaker: 'Официант',
        speakerIcon: '🤵',
        textTarget: 'Рекомендую запечённую с травами рыбу или нежный сливочный суп. А из напитков — ягодный лимонад.',
        textUz: 'O\'tlar bilan pishirilgan baliq yoki qaymoqli sho\'rvani tavsiya qilaman. Ichimliklardan esa rezavorli limonad.'
      },
      {
        speaker: 'Гость',
        speakerIcon: '👩',
        textTarget: 'Звучит аппетитно! Принесите суп, рыбу и чашку зелёного жасминового чая.',
        textUz: 'Juda ishtahani ochar eshitildi! Sho\'rva, baliq va bir finjon yashil yasminli choy keltiring.'
      },
      {
        speaker: 'Официант',
        speakerIcon: '🤵',
        textTarget: 'Прекрасный выбор! Время ожидания составит около пятнадцати минут. Приятного аппетита!',
        textUz: 'Ajoyib tanlov! Kutish vaqti o\'n besh daqiqani tashkil etadi. Yoqimli ishtaha!'
      },
      {
        speaker: 'Гость',
        speakerIcon: '👩',
        textTarget: 'Благодарю вас! Всё было исключительно вкусно. Можно счёт, пожалуйста?',
        textUz: 'Tashakkur! Barchasi o\'ta mazali bo\'ldi. Hisobni keltira olasizmi, iltimos?'
      }
    ],
    targetWords: ['Заказ', 'Меню', 'Фирменное', 'Блюдо', 'Напиток', 'Аппетит', 'Счёт', 'Вкусно']
  },

  {
    id: 'ru_daily_pharmacy',
    title: 'В аптеке и забота о здоровье (Dorixona va salomatlik)',
    situationTarget: 'Посетитель обращается к фармацевту за советом от простуды и подбирает витаминный комплекс.',
    situationUz: 'Xaridor dorixonada shamollashga qarshi maslahat so\'rab, vitamin kompleksini tanlamoqda.',
    level: 'BEGINNER',
    dayIndex: 5,
    language: 'ru',
    category: 'pharmacy',
    lines: [
      {
        speaker: 'Покупатель',
        speakerIcon: '🤒',
        textTarget: 'Здравствуйте! У меня с утра болит горло, насморк и лёгкая температура. Что вы посоветуете?',
        textUz: 'Assalomu alaykum! Ertalabdan beri tomog\'im og\'riyapti, tumov va biroz isitma bor. Nima tavsiya qilasiz?'
      },
      {
        speaker: 'Фармацевт',
        speakerIcon: '👩‍⚕️',
        textTarget: 'Добрый день! Возьмите спрей для горла с антисептиком и растворимый горячий чай от симптомов простуды.',
        textUz: 'Xayrli kun! Tomoq uchun antiseptikli sprey va shamollash alomatlariga qarshi eriydigan issiq choy oling.'
      },
      {
        speaker: 'Покупатель',
        speakerIcon: '🤒',
        textTarget: 'Как правильно принимать эти лекарства? Нужен ли рецепт от врача?',
        textUz: 'Bu dorilarni qanday to\'g\'ri qabul qilish kerak? Shifokor retsepti kerakmi?'
      },
      {
        speaker: 'Фармацевт',
        speakerIcon: '👩‍⚕️',
        textTarget: 'Это безрецептурные средства. Порошок растворите в тёплой воде трижды в день после еды. Пейте больше жидкости.',
        textUz: 'Bular retseptsiz beriladigan dorilar. Kukunni iliq suvda ovqatdan keyin kuniga 3 mahal iching. Ko\'p suyuqlik iste\'mol qiling.'
      },
      {
        speaker: 'Покупатель',
        speakerIcon: '🤒',
        textTarget: 'Понял. Порекомендуйте ещё витамины для укрепления иммунитета и бодрости.',
        textUz: 'Tushundim. Immunitetni mustahkamlash va tetiklik uchun vitamin ham tavsiya qiling.'
      },
      {
        speaker: 'Фармацевт',
        speakerIcon: '👩‍⚕️',
        textTarget: 'Вот отличный комплекс с витамином C, D3 и цинком. Принимайте по одной капсуле утром. Выздоравливайте скорей!',
        textUz: 'Mana vitamin C, D3 va rux moddali ajoyib kompleks. Ertalab bitta kapsuladan ichasiz. Tezroq shifo toping!'
      }
    ],
    targetWords: ['Горло', 'Насморк', 'Температура', 'Спрей', 'Лекарство', 'Рецепт', 'Иммунитет', 'Выздоравливайте']
  },

  {
    id: 'ru_daily_combo',
    title: 'Насыщенный день в городе (Birlashgan Hayotiy Katta Dialog)',
    situationTarget: 'Объединение словарного запаса из Такси, Аэропорта, Магазина, Кафе и Аптеки в единый живой рассказ.',
    situationUz: 'Taksi, Aeroport, Do\'kon, Kafe va Dorixonadagi barcha so\'zlarni birlashtirgan hayotiy yakuniy suhbat.',
    level: 'BEGINNER',
    dayIndex: 6,
    language: 'ru',
    category: 'combo',
    lines: [
      {
        speaker: 'Алексей',
        speakerIcon: '🧔',
        textTarget: 'Какой продуктивный день! Утром мы вызвали такси, объехали пробку и вовремя решили все вопросы.',
        textUz: 'Qanday unumli kun bo\'ldi! Ertalab taksi chaqirib, tirbandlikni aylanib o\'tdik va barcha ishlarni vaqtida hal qildik.'
      },
      {
        speaker: 'Ольга',
        speakerIcon: '👩',
        textTarget: 'Да! А потом выпили чудесный кофе в кафе, зашли в супермаркет за свежими продуктами и проверили билет.',
        textUz: 'Ha! Keyin kafeda ajoyib qahva ichdik, yangi mahsulotlar uchun supermarketga kirdik va chiptamizni tekshirib oldik.'
      },
      {
        speaker: 'Алексей',
        speakerIcon: '🧔',
        textTarget: 'И главное — в аптеке купили полезные витамины перед долгой поездкой на поезде. Мы ко всему готовы!',
        textUz: 'Eng asosiysi — uzoq poyezd safaridan oldin dorixonadan foydali vitaminlar oldik. Biz hamma narsaga tayyormiz!'
      },
      {
        speaker: 'Ольга',
        speakerIcon: '👩',
        textTarget: 'Когда каждый день учишь живые слова по практическим темам, общаться на языке становится легко и естественно!',
        textUz: 'Har kuni amaliy mavzularda jonli so\'zlarni o\'rgansang, tilda muloqot qilish juda oson va tabiiy bo\'lib qolar ekan!'
      }
    ],
    targetWords: ['Такси', 'Пробка', 'Кафе', 'Супермаркет', 'Продукты', 'Билет', 'Аптека', 'Витамины', 'Поездка']
  }
];
