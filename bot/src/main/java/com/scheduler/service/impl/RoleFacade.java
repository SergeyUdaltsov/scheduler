package com.scheduler.service.impl;

import com.scheduler.model.Button;
import com.scheduler.model.MessageHolder;
import com.scheduler.service.IRoleFacade;
import com.scheduler.service.IUserSessionService;
import com.scheduler.utils.MessageUtils;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Serhii_Udaltsov on 6/6/2021
 */
public class RoleFacade implements IRoleFacade {

    private IUserSessionService sessionService;

    public RoleFacade(IUserSessionService sessionService) {
        this.sessionService = sessionService;
    }

    @Override
    public boolean isActionAllowed(String action, long operatorId) {
        List<String> userActions = sessionService.getUserActionList(operatorId);
        return userActions.contains(action);
    }

    @Override
    public SendMessage filterByNotAllowed(MessageHolder holder, long operatorId) {
        List<Button> buttons = holder.getButtons();
        List<String> titles = new ArrayList<>();
        for (Button button : buttons) {
            String value = button.getValue();
            if (!button.isCheckable() || button.isCheckable() && isActionAllowed(value, operatorId)) {
                titles.add(value);
            }
        }
        return MessageUtils.buildMessage(titles, holder.getMessage(), operatorId, holder.getKeyBoardType(),
                holder.isWithCommonButtons());
    }
}
