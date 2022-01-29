package com.scheduler.helper.impl.dashboard;

import com.scheduler.helper.IDashboardHelper;
import com.scheduler.model.CommandType;
import com.scheduler.model.MessageHolder;
import com.scheduler.utils.MessageUtils;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

/**
 * @author Serhii_Udaltsov on 4/18/2021
 */
public class PaymentBillDashboardHelper implements IDashboardHelper {
    private  String paramValue;
    private CommandType nextCommandType;
    private String actionType;
    private List<String> buttons;

    public PaymentBillDashboardHelper(String paramValue, CommandType nextCommand, String actionType,
                                      List<String> buttons) {
        this.paramValue = paramValue;
        this.nextCommandType = nextCommand;
        this.actionType = actionType;
        this.buttons = buttons;
    }

    @Override
    public MessageHolder getMessage(Update update) {
        return MessageUtils.commonCheckableVerticalHolder(buttons, "Выбери действие с " + actionType);
    }

    @Override
    public String getHelperParamsValue() {
        return paramValue;
    }

    @Override
    public CommandType getNextCommandType() {
        return nextCommandType;
    }
}
