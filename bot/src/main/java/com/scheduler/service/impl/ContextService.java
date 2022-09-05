package com.scheduler.service.impl;

import com.scheduler.dao.IContextDao;
import com.scheduler.model.CommandType;
import com.scheduler.model.Context;
import com.scheduler.model.Language;
import com.scheduler.service.IContextService;
import com.scheduler.service.IRoleFacade;
import com.scheduler.utils.MessageUtils;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Map;

/**
 * @author Serhii_Udaltsov on 4/10/2021
 */
public class ContextService implements IContextService {

    private final IContextDao contextDao;

    public ContextService(IContextDao contextDao) {
        this.contextDao = contextDao;
    }

    @Override
    public void save(Context context) {
        contextDao.save(context);
    }

    @Override
    public void updateLocale(long id, Language language) {
        contextDao.updateLocale(id, language);
    }

    @Override
    public void updateContextParams(Update update, Map<String, Object> params) {
        Context context = getContext(update);
        context.getParams().putAll(params);
        save(context);
    }

    @Override
    public void updateContextParams(long chatId, Map<String, Object> params) {
        Context context = contextDao.getContext(chatId);
        context.getParams().putAll(params);
        save(context);
    }

    @Override
    public String getMessageText(Update update) {
        if (update.getCallbackQuery() != null) {
            return update.getCallbackQuery().getData();
        }
        return update.getMessage().getText();
    }

    @Override
    public String getStringValueFromParams(Update update, String paramKey) {
        Context context = getContext(update);
        return (String) context.getParams().get(paramKey);
    }

    @Override
    public Object getValueFromParams(Update update, String paramKey) {
        Context context = getContext(update);
        return context.getParams().get(paramKey);
    }

    @Override
    public String getMessageTextOrDefault(Update update, String paramKey) {
        String text = update.getMessage().getText();
        return text.equalsIgnoreCase("назад")
                ? (String) getContext(update).getParams().get(paramKey)
                : text;
    }

    @Override
    public void updateContextCommands(Map<String, String> commands, Update update) {
        Context contextFromDb = getContext(update);
        contextFromDb.setCommands(commands);
        contextDao.save(contextFromDb);
    }

    @Override
    public Context getContext(Update update) {
        if (update == null) {
            return null;
        }
        long userId = MessageUtils.getUserIdFromUpdate(update);
        return contextDao.getContext(userId);
    }

    @Override
    public void updateContextCommand(long userId, CommandType commandType, CommandType previousCommandType,
                                     Map<String, Object> params) {
        contextDao.updateContextCommand(userId, commandType, previousCommandType, params);
    }
}
