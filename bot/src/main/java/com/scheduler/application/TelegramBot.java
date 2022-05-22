package com.scheduler.application;

import com.scheduler.Constants;
import com.scheduler.exception.ExceptionHandler;
import com.scheduler.localization.ILocalizer;
import com.scheduler.model.Context;
import com.scheduler.model.MessageHolder;
import com.scheduler.processor.IProcessor;
import com.scheduler.processor.IProcessorFactory;
import com.scheduler.service.IContextService;
import com.scheduler.service.IRoleFacade;
import com.scheduler.service.ISecretService;
import com.scheduler.utils.JsonUtils;
import com.scheduler.utils.MessageUtils;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Serhii_Udaltsov on 4/7/2021
 */
public class TelegramBot extends TelegramLongPollingBot {

    private final IProcessorFactory factory;
    private final IContextService contextService;
    private final IRoleFacade roleFacade;
    private final ISecretService secretService;
    private final ILocalizer localizer;

    public TelegramBot(IProcessorFactory factory, IContextService contextService, IRoleFacade roleFacade,
                       ISecretService secretService, ILocalizer localizer) {
        this.factory = factory;
        this.contextService = contextService;
        this.roleFacade = roleFacade;
        this.secretService = secretService;
        this.localizer = localizer;
    }

    @Override
    public String getBotToken() {
        return secretService.getBotToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        System.out.println("Received update -------------- " + JsonUtils.convertObjectToString(update));
        List<BotApiMethod> messages = new ArrayList<>();
        long userId = MessageUtils.getUserIdFromUpdate(update);
        try {
            Context context = contextService.getContext(update);
            IProcessor processor = factory.getProcessor(update, context);
            System.out.println("processor got ----------- " + processor.getClass().getSimpleName());
            List<MessageHolder> result = processor.processRequest(update);
            localizer.localize(result, update);
            messages = roleFacade.filterByNotAllowed(result, userId);
            fillAndUpdateContext(update, processor);
        } catch (Exception e) {
            messages.add(ExceptionHandler.handle(e, userId));
            e.printStackTrace();
        }
        try {
            for (BotApiMethod message : messages) {
                execute(message);
            }
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void fillAndUpdateContext(Update update, IProcessor processor) {
        Map<String, String> commands = processor.getCommands();
        contextService.updateContextCommands(commands, update);
    }

    @Override
    public String getBotUsername() {
        return Constants.BOT_NAME;
    }
}
