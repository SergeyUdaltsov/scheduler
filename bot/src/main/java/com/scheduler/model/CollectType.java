package com.scheduler.model;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * @author Serhii_Udaltsov on 1/5/2022
 */
public enum CollectType {
    CASH("Наличные", true),
    BANK("Карта", false),
    COACH("Тренеру", true),
    REMOVE("Удалить", false);

    private String title;
    private boolean isCash;

    CollectType(String title, boolean isCash) {
        this.title = title;
        this.isCash = isCash;
    }

    public String getTitle() {
        return title;
    }

    public boolean isCash() {
        return isCash;
    }

    @JsonCreator
    public static CollectType fromTitle(String title) {
        for (CollectType value : values()) {
            if (title.equals(value.getTitle())) {
                return value;
            }
        }
        throw new IllegalArgumentException("Collect type not found for " + title);
    }
}
