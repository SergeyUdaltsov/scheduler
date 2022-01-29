package com.scheduler.helper.impl.payment;

import com.scheduler.helper.IPaymentActionHelper;
import com.scheduler.model.CommandType;
import com.scheduler.model.MessageHolder;
import com.scheduler.model.Payment;
import com.scheduler.service.IContextService;
import com.scheduler.service.IPaymentService;
import com.scheduler.utils.CollectionUtils;
import com.scheduler.utils.MessageUtils;
import com.scheduler.utils.PaymentUtils;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Collections;
import java.util.List;

/**
 * @author Serhii_Udaltsov on 5/16/2021
 */
public class LastPaymentsHelper implements IPaymentActionHelper {

    private IPaymentService paymentService;
    private IContextService contextService;

    public LastPaymentsHelper(IPaymentService paymentService, IContextService contextService) {
        this.paymentService = paymentService;
        this.contextService = contextService;
    }

    @Override
    public String getHelperParamsValue() {
        return null;
    }

    @Override
    public CommandType getNextCommandType(Update update) {
        return CommandType.PAYMENT_DESCRIBE;
    }

    @Override
    public MessageHolder getMessage(Update update) {
        List<Payment> payments = paymentService.getPaymentsPortion(0, MessageUtils.getUserIdFromUpdate(update));
        if (CollectionUtils.isEmpty(payments)) {
            return MessageUtils.commonUnCheckableVerticalHolder(Collections.emptyList(), "Платежей больше нет");
        }
        Payment lastPayment = payments.get(payments.size() - 1);
        contextService.updateContextParams(update, CollectionUtils.<String, Object>mapBuilder()
                .withPair("lastPaymentDate", lastPayment.getDate())
                .build());
        List<String> buttons = CollectionUtils.listWithElement("Еще");
        return MessageUtils.commonUnCheckableVerticalHolder(buttons, PaymentUtils.getPaymentTitles(payments));
    }
}
