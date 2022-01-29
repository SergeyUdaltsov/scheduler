package com.scheduler.processor.impl.player;

import com.scheduler.helper.IPlayerHelper;
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
 * @author Serhii_Udaltsov on 6/16/2021
 */
public class PlayerProcessor implements IProcessor {

    private IContextService contextService;
    private Map<String, IPlayerHelper> helpers;
    private IPlayerHelper helper;

    public PlayerProcessor(IContextService contextService, Map<String, IPlayerHelper> helpers) {
        this.contextService = contextService;
        this.helpers = helpers;
    }

    @Override
    public MessageHolder processRequest(Update update) throws TelegramApiException {
        String action = MessageUtils.getTextFromUpdate(update);
        contextService.updateContextParams(update, CollectionUtils.<String, Object>mapBuilder()
                .withPair("playerEditAction", action).build());
        IPlayerHelper helper = helpers.get(action);
        this.helper = helper;
        return helper.getMessage(update);
    }

    @Override
    public CommandType getNextCommandType() {
        return helper.getNextCommandType();
    }
}
