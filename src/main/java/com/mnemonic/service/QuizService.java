package com.mnemonic.service;

import com.mnemonic.model.QuizQuestion;
import com.mnemonic.model.Word;
import com.mnemonic.repository.WordRepository;

import java.util.*;

public class QuizService {
    private final WordRepository repository;
    private final Random random = new Random();

    public QuizService(WordRepository repository) {
        this.repository = repository;
    }

    /**
     * Yangi viktorina savoli generatsiya qiladi
     */
    public Optional<QuizQuestion> generateQuestion() {
        Optional<Word> correctWordOpt = repository.getRandomWord();
        if (correctWordOpt.isEmpty()) {
            return Optional.empty();
        }

        Word correctWord = correctWordOpt.get();
        List<Word> allWords = new ArrayList<>(repository.getAllWords());
        allWords.remove(correctWord);
        Collections.shuffle(allWords);

        // Savol turlari: 0 = So'z beriladi, tarjimasini topish; 1 = Mnemonika beriladi, so'zni topish
        boolean isMeaningQuestion = random.nextBoolean();

        String questionText;
        String correctAnswer;
        List<String> distractors = new ArrayList<>();

        if (isMeaningQuestion) {
            questionText = "🎯 <b>Viktorina:</b> Quyidagi so'zning to'g'ri tarjimasini toping:\n\n" +
                           "🔤 <b>" + correctWord.getEnglishWord().toUpperCase() + "</b> " + correctWord.getPronunciation();
            correctAnswer = correctWord.getUzbekMeaning();

            for (int i = 0; i < Math.min(3, allWords.size()); i++) {
                distractors.add(allWords.get(i).getUzbekMeaning());
            }
        } else {
            questionText = "🧠 <b>Viktorina:</b> Ushbu mnemonik obraz qaysi so'zga tegishli?\n\n" +
                           "💡 <i>\"" + correctWord.getMnemonicStory() + "\"</i>";
            correctAnswer = correctWord.getEnglishWord();

            for (int i = 0; i < Math.min(3, allWords.size()); i++) {
                distractors.add(allWords.get(i).getEnglishWord());
            }
        }

        List<String> options = new ArrayList<>(distractors);
        int correctIndex = random.nextInt(options.size() + 1);
        options.add(correctIndex, correctAnswer);

        return Optional.of(new QuizQuestion(questionText, options, correctIndex, correctWord));
    }
}
