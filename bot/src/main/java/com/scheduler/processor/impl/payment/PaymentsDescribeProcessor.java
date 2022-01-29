package com.scheduler.processor.impl.payment;

import com.scheduler.model.CommandType;
import com.scheduler.model.MessageHolder;
import com.scheduler.model.Payment;
import com.scheduler.processor.IProcessor;
import com.scheduler.service.IContextService;
import com.scheduler.service.IPaymentService;
import com.scheduler.utils.CollectionUtils;
import com.scheduler.utils.MessageUtils;
import com.scheduler.utils.PaymentUtils;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Collections;
import java.util.List;

/**
 * @author Serhii_Udaltsov on 5/16/2021
 */
public class PaymentsDescribeProcessor implements IProcessor {

    private IContextService contextService;
    private IPaymentService paymentService;

    public PaymentsDescribeProcessor(IContextService contextService, IPaymentService paymentService) {
        this.contextService = contextService;
        this.paymentService = paymentService;
    }

    @Override
    public MessageHolder processRequest(Update update) throws TelegramApiException {
        Object lastPaymentDate = contextService.getValueFromParams(update, "lastPaymentDate");
        if (lastPaymentDate == null) {
            return MessageUtils.commonCheckableVerticalHolder(Collections.emptyList(), "Платежей больше нет");
        }
        long startDate = (long) lastPaymentDate;
        List<Payment> payments = paymentService.getPaymentsPortion(startDate, MessageUtils.getUserIdFromUpdate(update));
        if (CollectionUtils.isEmpty(payments)) {
            return MessageUtils.commonUnCheckableVerticalHolder(Collections.emptyList(), "Платежей больше нет");
        }
        Payment lastPayment = payments.get(payments.size() - 1);
        contextService.updateContextParams(update, CollectionUtils.<String, Object>mapBuilder()
                .withPair("lastPaymentDate", lastPayment.getDate())
                .build());
        List<String> buttons = CollectionUtils.listWithElement("Еще");
        return MessageUtils.commonCheckableVerticalHolder(buttons, PaymentUtils.getPaymentTitles(payments));
    }

    @Override
    public CommandType getNextCommandType() {
        return null;
    }
}
