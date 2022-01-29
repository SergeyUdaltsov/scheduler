package com.scheduler.processor.impl.collect;

import com.scheduler.model.CommandType;
import com.scheduler.model.MessageHolder;
import com.scheduler.processor.IProcessor;
import com.scheduler.service.IContextService;
import com.scheduler.utils.CollectionUtils;
import com.scheduler.utils.MessageUtils;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Arrays;
import java.util.Map;

/**
 * @author Serhii_Udaltsov on 1/5/2022
 */
public class CollectPayTypeSelectProcessor implements IProcessor {

    private IContextService contextService;

    public CollectPayTypeSelectProcessor(IContextService contextService) {
        this.contextService = contextService;
    }

    @Override
    public MessageHolder processRequest(Update update) {
        String playerName = MessageUtils.getTextFromUpdate(update);
        Map<String, Object> params = CollectionUtils.<String, Object>mapBuilder()
                .withPair("playerName", playerName)
                .build();
        contextService.updateContextParams(update, params);
        return MessageUtils.commonUnCheckableVerticalHolder(Arrays.asList("Карта", "Наличные", "Тренеру", "Удалить"),
                "Выбери опцию.");
    }

    @Override
    public CommandType getNextCommandType() {
        return CommandType.COLLECT_PAY_CREATE_PROCESSOR;
    }
}
