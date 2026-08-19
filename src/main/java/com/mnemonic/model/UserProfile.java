package com.mnemonic.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public class UserProfile {
    private long chatId;
    private String firstName;
    private WordLevel selectedLevel; // null bo'lsa kirganda so'raladi

    private int currentStreak = 0;
    private int maxStreak = 0;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate lastActiveDate;

    private int currentDayIndex = 1;
    private int currentWordInDay = 0;
    private boolean todayLessonCompleted = false;
    private boolean todayExerciseCompleted = false;

    private int totalWordsLearned = 0;
    private int totalExercisesCompleted = 0;
    private int totalQuizCorrect = 0;
    private int totalQuizCount = 0;

    private boolean reminderEnabled = true;
    private int reminderHour = 20; // Default: 20:00

    public UserProfile() {
    }

    public UserProfile(long chatId, String firstName) {
        this.chatId = chatId;
        this.firstName = firstName;
        this.selectedLevel = null; // Ilk kirganida darajasini tanlaydi
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
        return totalWordsLearned;
    }

    public void setTotalWordsLearned(int totalWordsLearned) {
        this.totalWordsLearned = totalWordsLearned;
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
}
