# 🧠 Ingliz Tili Mnemonika Telegram Boti (Java)

Bu Telegram boti foydalanuvchilarga ingliz tili so'zlarini quruq yodlash o'rniga **Mnemonika (assotsiatsiyalar, fonetik o'xshashliklar, jonli obrazlar va qiziqarli hikoyalar)** orqali 10 barobar tez va uzoq muddatga eslab qolishga yordam beradi.

---

## 🌟 Asosiy Imkoniyatlar (Features)

1. **🎲 Tasodifiy Mnemonik So'z (Random Word)**:
   - Inglizcha so'z va uning talaffuzi/transkripsiyasi
   - O'zbekcha aniq tarjimasi
   - 💡 **Mnemonik kalit so'z va obraz/hikoya**
   - 🇬🇧 & 🇺🇿 Misol gaplar va ularning tarjimasi
2. **📚 Darajalar bo'yicha saralash**:
   - 🟢 **Boshlang'ich (A1-A2)**: Oddiy va kundalik so'zlar
   - 🟡 **O'rta (B1-B2)**: Erkin muloqot va muhim so'zlar
   - 🔴 **Yuqori (C1-C2 / IELTS)**: Akademik va nufuzli so'zlar
3. **🎮 Interaktiv Viktorina (Quiz)**:
   - 4 ta variantli qiziqarli testlar (so'zning tarjimasi yoki uning mnemonik obrazini topish)
   - To'g'ri/noto'g'ri javob tekshiruvi va bir zumda mnemonikani eslatish
4. **🔍 Aqlli Qidiruv**:
   - Istalgan inglizcha yoki o'zbekcha so'zni yozib yuboring (masalan: `lucid`, `frugal`, `ikkilanmoq`), bot uning mnemonik kartochkasini chiqarib beradi.
5. **📊 Shaxsiy Statistika**:
   - Ko'rilgan so'zlar soni, testlardagi to'g'ri javoblar va aniqlik foizi.
6. **💡 Mnemonika Haqida Qo'llanma**:
   - Assotsiativ xotirani rivojlantirishning 3 ta oltin qoidasi.

---

## 📁 Loyiha Tuzilmasi (Project Structure)

```
tgbot/
├── pom.xml                                           # Maven bog'liqliklari
└── src/
    └── main/
        ├── java/
        │   └── com/
        │       └── mnemonic/
        │           ├── Main.java                     # Ishga tushirish (EntryPoint)
        │           ├── bot/
        │           │   └── EnglishMnemonicBot.java  # Telegram Bot mantiqi va boshqaruvi
        │           ├── model/
        │           │   ├── Word.java                 # Mnemonik so'z modeli
        │           │   ├── WordLevel.java            # Darajalar (A1-A2, B1-B2, C1-C2)
        │           │   └── QuizQuestion.java         # Test savoli modeli
        │           ├── repository/
        │           │   └── WordRepository.java       # Boyitilgan 28+ mnemonik so'zlar bazasi
        │           └── service/
        │               └── QuizService.java          # Viktorina generatsiya qilish xizmati
        └── resources/
            └── logback.xml                           # Loglar sozlamasi
```

---

## 🚀 Loyihani Ishga Tushirish (How to Run)

### 1. IntelliJ IDEA orqali:
1. IntelliJ IDEA dasturida ushbu `tgbot` papkasini oching (`File -> Open -> tgbot`).
2. Maven avtomatik tarzda `pom.xml` dagi kerakli kutubxonalarni (TelegramBots) yuklab oladi.
3. [Main.java](file:///c:/Users/parij/Desktop/modul-3/modul_4/tgbot/tgbot/src/main/java/com/mnemonic/Main.java) fayliga o'ting va yashil **Run ▶️** tugmasini bosing.
4. Telegram dasturida botingizga kiring va `/start` tugmasini bosing!

### 2. Bot Tokenini o'zgartirish (agar boshqa bot ochmoqchi bo'lsangiz):
- [Main.java](file:///c:/Users/parij/Desktop/modul-3/modul_4/tgbot/tgbot/src/main/java/com/mnemonic/Main.java) faylidagi `DEFAULT_BOT_TOKEN` va `DEFAULT_BOT_USERNAME` qiymatlarini o'zingizning Telegram `@BotFather` dan olgan ma'lumotlaringizga almashtiring yoki muhit o'zgaruvchisi (Environment Variable) sifatida `BOT_TOKEN` va `BOT_USERNAME` ni o'rnating.
