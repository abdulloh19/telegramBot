package com.mnemonic.service;

import com.mnemonic.model.DailyDialogue;
import com.mnemonic.model.TargetLanguage;
import com.mnemonic.model.UserProfile;
import com.mnemonic.model.WordLevel;

import java.util.*;

/**
 * Kunlik 5 xil hayotiy mavzudagi dialoglar (Taxi, Travel, Market, Café, Pharmacy)
 * va ularni birlashtiruvchi Combo dialoglarni boshqaruvchi servis (Ingliz va Rus tillari uchun).
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
        List<DailyDialogue> enList = new ArrayList<>();

        // 1. 🚕 TAXI
        enList.add(new DailyDialogue(
            "en_daily_taxi",
            "🚕 Taking a City Taxi (Taksida yo'l yurish)",
            "A passenger hails a taxi, gives directions to the train station, and discusses the route with the driver.",
            "Yo'lovchi taksi to'xtatib, haydovchiga vokzalga borish yo'lini tushuntirmoqda va to'lov qilmoqda.",
            WordLevel.BEGINNER,
            1,
            List.of(
                new DailyDialogue.DialogueLine("Passenger", "👨", "Good morning! Are you available? I need to go to the Central Train Station as soon as possible.", "Xayrli tong! Bo'shmisiz? Men imkon qadar tezroq Markaziy Vokzalga borishim kerak."),
                new DailyDialogue.DialogueLine("Driver", "🚕", "Hello! Yes, please get in. There is some morning traffic on the main avenue, but we can take a shortcut.", "Salom! Ha, marhamat o'tiring. Bosh shoh ko'chada ozgina tirbandlik bor, ammo qisqa yo'ldan ketamiz."),
                new DailyDialogue.DialogueLine("Passenger", "👨", "That is great! How long will the ride take? My train departs in thirty-five minutes.", "Juda soz! Yo'l qancha vaqt oladi? Poyezdim o'ttiz besh daqiqada jo'naydi."),
                new DailyDialogue.DialogueLine("Driver", "🚕", "Don't worry, we will arrive in about fifteen minutes. Please fasten your seatbelt.", "Xavotir olmang, o'n besh daqiqada yetib boramiz. Iltimos, xavfsizlik kamarini taqing."),
                new DailyDialogue.DialogueLine("Passenger", "👨", "Here we are! Could you please stop right in front of the main entrance? Can I pay with credit card?", "Mana yetib keldik! Bosh kirish eshigi oldida to'xtatsangiz. Plastik karta orqali to'lasam bo'ladimi?"),
                new DailyDialogue.DialogueLine("Driver", "🚕", "Certainly! Tap your card on the terminal. The total is eight dollars. Here is your receipt, have a pleasant trip!", "Albatta! Kartangizni terminalga tekkizing. Jami sakkiz dollar. Mana chekingiz, safaringiz xayrli bo'lsin!")
            ),
            List.of("Available", "Traffic", "Shortcut", "Depart", "Fasten", "Receipt", "Pleasant"),
            TargetLanguage.ENGLISH
        ));

        // 2. ✈️ TRAVEL
        enList.add(new DailyDialogue(
            "en_daily_travel",
            "✈️ Airport Check-in & Travel (Aeroport va Sayohat)",
            "A traveler checks in for a flight at the airport and asks about baggage allowances.",
            "Sayohatchi aeroportda reysga ro'yxatdan o'tib, bagaj va parvoz haqida so'ramoqda.",
            WordLevel.BEGINNER,
            2,
            List.of(
                new DailyDialogue.DialogueLine("Traveler", "🧳", "Good afternoon! Here is my passport and booking confirmation for flight BA 204 to London.", "Xayrli kun! Mana pasportim va London reysiga band qilingan chiptam."),
                new DailyDialogue.DialogueLine("Agent", "👩‍💼", "Welcome! Thank you. Are you checking any luggage today, or do you only have carry-on bags?", "Xush kelibsiz! Bugun biror yuk topshirasizmi yoki faqat qo'l yuki bormi?"),
                new DailyDialogue.DialogueLine("Traveler", "🧳", "I have one large suitcase to check and a small backpack. Would it be possible to get a window seat?", "Bitta katta chamadonim va kichik ryukzak bor. Oyna yonidan joy olsa bo'ladimi?"),
                new DailyDialogue.DialogueLine("Agent", "👩‍💼", "Let me check... Yes, seat 14A is open. Here is your boarding pass. Boarding begins at gate B7 in forty minutes.", "Tekshirib ko'ray... Ha, 14A bo'sh ekan. Mana chiqish taloningiz. Chiqish B7 darvozasida boshlanadi."),
                new DailyDialogue.DialogueLine("Traveler", "🧳", "Wonderful! Where is the security check area?", "Ajoyib! Xavfsizlik nazorati joyi qayerda?"),
                new DailyDialogue.DialogueLine("Agent", "👩‍💼", "Go straight ahead and turn right at the duty-free shop. Have a safe journey!", "To'g'riga boring va do'kon yonidan o'ngga buriling. Xavfsiz parvoz tilayman!")
            ),
            List.of("Confirmation", "Luggage", "Carry-on", "Boarding pass", "Gate", "Security", "Journey"),
            TargetLanguage.ENGLISH
        ));

        // 3. 🛒 MARKET
        enList.add(new DailyDialogue(
            "en_daily_market",
            "🛒 At the Grocery Store & Market (Bozor va do'konda xarid)",
            "A shopper selects fresh produce, asks about discounts, and pays at the checkout counter.",
            "Xaridor yangi meva-sabzavotlarni tanlab, narx va chegirmalar haqida so'rab xarid qilmoqda.",
            WordLevel.BEGINNER,
            3,
            List.of(
                new DailyDialogue.DialogueLine("Customer", "🛒", "Excuse me! Are these organic apples fresh, and what is the price per kilogram?", "Kechirasiz! Bu olmadan yangimi va bir kilogrami qancha turadi?"),
                new DailyDialogue.DialogueLine("Seller", "👨‍🌾", "Yes, they arrived this morning! Three dollars a kilo, but if you buy two, you get twenty percent off.", "Ha, bugun ertalab keldi! Kilosi 3 dollar, ikkita olsangiz 20% chegirma bor."),
                new DailyDialogue.DialogueLine("Customer", "🛒", "That sounds like a great deal. I will also take two cartons of almond milk and whole grain bread.", "Juda yaxshi taklif. Yana ikkita bodom suti va nondan ham olaman."),
                new DailyDialogue.DialogueLine("Cashier", "👩", "Your total comes to fifteen dollars and fifty cents. Would you like a shopping bag?", "Jami 15 dollar 50 sent bo'ldi. Xarid paketi kerakmi?"),
                new DailyDialogue.DialogueLine("Customer", "🛒", "Yes please. I brought my loyalty rewards card. Can I scan it here?", "Ha, iltimos. Menda doimiy mijoz kartasi bor, skaner qilsam bo'ladimi?"),
                new DailyDialogue.DialogueLine("Cashier", "👩", "Of course! You just earned fifty loyalty points. Thank you for shopping with us!", "Albatta! Siz 50 bonus ball to'pladingiz. Bizdan xarid qilganingiz uchun tashakkur!")
            ),
            List.of("Organic", "Discount", "Deal", "Loyalty card", "Reward", "Produce"),
            TargetLanguage.ENGLISH
        ));

        // 4. ☕ CAFÉ
        enList.add(new DailyDialogue(
            "en_daily_cafe",
            "☕ At the Cozy Café (Qahvaxona va tushlik buyurtmasi)",
            "Two friends ordering coffee and delicious pastries at a cozy city coffee shop.",
            "Ikki do'st qahvaxonada o'tirib, qahva va pishiriqlar buyurtma qilmoqda.",
            WordLevel.BEGINNER,
            4,
            List.of(
                new DailyDialogue.DialogueLine("Barista", "☕", "Welcome to Aroma Café! What can I brew for you today?", "Aroma kafesiga xush kelibsiz! Bugun nima tayyorlab beray?"),
                new DailyDialogue.DialogueLine("Guest", "👩", "Hi! Could I have an iced caramel latte with oat milk, and a double espresso for my colleague?", "Salom! Menga muzdek karamel latte, hamkasbimga esa espresso bera olasizmi?"),
                new DailyDialogue.DialogueLine("Barista", "☕", "Excellent choice! Would you like extra syrup, or less sugar?", "Ajoyib tanlov! Qo'shimcha sirop xohlaysizmi yoki kamroq shakar bilanmi?"),
                new DailyDialogue.DialogueLine("Guest", "👩", "Less sugar, please. Do you have any fresh almond croissants today?", "Kamroq shakar, iltimos. Bugun yangi bodomli krussanlar bormi?"),
                new DailyDialogue.DialogueLine("Barista", "☕", "Yes, they just came out of the oven! They are delightfully warm and crisp.", "Ha, hozirgina pechdan chiqdi! Juda issiq va qarsildoq."),
                new DailyDialogue.DialogueLine("Guest", "👩", "We will take two of those! Can we sit at the patio table outside?", "Ikkita olamiz! Tashqaridagi stollarda o'tirsak bo'ladimi?"),
                new DailyDialogue.DialogueLine("Barista", "☕", "Absolutely! I will bring your order over as soon as it is ready. Enjoy your break!", "Albatta! Buyurtmangizni darhol yetkazaman. Yoqimli dam oling!")
            ),
            List.of("Brew", "Pastry", "Delightfully", "Crisp", "Patio", "Colleague", "Croissant"),
            TargetLanguage.ENGLISH
        ));

        // 5. 💊 PHARMACY
        enList.add(new DailyDialogue(
            "en_daily_pharmacy",
            "💊 At the Pharmacy & Health (Dorixonada salomatlik va maslahat)",
            "A customer visits a local pharmacy seeking relief for a headache and asking for vitamins.",
            "Mijoz dorixonaga kirib, bosh og'rig'iga dori va immunitet uchun vitamin so'ramoqda.",
            WordLevel.BEGINNER,
            5,
            List.of(
                new DailyDialogue.DialogueLine("Customer", "🤒", "Hello! I have had a severe migraine and slight fever since yesterday. Can you recommend something effective?", "Salom! Kechadan beri kuchli bosh og'rig'i va isitma bor. Samarali dori tavsiya eta olasizmi?"),
                new DailyDialogue.DialogueLine("Pharmacist", "👩‍⚕️", "Good day! Do you have any known allergies or sensitive stomach conditions?", "Xayrli kun! Biror dori vositasiga allergiyangiz bormi?"),
                new DailyDialogue.DialogueLine("Customer", "🤒", "No allergies. I just need quick relief so I can concentrate on my studies.", "Allergiyam yo'q. Darslarimga diqqatni jamlay olishim uchun tez yordam bersa bo'ldi."),
                new DailyDialogue.DialogueLine("Pharmacist", "👩‍⚕️", "These pain-relief tablets work within twenty minutes. Take one tablet after meals, twice a day, with water.", "Ushbu og'riq qoldiruvchi dori 20 daqiqada ta'sir qiladi. Ovqatdan so'ng suv bilan iching."),
                new DailyDialogue.DialogueLine("Customer", "🤒", "Understood. Could you also recommend some Vitamin C or multivitamin supplements for energy?", "Tushundim. Quvvat uchun Vitamin C yoki multivitamin ham bera olasizmi?"),
                new DailyDialogue.DialogueLine("Pharmacist", "👩‍⚕️", "Here is our bestselling mineral complex. It boosts your immune defense and reduces fatigue. Get well soon!", "Mana mineral kompleksimiz. U immunitetni kuchaytiradi va charchoqni ketkazadi. Tezroq tuzaling!")
            ),
            List.of("Migraine", "Fever", "Allergy", "Relief", "Concentrate", "Supplement", "Immune", "Fatigue"),
            TargetLanguage.ENGLISH
        ));

        // 6. 🌟 COMBO
        enList.add(new DailyDialogue(
            "en_daily_combo",
            "🌟 A Full Day in Town (Birlashgan Hayotiy Katta Dialog)",
            "Combining vocabulary from Taxi, Airport, Market, Café and Pharmacy into a connected real-life story.",
            "Taksi, Aeroport, Bozor, Kafe va Dorixona so'zlarini birlashtirgan katta hayotiy muloqot.",
            WordLevel.BEGINNER,
            6,
            List.of(
                new DailyDialogue.DialogueLine("Mark", "🧔", "What an eventful day! First we took a quick taxi through morning traffic, and now we are at this cozy café.", "Qanday sermazmun kun! Avval taksida keldik, hozir esa mana bu shinam qahvaxonadamiz."),
                new DailyDialogue.DialogueLine("Lisa", "👩", "Yes! After our breakfast croissants, we should stop by the grocery market to buy fresh produce and water.", "Ha! Nonushtadan so'ng mevalar va suv olish uchun bozorga kirib o'tishimiz kerak."),
                new DailyDialogue.DialogueLine("Mark", "🧔", "Don't forget we need to visit the pharmacy too. I need those vitamin supplements before our long flight tomorrow.", "Dorixonaga kirishni ham unutmang. Ertangi uzoq parvozimiz oldidan vitaminlar kerak."),
                new DailyDialogue.DialogueLine("Lisa", "👩", "Right! I already checked our flight confirmation and luggage limits online. We are completely prepared!", "To'g'ri! Reys tasdig'i va bagaj me'yorlarini tekshirib qo'ydim. Biz to'liq tayyormiz!"),
                new DailyDialogue.DialogueLine("Mark", "🧔", "Fantastic! Learning practical words and applying them in everyday situations makes speaking so natural.", "Qoyilmaqom! Amaliy so'zlarni kundalik vaziyatlarda qo'llash muloqotni shunchalik osonlashtiradi.")
            ),
            List.of("Traffic", "Croissant", "Produce", "Pharmacy", "Confirmation", "Luggage", "Prepared"),
            TargetLanguage.ENGLISH
        ));

        dialoguesByLangAndLevel.get(TargetLanguage.ENGLISH).put(WordLevel.BEGINNER, enList);
    }

    private void initRussianDialogues() {
        List<DailyDialogue> ruList = new ArrayList<>();

        // 1. 🚕 TAXI
        ruList.add(new DailyDialogue(
            "ru_daily_taxi",
            "🚕 Поездка на такси (Taksida yo'l yurish)",
            "Пассажир садится в такси, называет адрес и просит водителя успеть до отправления поезда.",
            "Yo'lovchi taksiga o'tirib, manzilni aytmoqda va poyezd jo'naguncha yetib borishni so'ramoqda.",
            WordLevel.BEGINNER,
            1,
            List.of(
                new DailyDialogue.DialogueLine("Пассажир", "👨", "Добрый день! До Центрального вокзала, пожалуйста. Мы успеем за полчаса?", "Xayrli kun! Markaziy vokzalgacha, iltimos. Yarim soatda ulguramizmi?"),
                new DailyDialogue.DialogueLine("Водитель", "🚕", "Здравствуйте! Садитесь, пожалуйста. Сейчас на мосту небольшая пробка, но мы поедем в объезд.", "Assalomu alaykum! O'tiring, iltimos. Ko'prikda tirbandlik bor, aylanma yo'ldan boramiz."),
                new DailyDialogue.DialogueLine("Пассажир", "👨", "Отлично! Мой поезд отправляется через сорок минут. Не хочется опаздывать.", "Ajoyib! Poyezdim 40 daqiqada jo'naydi. Kechikishni xohlamayman."),
                new DailyDialogue.DialogueLine("Водитель", "🚕", "Не волнуйтесь, доедем быстро и безопасно. Пожалуйста, пристегните ремень безопасности.", "Xavotir olmang, tez va xavfsiz yetib boramiz. Iltimos, xavfsizlik kamarini taqing."),
                new DailyDialogue.DialogueLine("Пассажир", "👨", "Вот мы и приехали! Остановите, пожалуйста, у главного входа. Можно оплатить картой?", "Mana yetib keldik! Bosh kirish eshigi yonida to'xtating. Karta orqali to'lasam bo'ladimi?"),
                new DailyDialogue.DialogueLine("Водитель", "🚕", "Да, конечно, приложите карту к терминалу. С вас триста рублей. Спасибо, счастливого пути!", "Ha, albatta, kartani tekkizing. Sizdan 300 rubl. Rahmat, oq yo'l!")
            ),
            List.of("Вокзал", "Пробка", "Объезд", "Отправляется", "Безопасно", "Ремень", "Счастливого пути"),
            TargetLanguage.RUSSIAN
        ));

        // 2. ✈️ TRAVEL
        ruList.add(new DailyDialogue(
            "ru_daily_travel",
            "✈️ В аэропорту и в отеле (Aeroport va Sayohat)",
            "Путешественник регистрируется на авиарейс и уточняет детали багажа.",
            "Sayohatchi aeroportda ro'yxatdan o'tib, bagaj va joylashuv haqida gaplashmoqda.",
            WordLevel.BEGINNER,
            2,
            List.of(
                new DailyDialogue.DialogueLine("Пассажир", "🧳", "Здравствуйте! Вот мой паспорт и электронный билет на рейс в Санкт-Петербург.", "Assalomu alaykum! Mana pasportim va Peterburgga bo'lgan reys chiptasi."),
                new DailyDialogue.DialogueLine("Сотрудник", "👩‍💼", "Добрый день! Поставьте, пожалуйста, ваш чемодан на весы. Есть ли у вас ручная кладь?", "Xayrli kun! Chamadoningizni taroziga qo'ying. Sizda qo'l yuki bormi?"),
                new DailyDialogue.DialogueLine("Пассажир", "🧳", "Да, у меня только небольшой рюкзак. Можно место около окна?", "Ha, menda faqat kichik ryukzak bor. Oyna yonidan joy olsa bo'ladimi?"),
                new DailyDialogue.DialogueLine("Сотрудник", "👩‍💼", "Конечно! Ваше место 12А. Вот ваш посадочный талон. Выход на посадку номер семь.", "Albatta! O'rningiz 12A. Mana chiqish taloningiz. Chiqish 7-darvozada."),
                new DailyDialogue.DialogueLine("Пассажир", "🧳", "Большое спасибо! Подскажите, где находится зона паспортного контроля?", "Katta rahmat! Pasport nazorati hududi qayerda joylashgan?"),
                new DailyDialogue.DialogueLine("Сотрудник", "👩‍💼", "Прямо по коридору и налево. Желаем вам приятного полёта и мягкой посадки!", "To'g'riga va chapga. Yoqimli parvoz va yengil qo'nish tilaymiz!")
            ),
            List.of("Билет", "Чемодан", "Ручная кладь", "Посадочный талон", "Выход", "Контроль", "Посадка"),
            TargetLanguage.RUSSIAN
        ));

        // 3. 🛒 MARKET
        ruList.add(new DailyDialogue(
            "ru_daily_market",
            "🛒 В супермаркете и на рынке (Do'konda xarid)",
            "Покупатель выбирает свежие продукты, интересуется скидками и оплачивает покупки на кассе.",
            "Xaridor yangi mahsulotlarni tanlab, chegirmalarni so'rab, kassada to'lov qilmoqda.",
            WordLevel.BEGINNER,
            3,
            List.of(
                new DailyDialogue.DialogueLine("Покупатель", "🛒", "Здравствуйте! Скажите, пожалуйста, где у вас свежие молочные продукты и сок?", "Assalomu alaykum! Yangi sut mahsulotlari va sharbat qayerda?"),
                new DailyDialogue.DialogueLine("Продавец", "👩", "Добрый день! Молочный отдел во втором ряду. Сегодня у нас отличная скидка на сыр.", "Xayrli kun! Sut bo'limi 2-qatorda. Bugun pishloqqa ajoyib chegirma bor."),
                new DailyDialogue.DialogueLine("Покупатель", "🛒", "Замечательно! Взвесьте мне, пожалуйста, ещё килограмм спелых яблок и хлеб.", "Ajoyib! Menga yana 1 kg olma va nondan ham tortib bering."),
                new DailyDialogue.DialogueLine("Кассир", "👩‍💼", "Сумма вашей покупки — четыреста пятьдесят рублей. Вам понадобится пакет?", "Xaridingiz summasi — 450 rubl bo'ldi. Sizga paket kerakmi?"),
                new DailyDialogue.DialogueLine("Покупатель", "🛒", "Да, пакет, пожалуйста. У меня есть скидочная карта постоянного покупателя.", "Ha, paket, iltimos. Menda doimiy xaridor chegirma kartasi bor."),
                new DailyDialogue.DialogueLine("Кассир", "👩‍💼", "Скидка начислена! Вот ваш чек и сдача. Спасибо за визит, приходите снова!", "Chegirma hisoblandi! Mana chek va qaytim. Rahmat, yana keling!")
            ),
            List.of("Продукты", "Отдел", "Скидка", "Взвесьте", "Сумма", "Пакет", "Чек", "Сдача"),
            TargetLanguage.RUSSIAN
        ));

        // 4. ☕ CAFÉ
        ruList.add(new DailyDialogue(
            "ru_daily_cafe",
            "☕ В уютном кафе (Kafe va taom buyurtmasi)",
            "Гости заказывают ароматный кофе, десерты и горячие блюда в городском ресторане.",
            "Mehmonlar restoranda xushbo'y qahva, shirinliklar va issiq taomlar buyurtma qilmoqda.",
            WordLevel.BEGINNER,
            4,
            List.of(
                new DailyDialogue.DialogueLine("Официант", "🤵", "Добрый вечер! Рады видеть вас в нашем заведении. Вот меню, готовы сделать заказ?", "Xayrli kech! Muassasamizda ko'rib turganimizdan xursandmiz. Mana menyu, buyurtma berasizmi?"),
                new DailyDialogue.DialogueLine("Гость", "👩", "Здравствуйте! Посоветуйте, пожалуйста, фирменное горячее блюдо и напиток.", "Assalomu alaykum! Maxsus issiq taomingiz va yaxna ichimlik tavsiya qiling."),
                new DailyDialogue.DialogueLine("Официант", "🤵", "Рекомендую запечённую рыбу или нежный сливочный суп. А из напитков — лимонад.", "Pishirilgan baliq yoki qaymoqli sho'rvani tavsiya qilaman. Ichimliklardan — limonad."),
                new DailyDialogue.DialogueLine("Гость", "👩", "Звучит аппетитно! Принесите суп, рыбу и чашку зелёного чая.", "Ishtahani ochar eshitildi! Sho'rva, baliq va bir finjon yashil choy keltiring."),
                new DailyDialogue.DialogueLine("Официант", "🤵", "Прекрасный выбор! Время ожидания составит около пятнадцати минут. Приятного аппетита!", "Ajoyib tanlov! Kutish vaqti 15 daqiqa. Yoqimli ishtaha!"),
                new DailyDialogue.DialogueLine("Гость", "👩", "Благодарю вас! Всё было исключительно вкусно. Можно счёт, пожалуйста?", "Tashakkur! Barchasi juda mazali bo'ldi. Hisobni keltira olasizmi?")
            ),
            List.of("Заказ", "Меню", "Фирменное", "Блюдо", "Напиток", "Аппетит", "Счёт", "Вкусно"),
            TargetLanguage.RUSSIAN
        ));

        // 5. 💊 PHARMACY
        ruList.add(new DailyDialogue(
            "ru_daily_pharmacy",
            "💊 В аптеке и забота о здоровье (Dorixona va salomatlik)",
            "Посетитель обращается к фармацевту за советом от простуды и подбирает витаминный комплекс.",
            "Xaridor dorixonada shamollashga qarshi maslahat so'rab, vitamin kompleksini tanlamoqda.",
            WordLevel.BEGINNER,
            5,
            List.of(
                new DailyDialogue.DialogueLine("Покупатель", "🤒", "Здравствуйте! У меня с утра болит горло, насморк и температура. Что вы посоветуете?", "Assalomu alaykum! Tomog'im og'riyapti, tumov va isitma bor. Nima tavsiya qilasiz?"),
                new DailyDialogue.DialogueLine("Фармацевт", "👩‍⚕️", "Добрый день! Возьмите спрей для горла с антисептиком и растворимый горячий чай.", "Xayrli kun! Tomoq uchun antiseptikli sprey va eruvchan issiq choy oling."),
                new DailyDialogue.DialogueLine("Покупатель", "🤒", "Как правильно принимать эти лекарства? Нужен ли рецепт?", "Bu dorilarni qanday qabul qilish kerak? Retsept kerakmi?"),
                new DailyDialogue.DialogueLine("Фармацевт", "👩‍⚕️", "Это безрецептурные средства. Порошок растворите в тёплой воде трижды в день после еды.", "Bular retseptsiz dorilar. Kukunni iliq suvda ovqatdan so'ng kuniga 3 mahal iching."),
                new DailyDialogue.DialogueLine("Покупатель", "🤒", "Понял. Порекомендуйте ещё витамины для укрепления иммунитета.", "Tushundim. Immunitetni mustahkamlash uchun vitamin ham bering."),
                new DailyDialogue.DialogueLine("Фармацевт", "👩‍⚕️", "Вот отличный комплекс с витамином C, D3 и цинком. Выздоравливайте скорей!", "Mana vitamin C, D3 va ruxli ajoyib kompleks. Tezroq shifo toping!")
            ),
            List.of("Горло", "Насморк", "Температура", "Спрей", "Лекарство", "Рецепт", "Иммунитет", "Выздоравливайте"),
            TargetLanguage.RUSSIAN
        ));

        // 6. 🌟 COMBO
        ruList.add(new DailyDialogue(
            "ru_daily_combo",
            "🌟 Насыщенный день в городе (Birlashgan Hayotiy Katta Dialog)",
            "Объединение словарного запаса из Такси, Аэропорта, Магазина, Кафе и Аптеки в единый живой рассказ.",
            "Taksi, Aeroport, Do'kon, Kafe va Dorixonadagi barcha so'zlarni birlashtirgan hayotiy yakuniy suhbat.",
            WordLevel.BEGINNER,
            6,
            List.of(
                new DailyDialogue.DialogueLine("Алексей", "🧔", "Какой продуктивный день! Утром мы вызвали такси, объехали пробку и решили все дела.", "Qanday unumli kun! Ertalab taksi chaqirib, tirbandlikni aylanib o'tdik va ishlarni hal qildik."),
                new DailyDialogue.DialogueLine("Ольга", "👩", "Да! А потом выпили кофе в кафе, зашли в супермаркет за свежими продуктами и проверили билет.", "Ha! Keyin kafeda qahva ichdik, supermarketga kirdik va chiptani tekshirdik."),
                new DailyDialogue.DialogueLine("Алексей", "🧔", "И главное — в аптеке купили полезные витамины перед поездкой на поезде. Мы готовы!", "Eng asosiysi — dorixonadan foydali vitaminlar oldik. Biz tayyormiz!"),
                new DailyDialogue.DialogueLine("Ольга", "👩", "Когда каждый день учишь живые слова по практическим темам, общаться становится легко!", "Har kuni amaliy mavzularda jonli so'zlarni o'rgansang, muloqot qilish juda osonlashadi!")
            ),
            List.of("Такси", "Пробка", "Кафе", "Супермаркет", "Продукты", "Билет", "Аптека", "Витамины", "Поездка"),
            TargetLanguage.RUSSIAN
        ));

        dialoguesByLangAndLevel.get(TargetLanguage.RUSSIAN).put(WordLevel.BEGINNER, ruList);
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

    public List<DailyDialogue> getDialoguesForLanguage(TargetLanguage lang) {
        List<DailyDialogue> res = new ArrayList<>();
        Map<WordLevel, List<DailyDialogue>> levelMap = dialoguesByLangAndLevel.get(lang);
        if (levelMap != null) {
            for (List<DailyDialogue> list : levelMap.values()) {
                res.addAll(list);
            }
        }
        return res;
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
