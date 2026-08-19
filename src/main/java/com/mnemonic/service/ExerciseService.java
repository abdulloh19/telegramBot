package com.mnemonic.service;

import com.mnemonic.model.Exercise;
import com.mnemonic.model.UserProfile;
import com.mnemonic.model.Word;
import com.mnemonic.repository.UserRepository;
import com.mnemonic.repository.WordRepository;

import java.util.*;

public class ExerciseService {
    private final WordRepository wordRepository;
    private final UserRepository userRepository;
    private final StreakService streakService;
    private final Random random = new Random();

    public static final int EXERCISES_PER_SESSION = 5;

    public ExerciseService(WordRepository wordRepository, UserRepository userRepository, StreakService streakService) {
        this.wordRepository = wordRepository;
        this.userRepository = userRepository;
        this.streakService = streakService;
    }

    /**
     * Foydalanuvchining bugungi 20 ta so'zi asosida 5 ta mustahkamlovchi mashq generatsiya qiladi
     */
    public List<Exercise> generateDailyExerciseSet(UserProfile profile) {
        List<Word> dayWords = new ArrayList<>(wordRepository.getWordsForDay(profile.getCurrentDayIndex()));
        if (dayWords.isEmpty()) {
            dayWords = new ArrayList<>(wordRepository.getAllWords());
        }
        Collections.shuffle(dayWords);

        List<Exercise> exercises = new ArrayList<>();
        int count = Math.min(EXERCISES_PER_SESSION, dayWords.size());

        for (int i = 0; i < count; i++) {
            Word targetWord = dayWords.get(i);

            List<Word> distractors = new ArrayList<>(dayWords);
            distractors.remove(targetWord);
            Collections.shuffle(distractors);

            int type = i % 3;
            String prompt;
            String correctAnswer;
            List<String> options = new ArrayList<>();

            if (type == 0) {
                // Inglizcha so'z beriladi -> O'zbekcha tarjimasini topish
                prompt = "📝 <b>Mashq " + (i + 1) + "/" + count + ":</b> Ushbu so'zning to'g'ri ma'nosini tanlang:\n\n" +
                         "🔤 <b>" + targetWord.getEnglishWord().toUpperCase() + "</b> " + targetWord.getPronunciation();
                correctAnswer = targetWord.getUzbekMeaning();

                for (int d = 0; d < Math.min(3, distractors.size()); d++) {
                    options.add(distractors.get(d).getUzbekMeaning());
                }
            } else if (type == 1) {
                // Mnemonik obraz beriladi -> So'zni topish
                prompt = "🧠 <b>Mashq " + (i + 1) + "/" + count + ":</b> Ushbu mnemonik obraz qaysi so'zga tegishli?\n\n" +
                         "🎬 <i>\"" + targetWord.getMnemonicStory() + "\"</i>";
                correctAnswer = targetWord.getEnglishWord();

                for (int d = 0; d < Math.min(3, distractors.size()); d++) {
                    options.add(distractors.get(d).getEnglishWord());
                }
            } else {
                // O'zbekcha ma'nosi beriladi -> Inglizcha so'zni topish
                prompt = "🇺🇿 <b>Mashq " + (i + 1) + "/" + count + ":</b> Quyidagi ma'noga mos inglizcha so'zni toping:\n\n" +
                         "🎯 <b>\"" + targetWord.getUzbekMeaning() + "\"</b>";
                correctAnswer = targetWord.getEnglishWord();

                for (int d = 0; d < Math.min(3, distractors.size()); d++) {
                    options.add(distractors.get(d).getEnglishWord());
                }
            }

            int correctIdx = random.nextInt(options.size() + 1);
            options.add(correctIdx, correctAnswer);

            exercises.add(new Exercise(
                    UUID.randomUUID().toString(),
                    prompt,
                    options,
                    correctIdx,
                    targetWord,
                    i + 1,
                    count
            ));
        }

        return exercises;
    }

    public StreakService.StreakResult completeExerciseSession(UserProfile profile, int correctAnswers) {
        profile.setTodayExerciseCompleted(true);
        profile.setTotalExercisesCompleted(profile.getTotalExercisesCompleted() + EXERCISES_PER_SESSION);
        profile.setTotalQuizCorrect(profile.getTotalQuizCorrect() + correctAnswers);
        profile.setTotalQuizCount(profile.getTotalQuizCount() + EXERCISES_PER_SESSION);

        StreakService.StreakResult result = streakService.recordDailyActivity(profile);
        userRepository.save(profile);
        return result;
    }
}
