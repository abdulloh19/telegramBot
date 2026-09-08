package com.mnemonic.model;

public enum TargetLanguage {
    ENGLISH("🇬🇧 Ingliz tili", "en", "🇬🇧"),
    RUSSIAN("🇷🇺 Rus tili", "ru", "🇷🇺");

    private final String displayName;
    private final String code;
    private final String flag;

    TargetLanguage(String displayName, String code, String flag) {
        this.displayName = displayName;
        this.code = code;
        this.flag = flag;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getCode() {
        return code;
    }

    public String getFlag() {
        return flag;
    }
}
