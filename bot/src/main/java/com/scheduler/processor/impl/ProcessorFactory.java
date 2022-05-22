package com.scheduler.processor.impl;

import com.scheduler.Constants;
import com.scheduler.model.CommandType;
import com.scheduler.model.Context;
import com.scheduler.processor.IProcessor;
import com.scheduler.processor.IProcessorFactory;
import com.scheduler.service.IContextService;
import com.scheduler.utils.MessageUtils;
import com.scheduler.utils.StringUtils;
import org.telegram.telegrambots.meta.api.objects.Update;

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
        if (context == null) {
            return processorsMap.get(CommandType.REGISTRATION_START);
        }
        if ("главная".equalsIgnoreCase(MessageUtils.getTextFromUpdate(update))) {
            return processorsMap.get(CommandType.START);
        }

        String commandKey = contextService.getMessageText(update);
        String commandName = defineCommandTypeName(commandKey, context.getCommands());
        CommandType commandType = CommandType.fromValue(commandName);
        IProcessor processor = processorsMap.get(commandType);
        if (processor == null) {
            throw new IllegalStateException("Processor not found for key " + commandKey);
        }
        return processor;
    }

    private String defineCommandTypeName(String commandKey, Map<String, String> commandsMap) {
        if (Constants.BACK.equals(commandKey)) {
            return commandsMap.get(commandKey);
        }
        String defaultCommandName = commandsMap.get(Constants.ANY);
        return StringUtils.isBlank(defaultCommandName)
                ? commandsMap.get(commandKey)
                : defaultCommandName;
    }
}
