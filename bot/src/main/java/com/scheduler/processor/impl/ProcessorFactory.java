package com.scheduler.processor.impl;

import com.scheduler.model.CommandType;
import com.scheduler.model.Context;
import com.scheduler.processor.IProcessor;
import com.scheduler.processor.IProcessorFactory;
import com.scheduler.service.IContextService;
import com.scheduler.utils.CollectionUtils;
import com.scheduler.utils.MessageUtils;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;
import java.util.Map;

/**
 * @author Serhii_Udaltsov on 4/7/2021
 */
public class ProcessorFactory implements IProcessorFactory {

    private Map<CommandType, IProcessor> processorsMap;
    private IContextService contextService;

    public ProcessorFactory(Map<CommandType, IProcessor> processorsMap, IContextService contextService) {
        this.processorsMap = processorsMap;
        this.contextService = contextService;
    }

    @Override
    public IProcessor getProcessor(Update update, Context context) {
        if (context == null || "главная".equalsIgnoreCase(MessageUtils.getTextFromUpdate(update))) {
            return processorsMap.get(CommandType.START);
        }

        List<CommandType> location = context.getLocation();
        if (CollectionUtils.isEmpty(location)) {
            return processorsMap.get(CommandType.START);
        }
        CommandType commandType;
        if ("назад".equalsIgnoreCase(MessageUtils.getTextFromUpdate(update))) {
            commandType = location.size() < 3
                    ? CommandType.START
                    : contextService.getPreviousCommandTypeAndSaveLocation(context);

        } else {
            commandType = CollectionUtils.getLastElement(location);
        }
        return processorsMap.get(commandType);
    }
}
