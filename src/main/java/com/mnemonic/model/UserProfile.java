package com.mnemonic.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public class UserProfile {
    private long chatId;
    private String firstName;
    private TargetLanguage targetLanguage = null; // null bo'lsa kirganda dastlab til tanlanadi
    private WordLevel selectedLevel; // null bo'lsa til tanlangach so'raladi

    private int currentStreak = 0;
    private int maxStreak = 0;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate lastActiveDate;

    private int currentDayIndex = 1;
    private int currentWordInDay = 0;
    private boolean todayLessonCompleted = false;
    private boolean todayExerciseCompleted = false;

    private int totalWordsLearned = 0;
    private int totalWordsLearnedRu = 0;
    private int totalWordsLearnedEn = 0;
    private int totalExercisesCompleted = 0;
    private int totalQuizCorrect = 0;
    private int totalQuizCount = 0;

    private boolean reminderEnabled = true;
    private int reminderHour = 20; // Default: 20:00

    private int botVersion = 0; // Tizim yangilanganda /start majburiyligini tekshirish

    private String username; // Telegram @username

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate lastReminderDate; // Oxirgi eslatma yuborilgan sana

    private boolean enteredAfterReminder = false; // Eslatmadan so'ng kirganmi?
    private String lastName; // Telegram familiyasi
    private String phoneNumber; // Telefon raqami

    // Dialog progress — foydalanuvchi oxirgi ko'rgan dialog ID va mavzu indeksi
    private String currentDialogueId = null;    // Masalan: "en_daily_taxi"
    private int currentDialogueTopicIndex = 0;  // 0-4 oralig'ida (5 ta mavzu)

    public UserProfile() {
    }

    public UserProfile(long chatId, String firstName) {
        this.chatId = chatId;
        this.firstName = firstName;
        this.targetLanguage = null; // Kirishda til tanlaydi
        this.selectedLevel = null; // Til tanlangach daraja tanlaydi
        this.currentStreak = 0;
        this.maxStreak = 0;
        this.currentDayIndex = 1;
        this.currentWordInDay = 0;
        this.reminderEnabled = true;
        this.reminderHour = 20;
    }

    public long getChatId() {
        return chatId;
    }

    public void setChatId(long chatId) {
        this.chatId = chatId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public TargetLanguage getTargetLanguage() {
        return targetLanguage;
    }

    public void setTargetLanguage(TargetLanguage targetLanguage) {
        this.targetLanguage = targetLanguage;
    }

    public WordLevel getSelectedLevel() {
        return selectedLevel;
    }

    public void setSelectedLevel(WordLevel selectedLevel) {
        this.selectedLevel = selectedLevel;
    }

    public int getCurrentStreak() {
        return currentStreak;
    }

    public void setCurrentStreak(int currentStreak) {
        this.currentStreak = currentStreak;
    }

    public int getMaxStreak() {
        return maxStreak;
    }

    public void setMaxStreak(int maxStreak) {
        this.maxStreak = maxStreak;
    }

    public LocalDate getLastActiveDate() {
        return lastActiveDate;
    }

    public void setLastActiveDate(LocalDate lastActiveDate) {
        this.lastActiveDate = lastActiveDate;
    }

    public int getCurrentDayIndex() {
        return currentDayIndex;
    }

    public void setCurrentDayIndex(int currentDayIndex) {
        this.currentDayIndex = currentDayIndex;
    }

    public int getCurrentWordInDay() {
        return currentWordInDay;
    }

    public void setCurrentWordInDay(int currentWordInDay) {
        this.currentWordInDay = currentWordInDay;
    }

    public boolean isTodayLessonCompleted() {
        return todayLessonCompleted;
    }

    public void setTodayLessonCompleted(boolean todayLessonCompleted) {
        this.todayLessonCompleted = todayLessonCompleted;
    }

    public boolean isTodayExerciseCompleted() {
        return todayExerciseCompleted;
    }

    public void setTodayExerciseCompleted(boolean todayExerciseCompleted) {
        this.todayExerciseCompleted = todayExerciseCompleted;
    }

    public int getTotalWordsLearned() {
        if (totalWordsLearned > 0) {
            return totalWordsLearned;
        }
        return getTotalWordsLearnedRu() + getTotalWordsLearnedEn();
    }

    public void setTotalWordsLearned(int totalWordsLearned) {
        this.totalWordsLearned = totalWordsLearned;
    }

    public int getTotalWordsLearnedRu() {
        if (totalWordsLearnedRu == 0 && targetLanguage == TargetLanguage.RUSSIAN && totalWordsLearned > 0) {
            return totalWordsLearned;
        }
        return totalWordsLearnedRu;
    }

    public void setTotalWordsLearnedRu(int totalWordsLearnedRu) {
        this.totalWordsLearnedRu = totalWordsLearnedRu;
    }

    public int getTotalWordsLearnedEn() {
        if (totalWordsLearnedEn == 0 && targetLanguage == TargetLanguage.ENGLISH && totalWordsLearned > 0) {
            return totalWordsLearned;
        }
        return totalWordsLearnedEn;
    }

    public void setTotalWordsLearnedEn(int totalWordsLearnedEn) {
        this.totalWordsLearnedEn = totalWordsLearnedEn;
    }

    public int getWordsLearnedByLanguage(TargetLanguage lang) {
        if (lang == TargetLanguage.RUSSIAN) {
            return getTotalWordsLearnedRu();
        } else {
            return getTotalWordsLearnedEn();
        }
    }

    public int getTotalExercisesCompleted() {
        return totalExercisesCompleted;
    }

    public void setTotalExercisesCompleted(int totalExercisesCompleted) {
        this.totalExercisesCompleted = totalExercisesCompleted;
    }

    public int getTotalQuizCorrect() {
        return totalQuizCorrect;
    }

    public void setTotalQuizCorrect(int totalQuizCorrect) {
        this.totalQuizCorrect = totalQuizCorrect;
    }

    public int getTotalQuizCount() {
        return totalQuizCount;
    }

    public void setTotalQuizCount(int totalQuizCount) {
        this.totalQuizCount = totalQuizCount;
    }

    public boolean isReminderEnabled() {
        return reminderEnabled;
    }

    public void setReminderEnabled(boolean reminderEnabled) {
        this.reminderEnabled = reminderEnabled;
    }

    public int getReminderHour() {
        return reminderHour;
    }

    public void setReminderHour(int reminderHour) {
        this.reminderHour = reminderHour;
    }

    public int getBotVersion() {
        return botVersion;
    }

    public void setBotVersion(int botVersion) {
        this.botVersion = botVersion;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public LocalDate getLastReminderDate() {
        return lastReminderDate;
    }

    public void setLastReminderDate(LocalDate lastReminderDate) {
        this.lastReminderDate = lastReminderDate;
    }

    public boolean isEnteredAfterReminder() {
        return enteredAfterReminder;
    }

    public void setEnteredAfterReminder(boolean enteredAfterReminder) {
        this.enteredAfterReminder = enteredAfterReminder;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCurrentDialogueId() {
        return currentDialogueId;
    }

    public void setCurrentDialogueId(String currentDialogueId) {
        this.currentDialogueId = currentDialogueId;
    }

    public int getCurrentDialogueTopicIndex() {
        return currentDialogueTopicIndex;
    }

    public void setCurrentDialogueTopicIndex(int currentDialogueTopicIndex) {
        this.currentDialogueTopicIndex = currentDialogueTopicIndex;
    }
}
