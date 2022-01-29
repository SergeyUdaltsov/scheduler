package com.scheduler.processor.impl.transfer;

import com.scheduler.model.CommandType;
import com.scheduler.model.MessageHolder;
import com.scheduler.model.PaymentType;
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
 * @author Serhii_Udaltsov on 6/2/2021
 */
public class TransferCreateProcessor implements IProcessor {

    private ITransferService transferService;
    private IContextService contextService;
    private CommandType nextCommandType;

    public TransferCreateProcessor(ITransferService transferService, IContextService contextService) {
        this.transferService = transferService;
        this.contextService = contextService;
    }

    @Override
    public MessageHolder processRequest(Update update) throws TelegramApiException {
        String transferSumString = contextService.getMessageTextOrDefault(update, "someStr");
        try {
            int transferSum = Integer.parseInt(transferSumString);
            if (transferSum == 0) {
                throw new NumberFormatException();
            }
            String transferType = contextService.getStringValueFromParams(update, "transferType");
            long date = LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli();
            Transfer transfer = new Transfer(date, transferSum, MessageUtils.getUserIdFromUpdate(update),
                    PaymentType.fromValue(transferType));
            transferService.save(transfer);
            contextService.clearContext(update);
            this.nextCommandType = CommandType.DASHBOARD_PROCESSOR;
            return MessageUtils.buildDashboardHolder("Перевод проведен");
        } catch (NumberFormatException e) {
            this.nextCommandType = null;
            return MessageUtils.commonUnCheckableVerticalHolder(Collections.emptyList(), "Введи целое число суммы" +
                    " перевода");
        }
    }

    @Override
    public CommandType getNextCommandType() {
        return null;
    }
}
