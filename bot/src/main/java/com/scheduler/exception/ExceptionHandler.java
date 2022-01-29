package com.scheduler.exception;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

/**
 * @author Serhii_Udaltsov on 4/15/2021
 */
public class ExceptionHandler {
    public static BotApiMethod handle(Exception e, long userId) {
        return new SendMessage(String.valueOf(userId), e.getMessage());
    }
}
