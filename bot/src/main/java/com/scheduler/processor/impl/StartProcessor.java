package com.scheduler.processor.impl;

import com.scheduler.model.CommandType;
import com.scheduler.model.Context;
import com.scheduler.model.MessageHolder;
import com.scheduler.processor.IProcessor;
import com.scheduler.service.IContextService;
import com.scheduler.utils.CollectionUtils;
import com.scheduler.utils.MessageUtils;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author Serhii_Udaltsov on 4/7/2021
 */
public class StartProcessor implements IProcessor {

    private final IContextService contextService;

    public StartProcessor(IContextService contextService) {
        this.contextService = contextService;
    }

    @Override
    public List<MessageHolder> processRequest(Update update) {
        long operatorId = MessageUtils.getUserIdFromUpdate(update);
        MessageHolder messageHolder = MessageUtils.buildDashboardHolder();
        Context context = buildContext(operatorId);
        contextService.save(context);
        return Collections.singletonList(messageHolder);
    }

    private Context buildContext(long chatId) {
        Context context = new Context();
        context.setUserId(chatId);
        context.setParams(Collections.emptyMap());
        return context;
    }

    @Override
    public Map<String, String> getCommands() {
        return CollectionUtils.<String, String>mapBuilder()
                .withPair("Назад", CommandType.START.name())
                .build();
    }
}
