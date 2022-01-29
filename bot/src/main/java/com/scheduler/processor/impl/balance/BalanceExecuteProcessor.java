package com.scheduler.processor.impl.balance;

import com.scheduler.helper.IBalanceHelper;
import com.scheduler.model.CommandType;
import com.scheduler.model.MessageHolder;
import com.scheduler.processor.IProcessor;
import com.scheduler.service.IContextService;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Map;

/**
 * @author Serhii_Udaltsov on 5/11/2021
 */
public class BalanceExecuteProcessor implements IProcessor {
    private IContextService contextService;
    private Map<String, IBalanceHelper> helpersMap;
    private IBalanceHelper helper;

    public BalanceExecuteProcessor(IContextService contextService, Map<String, IBalanceHelper> helpersMap) {
        this.contextService = contextService;
        this.helpersMap = helpersMap;
    }

    @Override
    public MessageHolder processRequest(Update update) {
        String dashboardAction = contextService.getMessageTextOrDefault(update, "balanceAction");
        this.helper = helpersMap.get(dashboardAction);
        return helper.getMessage(update);
    }

    @Override
    public CommandType getNextCommandType() {
        return helper.getNextCommandType();
    }
}
