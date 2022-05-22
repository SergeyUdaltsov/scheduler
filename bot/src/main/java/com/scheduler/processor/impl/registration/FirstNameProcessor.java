package com.scheduler.processor.impl.registration;

import com.scheduler.Constants;
import com.scheduler.model.CommandType;
import com.scheduler.model.KeyBoardType;
import com.scheduler.model.MessageHolder;
import com.scheduler.model.User;
import com.scheduler.processor.IProcessor;
import com.scheduler.service.IUserService;
import com.scheduler.utils.MessageUtils;
import com.scheduler.utils.StringUtils;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FirstNameProcessor implements IProcessor {

    private final IUserService userService;
    private final Map<String, String> commandsMap = new HashMap<>();

    public FirstNameProcessor(IUserService userService) {
        this.userService = userService;
    }

    public List<MessageHolder> processRequest(Update update) throws TelegramApiException {
        long id = MessageUtils.getUserIdFromUpdate(update);
        String name = MessageUtils.getTextFromUpdate(update);
        if (StringUtils.isBlank(name)) {
            commandsMap.put(Constants.ANY, CommandType.REGISTER_FIRST_NAME.name());
            return Collections.singletonList(MessageUtils.commonCheckableHolder(Collections.emptyList(), "Введи имя",
                    KeyBoardType.VERTICAL));
        }
        User user = new User(id, name, null);
        userService.saveUser(user);
        commandsMap.put(Constants.ANY, CommandType.REGISTER_SECOND_NAME.name());
        return Collections.singletonList(MessageUtils.commonCheckableHolder(Collections.emptyList(), "Введи фамилию",
                KeyBoardType.VERTICAL));
    }

    @Override
    public Map<String, String> getCommands() {
        return commandsMap;
    }
}
