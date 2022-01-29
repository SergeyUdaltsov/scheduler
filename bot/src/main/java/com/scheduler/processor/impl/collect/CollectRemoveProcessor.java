package com.scheduler.processor.impl.collect;

import com.scheduler.model.CommandType;
import com.scheduler.model.MessageHolder;
import com.scheduler.processor.IProcessor;
import com.scheduler.service.ICollectService;
import com.scheduler.service.IUserService;
import com.scheduler.utils.MessageUtils;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * @author Serhii_Udaltsov on 1/4/2022
 */
public class CollectRemoveProcessor implements IProcessor {

    private ICollectService collectService;
    private IUserService userService;

    public CollectRemoveProcessor(ICollectService collectService, IUserService userService) {
        this.collectService = collectService;
        this.userService = userService;
    }

    @Override
    public MessageHolder processRequest(Update update) {
        String collectName = MessageUtils.getTextFromUpdate(update);
        collectService.deleteCollect(collectName, MessageUtils.getUserIdFromUpdate(update));
        userService.removeCollect(update, collectName);
        return MessageUtils.buildDashboardHolder("Сбор успешно удален");
    }

    @Override
    public CommandType getNextCommandType() {
        return CommandType.DASHBOARD_PROCESSOR;
    }
}
