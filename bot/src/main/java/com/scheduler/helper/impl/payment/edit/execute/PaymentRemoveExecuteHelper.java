package com.scheduler.helper.impl.payment.edit.execute;

import com.scheduler.helper.IPaymentEditHelper;
import com.scheduler.model.CommandType;
import com.scheduler.model.Context;
import com.scheduler.model.MessageHolder;
import com.scheduler.model.Payment;
import com.scheduler.service.IContextService;
import com.scheduler.service.IPaymentService;
import com.scheduler.utils.MessageUtils;
import com.scheduler.utils.PaymentUtils;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;
import java.util.Map;

/**
 * @author Serhii_Udaltsov on 4/27/2021
 */
public class PaymentRemoveExecuteHelper implements IPaymentEditHelper {

    private IContextService contextService;
    private IPaymentService paymentService;

    public PaymentRemoveExecuteHelper(IContextService contextService, IPaymentService paymentService) {
        this.contextService = contextService;
        this.paymentService = paymentService;
    }

    @Override
    public MessageHolder getMessage(Update update) {
        Context context = contextService.getContext(update);
        Map<String, Object> params = context.getParams();
        String paymentTitle = MessageUtils.getTextFromUpdate(update);
        Payment payment = PaymentUtils.getPaymentFromParams(params, paymentTitle);
        paymentService.remove(payment);
        long operatorId = MessageUtils.getUserIdFromUpdate(update);
        List<Payment> lastPayments = paymentService.getLastPaymentsByPlayerAndOperator(payment.getName(), operatorId);
        List<String> paymentsTitles = PaymentUtils.getPaymentTitlesAndFillParams(lastPayments, params, true);
        contextService.updateContextParams(update, params);
        return MessageUtils.commonUnCheckableVerticalHolder(paymentsTitles, "Платеж был удален");
    }

    @Override
    public CommandType getNextCommandType() {
        return null;
    }
}
