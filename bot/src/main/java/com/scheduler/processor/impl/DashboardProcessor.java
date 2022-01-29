package com.scheduler.processor.impl;

import com.scheduler.helper.IDashboardHelper;
import com.scheduler.model.CommandType;
import com.scheduler.model.MessageHolder;
import com.scheduler.processor.IProcessor;
import com.scheduler.service.IContextService;
import com.scheduler.utils.CollectionUtils;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Map;

/**
 * @author Serhii_Udaltsov on 4/18/2021
 */
public class DashboardProcessor implements IProcessor {
    private static final String PARAMS_KEY = "dashboardAction";

    private Map<String, IDashboardHelper> helpersMap;
    private IContextService contextService;
    private IDashboardHelper helper;

    public DashboardProcessor(Map<String, IDashboardHelper> helpersMap,
                              IContextService contextService) {
        this.helpersMap = helpersMap;
        this.contextService = contextService;
    }

    @Override
    public MessageHolder processRequest(Update update) {
        String text = contextService.getMessageTextOrDefault(update, PARAMS_KEY);
        this.helper = helpersMap.get(text);
        String paramsValue = helper.getHelperParamsValue();
        contextService.updateContextParams(update,
                CollectionUtils.<String, Object>mapBuilder()
                        .withPair(PARAMS_KEY, paramsValue)
                        .build());
        return helper.getMessage(update);
    }

    @Override
    public CommandType getNextCommandType() {
        return helper.getNextCommandType();
    }
}
