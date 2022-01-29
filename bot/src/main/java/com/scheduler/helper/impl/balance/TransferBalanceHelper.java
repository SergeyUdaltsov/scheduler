package com.scheduler.helper.impl.balance;

import com.scheduler.helper.IBalanceHelper;
import com.scheduler.model.CommandType;
import com.scheduler.model.MessageHolder;
import com.scheduler.utils.MessageUtils;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Collections;

/**
 * @author Serhii_Udaltsov on 5/18/2021
 */
public class TransferBalanceHelper implements IBalanceHelper {

    @Override
    public CommandType getNextCommandType() {
        return CommandType.TRANSFER_EXECUTE;
    }

    @Override
    public MessageHolder getMessage(Update update) {
        return MessageUtils.commonUnCheckableVerticalHolder(Collections.emptyList(), "Введи сумму платежа");
    }
}
