package com.mnemonic.model;

import java.util.List;

public class Exercise {
    private final String id;
    private final String questionPrompt;
    private final List<String> options;
    private final int correctOptionIndex;
    private final Word relatedWord;
    private final int exerciseNumber;
    private final int totalExercises;

    public Exercise(String id, String questionPrompt, List<String> options,
                    int correctOptionIndex, Word relatedWord,
                    int exerciseNumber, int totalExercises) {
        this.id = id;
        this.questionPrompt = questionPrompt;
        this.options = options;
        this.correctOptionIndex = correctOptionIndex;
        this.relatedWord = relatedWord;
        this.exerciseNumber = exerciseNumber;
        this.totalExercises = totalExercises;
    }

    public String getId() {
        return id;
    }

    public String getQuestionPrompt() {
        return questionPrompt;
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

    public int getExerciseNumber() {
        return exerciseNumber;
    }

    public int getTotalExercises() {
        return totalExercises;
    }
}
