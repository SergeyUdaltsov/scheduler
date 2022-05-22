package com.scheduler.processor;

import com.scheduler.model.ButtonsType;
import com.scheduler.model.MessageHolder;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;
import java.util.Map;

/**
 * @author Serhii_Udaltsov on 4/7/2021
 */
public interface IProcessor {

    List<MessageHolder> processRequest(Update update) throws TelegramApiException;

    Map<String, String> getCommands();
}
