package com.mnemonic.service;

import com.mnemonic.model.QuizQuestion;
import com.mnemonic.model.TargetLanguage;
import com.mnemonic.model.Word;
import com.mnemonic.repository.WordRepository;

import java.util.*;

public class QuizService {
    private final WordRepository repository;
    private final Random random = new Random();

    public QuizService(WordRepository repository) {
        this.repository = repository;
    }

    public Optional<QuizQuestion> generateQuestion() {
        return generateQuestion(TargetLanguage.ENGLISH);
    }

    /**
     * Yangi viktorina savoli generatsiya qiladi (faol tilga qarab)
     */
    public Optional<QuizQuestion> generateQuestion(TargetLanguage language) {
        TargetLanguage lang = (language != null) ? language : TargetLanguage.ENGLISH;
        Optional<Word> correctWordOpt = repository.getRandomWord(lang);
        if (correctWordOpt.isEmpty()) {
            return Optional.empty();
        }

        Word correctWord = correctWordOpt.get();
        List<Word> allWords = new ArrayList<>(repository.getWordsByLanguage(lang));
        allWords.remove(correctWord);
        Collections.shuffle(allWords);

        boolean isMeaningQuestion = random.nextBoolean();
        String langFlag = (lang == TargetLanguage.RUSSIAN) ? "🇷🇺" : "🇬🇧";
        String langWordPrompt = (lang == TargetLanguage.RUSSIAN) ? "ruscha" : "inglizcha";

        String questionText;
        String correctAnswer;
        List<String> distractors = new ArrayList<>();

        if (isMeaningQuestion) {
            questionText = "🎯 <b>Viktorina (" + langFlag + "):</b> Quyidagi " + langWordPrompt + " so'zning to'g'ri tarjimasini toping:\n\n" +
                           "🔤 <b>" + correctWord.getEnglishWord().toUpperCase() + "</b> " + correctWord.getPronunciation();
            correctAnswer = correctWord.getUzbekMeaning();

            for (int i = 0; i < Math.min(3, allWords.size()); i++) {
                distractors.add(allWords.get(i).getUzbekMeaning());
            }
        } else {
            questionText = "🧠 <b>Viktorina (" + langFlag + "):</b> Ushbu mnemonik obraz qaysi " + langWordPrompt + " so'zga tegishli?\n\n" +
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
