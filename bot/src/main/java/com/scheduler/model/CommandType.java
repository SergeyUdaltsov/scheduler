package com.scheduler.model;

/**
 * @author Serhii_Udaltsov on 4/10/2021
 */
public enum CommandType {
    START,
    REGISTRATION_START,
    SET_LANGUAGE,
    REGISTER_FIRST_NAME,
    REGISTER_SECOND_NAME,
    DASHBOARD_PROCESSOR;

    public static CommandType fromValue(String value) {
        for (CommandType commandType : values()) {
            if (commandType.name().equalsIgnoreCase(value)) {
                return commandType;
            }
        }
        throw new IllegalArgumentException("Command not found for value " + value);
    }
}
