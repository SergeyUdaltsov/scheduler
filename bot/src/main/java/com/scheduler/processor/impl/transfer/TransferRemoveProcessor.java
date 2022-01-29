package com.scheduler.processor.impl.transfer;

import com.scheduler.model.CommandType;
import com.scheduler.model.MessageHolder;
import com.scheduler.model.Transfer;
import com.scheduler.processor.IProcessor;
import com.scheduler.service.IContextService;
import com.scheduler.service.ITransferService;
import com.scheduler.utils.CollectionUtils;
import com.scheduler.utils.JsonUtils;
import com.scheduler.utils.MessageUtils;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;
import java.util.Map;

/**
 * @author Serhii_Udaltsov on 6/2/2021
 */
public class TransferRemoveProcessor implements IProcessor {

    private ITransferService transferService;
    private IContextService contextService;
    private CommandType nextProcessor;

    public TransferRemoveProcessor(ITransferService transferService, IContextService contextService) {
        this.transferService = transferService;
        this.contextService = contextService;
    }

    @Override
    public MessageHolder processRequest(Update update) throws TelegramApiException {
        String transferTitle = MessageUtils.getTextFromUpdate(update);
        Map<String, Object> transfers = (Map) contextService.getValueFromParams(update, "transfers");
        Transfer transfer = JsonUtils.getObjectFromJsonString(Transfer.class,
                JsonUtils.convertObjectToString(transfers.get(transferTitle)));
        transferService.remove(transfer);
        List<String> titles = transferService.getTransferTitlesAndUpdateContextParams(update);
        if (CollectionUtils.isEmpty(titles)) {
            this.nextProcessor = CommandType.DASHBOARD_PROCESSOR;
            return MessageUtils.buildDashboardHolder("Переводов больше нет");
        }
        this.nextProcessor = null;
        return MessageUtils.commonUnCheckableVerticalHolder(titles, "Выбери перевод");
    }

    @Override
    public CommandType getNextCommandType() {
        return nextProcessor;
    }
}
