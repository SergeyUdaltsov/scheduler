package com.scheduler.helper.impl.dashboard;

import com.scheduler.helper.IDashboardHelper;
import com.scheduler.model.CommandType;
import com.scheduler.model.MessageHolder;
import com.scheduler.utils.MessageUtils;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

/**
 * @author Serhii_Udaltsov on 1/4/2022
 */
public class CollectDashboardHelper implements IDashboardHelper {

    private  String paramValue;
    private CommandType nextCommandType;
    private String actionType;
    private List<String> buttons;

    public CollectDashboardHelper(String paramValue, CommandType nextCommandType, String actionType, List<String> buttons) {
        this.paramValue = paramValue;
        this.nextCommandType = nextCommandType;
        this.actionType = actionType;
        this.buttons = buttons;
    }

    @Override
    public CommandType getNextCommandType() {
        return nextCommandType;
    }

    @Override
    public String getHelperParamsValue() {
        return paramValue;
    }

    @Override
    public MessageHolder getMessage(Update update) {
        return MessageUtils.commonCheckableVerticalHolder(buttons, "Выбери действие со " + actionType);
    }
}
