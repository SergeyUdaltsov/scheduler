package com.scheduler.processor.impl.collect;

import com.scheduler.helper.ICollectActionHelper;
import com.scheduler.model.CommandType;
import com.scheduler.model.MessageHolder;
import com.scheduler.processor.IProcessor;
import com.scheduler.service.IContextService;
import com.scheduler.utils.CollectionUtils;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Map;

/**
 * @author Serhii_Udaltsov on 1/4/2022
 */
public class CollectActionSelectProcessor implements IProcessor {

    private IContextService contextService;
    private Map<String, ICollectActionHelper> helpersMap;
    private ICollectActionHelper helper;

    public CollectActionSelectProcessor(IContextService contextService, Map<String, ICollectActionHelper> helpersMap) {
        this.contextService = contextService;
        this.helpersMap = helpersMap;
    }

    @Override
    public MessageHolder processRequest(Update update) {
        String actionName = contextService.getMessageTextOrDefault(update, "collectAction");
        ICollectActionHelper helper = helpersMap.get(actionName);
        this.helper = helper;
        contextService.updateContextParams(update, CollectionUtils.<String, Object>mapBuilder()
                .withPair("collectAction", actionName).build());
        return helper.getMessage(update);
    }

    @Override
    public CommandType getNextCommandType() {
        return helper.getNextCommandType();
    }
}
