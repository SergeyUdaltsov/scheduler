package com.scheduler.service;

import com.scheduler.model.CommandType;
import com.scheduler.model.Context;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Map;

/**
 * @author Serhii_Udaltsov on 4/10/2021
 */
public interface IContextService {

    void save(Context context);

    Context getContext(Update update);

    void updateContextCommand(long contextId, CommandType commandType, CommandType previousCommandType,
                              Map<String, Object> params);

    void updateContextLocation(Update update, CommandType type);

    void updateContextParams(Update update, Map<String, Object> params);

    void updateContextParams(long chatId, Map<String, Object> params);

    String getMessageTextOrDefault(Update update, String paramKey);

    String getStringValueFromParams(Update update, String paramKey);

    Object getValueFromParams(Update update, String paramKey);

    CommandType getPreviousCommandTypeAndSaveLocation(Context context);

    void clearContext(Update update);
}
