package com.scheduler.helper.impl.bill.create;

import com.scheduler.helper.IBillExecuteHelper;
import com.scheduler.model.CommandType;
import com.scheduler.model.MessageHolder;
import com.scheduler.service.IContextService;
import com.scheduler.service.IUserService;
import com.scheduler.utils.CollectionUtils;
import com.scheduler.utils.MessageUtils;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * @author Serhii_Udaltsov on 5/4/2021
 */
public class SundayIceExecuteHelper implements IBillExecuteHelper {

    private IContextService contextService;
    private IUserService userService;

    public SundayIceExecuteHelper(IContextService contextService, IUserService userService) {
        this.contextService = contextService;
        this.userService = userService;
    }

    @Override
    public MessageHolder getMessage(Update update) {
        contextService.updateContextParams(update, CollectionUtils.<String, Object>mapBuilder()
                .withPair("playerAction", "billExecution")
                .withPair("billType", "Воскресный лед")
                .build());
        return userService.buildYearMessageHolder(MessageUtils.getUserIdFromUpdate(update));
    }

    @Override
    public CommandType getNextCommandType() {
        return CommandType.PLAYER_SELECT;
    }
}
