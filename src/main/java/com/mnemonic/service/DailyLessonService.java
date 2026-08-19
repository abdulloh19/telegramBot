package com.mnemonic.service;

import com.mnemonic.model.UserProfile;
import com.mnemonic.model.Word;
import com.mnemonic.repository.UserRepository;
import com.mnemonic.repository.WordRepository;

import java.util.List;

public class DailyLessonService {
    private final WordRepository wordRepository;
    private final UserRepository userRepository;
    private final StreakService streakService;

    public DailyLessonService(WordRepository wordRepository, UserRepository userRepository, StreakService streakService) {
        this.wordRepository = wordRepository;
        this.userRepository = userRepository;
        this.streakService = streakService;
    }

    public List<Word> getTodayWords(UserProfile profile) {
        return wordRepository.getWordsForDayAndLevel(profile.getCurrentDayIndex(), profile.getSelectedLevel());
    }

    public Word getCurrentWord(UserProfile profile) {
        List<Word> todayWords = getTodayWords(profile);
        int index = Math.max(0, Math.min(profile.getCurrentWordInDay(), todayWords.size() - 1));
        return todayWords.get(index);
    }

    /**
     * Kunlik 20 ta so'z darsi uchun chiroyli formatlangan xabar matnini qaytaradi
     */
    public String formatLessonCard(UserProfile profile) {
        List<Word> todayWords = getTodayWords(profile);
        int currentIndex = profile.getCurrentWordInDay();
        int total = todayWords.size();
        Word word = todayWords.get(currentIndex);
        String levelName = profile.getSelectedLevel() != null ? profile.getSelectedLevel().getDisplayName() : "Umumiy";

        StringBuilder sb = new StringBuilder();
        sb.append("📅 <b>KUNLIK DARS: ").append(profile.getCurrentDayIndex()).append("-KUN</b> (").append(levelName).append(")\n");
        sb.append("📊 <b>Jarayon:</b> ").append(currentIndex + 1).append(" / ").append(total).append(" ta so'z\n");
        sb.append(generateProgressBar(currentIndex + 1, total)).append("\n\n");
        sb.append(word.toFormattedCard());

        return sb.toString();
    }

    public void nextWord(UserProfile profile) {
        List<Word> todayWords = getTodayWords(profile);
        if (profile.getCurrentWordInDay() < todayWords.size() - 1) {
            profile.setCurrentWordInDay(profile.getCurrentWordInDay() + 1);
            userRepository.save(profile);
        }
    }

    public void previousWord(UserProfile profile) {
        if (profile.getCurrentWordInDay() > 0) {
            profile.setCurrentWordInDay(profile.getCurrentWordInDay() - 1);
            userRepository.save(profile);
        }
    }

    public StreakService.StreakResult completeTodayLesson(UserProfile profile) {
        profile.setTodayLessonCompleted(true);
        profile.setTotalWordsLearned(profile.getTotalWordsLearned() + WordRepository.WORDS_PER_DAY);
        profile.setCurrentWordInDay(0);
        profile.setCurrentDayIndex(profile.getCurrentDayIndex() + 1);

        StreakService.StreakResult result = streakService.recordDailyActivity(profile);
        userRepository.save(profile);
        return result;
    }

    private String generateProgressBar(int current, int total) {
        int barLength = 10;
        int filled = (int) Math.round(((double) current / total) * barLength);
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < barLength; i++) {
            if (i < filled) sb.append("🟩");
            else sb.append("⬜");
        }
        sb.append("] ").append(Math.round(((double) current / total) * 100)).append("%");
        return sb.toString();
    }
}
