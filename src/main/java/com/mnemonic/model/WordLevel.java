package com.mnemonic.model;

public enum WordLevel {
    BEGINNER("🟢 Boshlang'ich (A1-A2)"),
    INTERMEDIATE("🟡 O'rta (B1-B2)"),
    ADVANCED("🔴 Yuqori / IELTS (C1-C2)");

    private final String displayName;

    WordLevel(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
