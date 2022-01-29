package com.scheduler.application;

import com.scheduler.model.CommandType;
import com.scheduler.model.Context;
import com.scheduler.model.MessageHolder;
import com.scheduler.model.User;
import com.scheduler.processor.IProcessor;
import com.scheduler.processor.IProcessorFactory;
import com.scheduler.service.IContextService;
import com.scheduler.service.IRoleFacade;
import com.scheduler.service.ISecretService;
import com.scheduler.service.IUserService;
import com.scheduler.utils.MessageUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Serhii_Udaltsov on 4/7/2021
 */
public class TelegramBot extends TelegramLongPollingBot {
    private static final Logger LOG = LoggerFactory.getLogger(TelegramBot.class);

    private IProcessorFactory factory;
    private IContextService contextService;
    private IRoleFacade roleFacade;
    private IUserService userService;
    private ISecretService secretService;
    private Map<Long, User> usersCache = new HashMap<>();

    public TelegramBot(IProcessorFactory factory, IContextService contextService, IRoleFacade roleFacade,
                       IUserService userService, ISecretService secretService) {
        this.factory = factory;
        this.contextService = contextService;
        this.roleFacade = roleFacade;
        this.userService = userService;
        this.secretService = secretService;
    }

    @Override
    public String getBotToken() {
        return secretService.getBotToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        BotApiMethod botApiMethod;
        long userId = MessageUtils.getUserIdFromUpdate(update);
        User userFromCache = usersCache.get(userId);
        if (userFromCache == null) {
            User userFromDb = userService.getUserById(userId);
            if (userFromDb == null) {
                System.out.println(String.format("User not found for id: %s", userId));
                String message = String.format("Права на использование бота отсутствуют для пользователя %s", userId);
                try {
                    execute(new SendMessage(String.valueOf(userId), message));
                    return;
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
            usersCache.put(userId, userFromDb);
        }
        Context context = contextService.getContext(update);
        IProcessor processor = factory.getProcessor(update, context);
        MessageHolder messageHolder = null;
        try {
            messageHolder = processor.processRequest(update);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        botApiMethod = roleFacade.filterByNotAllowed(messageHolder, userId);
        CommandType nextCommandType = processor.getNextCommandType();
        if (nextCommandType != null) {
            contextService.updateContextLocation(update, nextCommandType);
        }
        try {
            execute(botApiMethod);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return "CoachHelper";
    }
}
