package com.mnemonic.model;

import java.util.List;

public class QuizQuestion {
    private final String questionText;
    private final List<String> options;
    private final int correctOptionIndex;
    private final Word relatedWord;

    public QuizQuestion(String questionText, List<String> options, int correctOptionIndex, com.mnemonic.model.Word relatedWord) {
        this.questionText = questionText;
        this.options = options;
        this.correctOptionIndex = correctOptionIndex;
        this.relatedWord = relatedWord;
    }

    public String getQuestionText() {
        return questionText;
    }

    public List<String> getOptions() {
        return options;
    }

    public int getCorrectOptionIndex() {
        return correctOptionIndex;
    }

    public Word getRelatedWord() {
        return relatedWord;
    }
}
