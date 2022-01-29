package com.scheduler.utils;

/**
 * @author Serhii_Udaltsov on 5/15/2021
 */
public class StringUtils {
    public static final String EMPTY_STRING = "";

    public static boolean isBlank(String text) {
        return (text == null || EMPTY_STRING.equalsIgnoreCase(text));
    }
}
