package com.scheduler.processor.impl.balance;

import com.scheduler.model.CommandType;
import com.scheduler.model.MessageHolder;
import com.scheduler.model.Transfer;
import com.scheduler.processor.IProcessor;
import com.scheduler.service.IContextService;
import com.scheduler.service.ITransferService;
import com.scheduler.utils.MessageUtils;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collections;

/**
 * @author Serhii_Udaltsov on 5/18/2021
 */
public class TransferExecuteProcessor implements IProcessor {

    private IContextService contextService;
    private ITransferService transferService;
    private CommandType nextCommandType;

    public TransferExecuteProcessor(IContextService contextService, ITransferService transferService) {
        this.contextService = contextService;
        this.transferService = transferService;
    }

    @Override
    public MessageHolder processRequest(Update update) throws TelegramApiException {
        this.nextCommandType = CommandType.DASHBOARD_PROCESSOR;
        String transferSumString = contextService.getMessageTextOrDefault(update, "someStr");
        try {
            int transferSum = Integer.parseInt(transferSumString);
            long date = LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli();
            Transfer transfer = new Transfer(date, transferSum, MessageUtils.getUserIdFromUpdate(update), null);
            transferService.save(transfer);
            return MessageUtils.buildDashboardHolder();
        } catch (NumberFormatException e) {
            this.nextCommandType = null;
            return MessageUtils.commonUnCheckableVerticalHolder(Collections.emptyList(), "Введи целое число суммы" +
                    " перевода");
        }
    }

    @Override
    public CommandType getNextCommandType() {
        return this.nextCommandType;
    }
}
