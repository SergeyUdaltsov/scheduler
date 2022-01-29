package com.scheduler.helper;

import com.scheduler.model.CommandType;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * @author Serhii_Udaltsov on 5/1/2021
 */
public interface IPaymentActionHelper extends IHelper{

    String getHelperParamsValue();

    CommandType getNextCommandType(Update update);
}
