package com.scheduler.processor.impl.registration;

import com.scheduler.Constants;
import com.scheduler.model.CommandType;
import com.scheduler.model.KeyBoardType;
import com.scheduler.model.Language;
import com.scheduler.model.MessageHolder;
import com.scheduler.processor.IProcessor;
import com.scheduler.service.IContextService;
import com.scheduler.utils.MessageUtils;
import com.scheduler.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class SetLanguageProcessor implements IProcessor {

    private final IContextService contextService;
    private final Map<String, String> commandsMap = new HashMap<>();

    public SetLanguageProcessor(IContextService contextService) {
        this.contextService = contextService;
    }

    @Override
    public List<MessageHolder> processRequest(Update update) {
        long id = MessageUtils.getUserIdFromUpdate(update);
        String language = MessageUtils.getTextFromUpdate(update);
        if (StringUtils.isBlank(language)) {
            log.warn(MarkerFactory.getMarker("SEV3"), "Default dictionary will be used");
            commandsMap.put(Constants.ANY, CommandType.REGISTRATION_START.name());
            return Collections.singletonList(MessageUtils.getLanguageMessageHolder());
        }
        contextService.updateLocale(id, Language.fromValue(language));
        commandsMap.put(Constants.ANY, CommandType.REGISTER_FIRST_NAME.name());
        MessageHolder holder = MessageUtils.commonCheckableHolder(Collections.emptyList(), Constants.Messages.ENTER_YOUR_NAME,
                KeyBoardType.VERTICAL);
        return Collections.singletonList(holder);
    }

    @Override
    public Map<String, String> getCommands() {
        return commandsMap;
    }
}
