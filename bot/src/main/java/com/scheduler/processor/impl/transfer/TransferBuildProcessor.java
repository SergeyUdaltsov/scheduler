package com.scheduler.processor.impl.transfer;

import com.scheduler.model.CommandType;
import com.scheduler.model.MessageHolder;
import com.scheduler.processor.IProcessor;
import com.scheduler.service.IContextService;
import com.scheduler.utils.CollectionUtils;
import com.scheduler.utils.MessageUtils;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Collections;

/**
 * @author Serhii_Udaltsov on 6/14/2021
 */
public class TransferBuildProcessor implements IProcessor {

    private IContextService contextService;

    public TransferBuildProcessor(IContextService contextService) {
        this.contextService = contextService;
    }

    @Override
    public MessageHolder processRequest(Update update) throws TelegramApiException {
        String transferType = MessageUtils.getTextFromUpdate(update);
        contextService.updateContextParams(update, CollectionUtils.<String, Object>mapBuilder()
                .withPair("transferType", transferType)
                .build());
        return MessageUtils.commonUnCheckableVerticalHolder(Collections.emptyList(), "Введи сумму перевода");
    }

    @Override
    public CommandType getNextCommandType() {
        return CommandType.TRANSFER_CREATE;
    }
}
