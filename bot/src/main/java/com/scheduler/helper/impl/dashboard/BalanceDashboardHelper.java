package com.scheduler.helper.impl.dashboard;

import com.scheduler.helper.IDashboardHelper;
import com.scheduler.model.CommandType;
import com.scheduler.model.MessageHolder;
import com.scheduler.utils.MessageUtils;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * @author Serhii_Udaltsov on 5/11/2021
 */
public class BalanceDashboardHelper implements IDashboardHelper {

    @Override
    public CommandType getNextCommandType() {
        return CommandType.BALANCE_EXECUTE;
    }

    @Override
    public String getHelperParamsValue() {
        return "Баланс";
    }

    @Override
    public MessageHolder getMessage(Update update) {
        return MessageUtils.commonCheckableVerticalHolder(MessageUtils.balanceTitlesList(),
                "Выбери действие");
    }
}
