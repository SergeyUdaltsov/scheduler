package com.scheduler.helper;

import com.scheduler.model.CommandType;
import com.scheduler.model.MessageHolder;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * @author Serhii_Udaltsov on 4/25/2021
 */
public interface IPlayerActionHelper {

    MessageHolder getMessage(Update update, boolean playerCorrect);

    boolean executeAction(Update update);

    CommandType getNextCommandType();

}
