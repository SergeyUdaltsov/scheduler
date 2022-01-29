package com.scheduler.processor;

import com.scheduler.model.CommandType;
import com.scheduler.model.MessageHolder;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * @author Serhii_Udaltsov on 4/7/2021
 */
public interface IProcessor {

    MessageHolder processRequest(Update update) throws TelegramApiException;

    CommandType getNextCommandType();
}
