package com.scheduler.helper.impl.payment.edit.execute;

import com.scheduler.helper.IPaymentEditHelper;
import com.scheduler.model.CommandType;
import com.scheduler.model.Context;
import com.scheduler.model.MessageHolder;
import com.scheduler.model.Payment;
import com.scheduler.service.IContextService;
import com.scheduler.utils.CollectionUtils;
import com.scheduler.utils.MessageUtils;
import com.scheduler.utils.PaymentUtils;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Collections;
import java.util.Map;

/**
 * @author Serhii_Udaltsov on 5/1/2021
 */
public class PaymentEditExecuteHelper implements IPaymentEditHelper {

    private IContextService contextService;
    private CommandType returnType = CommandType.PAYMENT_EDIT_EXECUTE_SAVE;

    public PaymentEditExecuteHelper(IContextService contextService) {
        this.contextService = contextService;
    }

    @Override
    public MessageHolder getMessage(Update update) {
        Context context = contextService.getContext(update);
        Map<String, Object> params = context.getParams();
        String paymentTitle = MessageUtils.getTextFromUpdate(update);
        Payment payment = PaymentUtils.getPaymentFromParams(params, paymentTitle);
        if (payment == null) {
            this.returnType = null;
            Map payments = (Map) params.get("payments");
            return MessageUtils.commonUnCheckableVerticalHolder(CollectionUtils.newList(payments.keySet()),
                    "Выбери платеж из списка");
        }
        params.put("payments", CollectionUtils.mapBuilder().withPair("payment", payment).build());
        contextService.updateContextParams(update, params);
        this.returnType = CommandType.PAYMENT_EDIT_EXECUTE_SAVE;
        return MessageUtils.commonUnCheckableVerticalHolder(Collections.emptyList(), "Введи новую сумму");
    }

    @Override
    public CommandType getNextCommandType() {
        return returnType;
    }
}
