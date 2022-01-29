package com.scheduler.processor.impl.player;

import com.scheduler.helper.IPlayerEditExecuteHelper;
import com.scheduler.model.CommandType;
import com.scheduler.model.MessageHolder;
import com.scheduler.processor.IProcessor;
import com.scheduler.service.IContextService;
import com.scheduler.utils.CollectionUtils;
import com.scheduler.utils.MessageUtils;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Map;

/**
 * @author Serhii_Udaltsov on 5/30/2021
 */
public class PlayerEditExecuteProcessor implements IProcessor {

    private Map<String, IPlayerEditExecuteHelper> helperMap;
    private IContextService contextService;
    private IPlayerEditExecuteHelper helper;

    public PlayerEditExecuteProcessor(Map<String, IPlayerEditExecuteHelper> helperMap, IContextService contextService) {
        this.helperMap = helperMap;
        this.contextService = contextService;
    }

    @Override
    public MessageHolder processRequest(Update update) throws TelegramApiException {
        String action = contextService.getStringValueFromParams(update, "playerEditAction");
        String year = MessageUtils.getTextFromUpdate(update);
        contextService.updateContextParams(update, CollectionUtils.<String, Object>mapBuilder()
                .withPair("year", year)
                .build());
        IPlayerEditExecuteHelper helper = helperMap.get(action);
        this.helper = helper;
        return helper.getMessage(update);
    }

    @Override
    public CommandType getNextCommandType() {
        return helper.getNextCommandType();
    }
}
