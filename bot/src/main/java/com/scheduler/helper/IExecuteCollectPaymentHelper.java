package com.scheduler.helper;

import com.scheduler.model.CollectType;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * @author Serhii_Udaltsov on 1/15/2022
 */
public interface IExecuteCollectPaymentHelper {

    void executeCollectPaymentAction(Update update, CollectType collectType);
}
