package com.scheduler.processor.impl.player;

import com.scheduler.helper.IPlayerActionHelper;
import com.scheduler.model.CommandType;
import com.scheduler.model.Context;
import com.scheduler.model.MessageHolder;
import com.scheduler.processor.IProcessor;
import com.scheduler.service.IContextService;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Map;

/**
 * @author Serhii_Udaltsov on 4/25/2021
 */
public class PlayerActionExecuteProcessor implements IProcessor {

    private IContextService contextService;
    private Map<String, IPlayerActionHelper> helpersMap;
    private IPlayerActionHelper helper;

    public PlayerActionExecuteProcessor(IContextService contextService, Map<String, IPlayerActionHelper> helpersMap) {
        this.contextService = contextService;
        this.helpersMap = helpersMap;
    }

    @Override
    public MessageHolder processRequest(Update update) {
        Context context = contextService.getContext(update);
        String helperName = (String) context.getParams().get("playerAction");
        this.helper = helpersMap.get(helperName);
        boolean playerCorrect = helper.executeAction(update);
        return helper.getMessage(update, playerCorrect);
    }

    @Override
    public CommandType getNextCommandType() {
        return helper.getNextCommandType();
    }
}
