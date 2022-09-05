package com.scheduler.service.impl;

import com.scheduler.model.MessageHolder;
import com.scheduler.service.IRoleFacade;
import com.scheduler.service.IUserSessionService;
import com.scheduler.utils.MessageUtils;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Serhii_Udaltsov on 6/6/2021
 */
public class RoleFacade implements IRoleFacade {

    private final IUserSessionService sessionService;

    public RoleFacade(IUserSessionService sessionService) {
        this.sessionService = sessionService;
    }

    @Override
    public boolean isActionAllowed(String action, long operatorId) {
        List<String> userActions = sessionService.getUserActionList(operatorId);
        return userActions.contains(action);
    }

    @Override
    public List<BotApiMethod> filterByNotAllowed(List<MessageHolder> holders, long operatorId) {
        List<BotApiMethod> messages = new ArrayList<>();
        for (MessageHolder holder : holders) {
            messages.add(MessageUtils.buildMessage(holder, operatorId));
        }
        return messages;
    }
}
