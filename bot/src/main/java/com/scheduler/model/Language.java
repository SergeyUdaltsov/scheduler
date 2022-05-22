package com.scheduler.model;

import java.util.Locale;

public enum Language {
    UA("\uD83C\uDDFA\uD83C\uDDE6", Locale.forLanguageTag("UA"), "localization_ua.json"),
    US("\uD83C\uDDFA\uD83C\uDDF8", Locale.forLanguageTag("USA"), "localization.json"),
    RU("\uD83C\uDDF7\uD83C\uDDFA", Locale.forLanguageTag("RU"), "localization_ru.json");

    private final String value;
    private final Locale locale;
    private final String localizationFilePath;

    Language(String value, Locale locale, String localizationFilePath) {
        this.value = value;
        this.locale = locale;
        this.localizationFilePath = localizationFilePath;
    }

    public String getValue() {
        return value;
    }
    public Locale getLocale() {
        return locale;
    }

    public String getLocalizationFilePath() {
        return localizationFilePath;
    }

    public static Language fromValue(String value) {
        for (Language language : values()) {
            if (language.getValue().equalsIgnoreCase(value)) {
                return language;
            }
        }
        throw new IllegalArgumentException("Language not found for value: " + value);
    }
}
