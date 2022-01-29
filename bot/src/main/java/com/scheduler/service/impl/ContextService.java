package com.scheduler.service.impl;

import com.scheduler.dao.IContextDao;
import com.scheduler.model.CommandType;
import com.scheduler.model.Context;
import com.scheduler.service.IContextService;
import com.scheduler.utils.CollectionUtils;
import com.scheduler.utils.MessageUtils;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author Serhii_Udaltsov on 4/10/2021
 */
public class ContextService implements IContextService {

    IContextDao contextDao;

    public ContextService(IContextDao contextDao) {
        this.contextDao = contextDao;
    }

    @Override
    public void save(Context context) {
        contextDao.save(context);
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
    public void clearContext(Update update) {
        Context context = new Context();
        context.setLocation(Collections.singletonList(CommandType.DASHBOARD_PROCESSOR));
        context.setUserId(MessageUtils.getUserIdFromUpdate(update));
        context.setParams(Collections.emptyMap());
        save(context);
    }

    @Override
    public CommandType getPreviousCommandTypeAndSaveLocation(Context context) {
        List<CommandType> location = context.getLocation();
        List<CommandType> updatedLocation = CollectionUtils.removeLastElements(location, 2);
        context.setLocation(updatedLocation);
        save(context);
        return CollectionUtils.getLastElement(updatedLocation);
    }

    @Override
    public void updateContextLocation(Update update, CommandType type) {
        Context context = getContext(update);
        List<CommandType> location = context.getLocation();
        location.add(type);
        save(context);
    }

    @Override
    public String getMessageTextOrDefault(Update update, String paramKey) {
        String text = update.getMessage().getText();
        return text.equalsIgnoreCase("назад")
                ? (String) getContext(update).getParams().get(paramKey)
                : text;
    }

    @Override
    public Context getContext(Update update) {
        if (update == null) {
            return null;
        }
        Long userId = update.getMessage().getFrom().getId();
        return contextDao.getContext(userId);
    }

    @Override
    public void updateContextCommand(long userId, CommandType commandType, CommandType previousCommandType,
                                     Map<String, Object> params) {
        contextDao.updateContextCommand(userId, commandType, previousCommandType, params);
    }
}
