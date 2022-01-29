package com.scheduler.processor.impl.payment;

import com.scheduler.model.CommandType;
import com.scheduler.model.Context;
import com.scheduler.model.MessageHolder;
import com.scheduler.model.Payment;
import com.scheduler.processor.IProcessor;
import com.scheduler.service.IContextService;
import com.scheduler.service.IPaymentService;
import com.scheduler.utils.MessageUtils;
import com.scheduler.utils.PaymentUtils;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Serhii_Udaltsov on 4/25/2021
 */
public class PaymentEditSelectProcessor implements IProcessor {
    private IContextService contextService;
    private IPaymentService paymentService;

    public PaymentEditSelectProcessor(IContextService contextService, IPaymentService paymentService) {
        this.contextService = contextService;
        this.paymentService = paymentService;
    }

    @Override
    public MessageHolder processRequest(Update update) {
        String actionName = contextService.getMessageTextOrDefault(update, "paymentEditAction");
        return getMessage(update, actionName);
    }

    @Override
    public CommandType getNextCommandType() {
        return CommandType.PAYMENT_EDIT_EXECUTE;
    }

    private MessageHolder getMessage(Update update, String action) {
        Context context = contextService.getContext(update);
        Map<String, Object> params = new HashMap<>(context.getParams());
        String playerName = (String) params.get("player");
        long operatorId = MessageUtils.getUserIdFromUpdate(update);
        List<Payment> lastPayments = paymentService.getLastPaymentsByPlayerAndOperator(playerName, operatorId);
        List<String> paymentTitles = PaymentUtils.getPaymentTitlesAndFillParams(lastPayments, params, true);
        params.put("paymentEditAction", action);
        contextService.updateContextParams(update, params);
        return MessageUtils.commonUnCheckableVerticalHolder(paymentTitles, "Выбери платеж");
    }
}
