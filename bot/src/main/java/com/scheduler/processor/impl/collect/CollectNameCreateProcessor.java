package com.scheduler.processor.impl.collect;

import com.scheduler.model.CommandType;
import com.scheduler.model.KeyBoardType;
import com.scheduler.model.MessageHolder;
import com.scheduler.processor.IProcessor;
import com.scheduler.service.IContextService;
import com.scheduler.utils.CollectionUtils;
import com.scheduler.utils.MessageUtils;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Collections;

/**
 * @author Serhii_Udaltsov on 1/4/2022
 */
public class CollectNameCreateProcessor implements IProcessor {

    private IContextService contextService;

    public CollectNameCreateProcessor(IContextService contextService) {
        this.contextService = contextService;
    }

    @Override
    public MessageHolder processRequest(Update update) {
        String collectName = contextService.getMessageTextOrDefault(update, "collectName");
        contextService.updateContextParams(update, CollectionUtils.<String, Object>mapBuilder()
                .withPair("collectName", collectName).build());
        return MessageUtils.commonHolder(Collections.emptyList(), "Введи сумму", KeyBoardType.TWO_ROW, false);
    }

    @Override
    public CommandType getNextCommandType() {
        return CommandType.COLLECT_CREATE_PROCESSOR;
    }
}
