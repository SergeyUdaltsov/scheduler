package com.scheduler.processor.impl.registration;

import com.scheduler.Constants;
import com.scheduler.model.CommandType;
import com.scheduler.model.Context;
import com.scheduler.model.MessageHolder;
import com.scheduler.processor.IProcessor;
import com.scheduler.service.IContextService;
import com.scheduler.utils.CollectionUtils;
import com.scheduler.utils.MessageUtils;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class StartRegistrationProcessor implements IProcessor {

    private final IContextService contextService;

    public StartRegistrationProcessor(IContextService contextService) {
        this.contextService = contextService;
    }

    @Override
    public List<MessageHolder> processRequest(Update update) throws TelegramApiException {
        long operatorId = MessageUtils.getUserIdFromUpdate(update);
        MessageHolder messageHolder = MessageUtils.getLanguageMessageHolder();
        Context context = buildContext(operatorId);
        contextService.save(context);
        return Collections.singletonList(messageHolder);
    }

    private Context buildContext(long chatId) {
        Context context = new Context();
        context.setUserId(chatId);
        context.setParams(Collections.emptyMap());
        context.setCommands(Collections.emptyMap());
        return context;
    }

    @Override
    public Map<String, String> getCommands() {
        return CollectionUtils.<String, String>mapBuilder()
                .withPair(Constants.ANY, CommandType.SET_LANGUAGE.name())
                .build();
    }
}
