package com.scheduler.processor.impl.transfer;

import com.scheduler.helper.ITransferHelper;
import com.scheduler.model.CommandType;
import com.scheduler.model.MessageHolder;
import com.scheduler.processor.IProcessor;
import com.scheduler.utils.MessageUtils;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Map;

/**
 * @author Serhii_Udaltsov on 6/1/2021
 */
public class TransferSelectProcessor implements IProcessor {

    private Map<String, ITransferHelper> helperMap;
    private ITransferHelper helper;

    public TransferSelectProcessor(Map<String, ITransferHelper> helperMap) {
        this.helperMap = helperMap;
    }

    @Override
    public MessageHolder processRequest(Update update) throws TelegramApiException {
        String transferAction = MessageUtils.getTextFromUpdate(update);
        ITransferHelper helper = helperMap.get(transferAction);
        this.helper = helper;
        return helper.getMessage(update);
    }

    @Override
    public CommandType getNextCommandType() {
        return helper.getNextCommandType();
    }
}
