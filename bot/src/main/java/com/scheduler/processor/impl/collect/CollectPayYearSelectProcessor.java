package com.scheduler.processor.impl.collect;

import com.scheduler.model.CommandType;
import com.scheduler.model.MessageHolder;
import com.scheduler.processor.IProcessor;
import com.scheduler.service.IContextService;
import com.scheduler.service.IUserService;
import com.scheduler.utils.CollectionUtils;
import com.scheduler.utils.MessageUtils;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * @author Serhii_Udaltsov on 1/5/2022
 */
public class CollectPayYearSelectProcessor implements IProcessor {

    private IUserService userService;
    private IContextService contextService;

    public CollectPayYearSelectProcessor(IUserService userService, IContextService contextService) {
        this.userService = userService;
        this.contextService = contextService;
    }

    @Override
    public MessageHolder processRequest(Update update) {
        String collectName = contextService.getMessageTextOrDefault(update, "collectName");
        contextService.updateContextParams(update, CollectionUtils.<String, Object>mapBuilder()
                .withPair("collectName", collectName)
                .build());
        return userService.buildYearMessageHolder(MessageUtils.getUserIdFromUpdate(update));
    }

    @Override
    public CommandType getNextCommandType() {
        return CommandType.COLLECT_PAY_PLAYER_SELECT;
    }
}
