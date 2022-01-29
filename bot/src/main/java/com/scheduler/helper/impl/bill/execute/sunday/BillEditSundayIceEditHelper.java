package com.scheduler.helper.impl.bill.execute.sunday;

import com.scheduler.helper.IBillSundayIceEditHelper;
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
 * @author Serhii_Udaltsov on 5/10/2021
 */
public class BillEditSundayIceEditHelper implements IBillSundayIceEditHelper {

    private IContextService contextService;
    private CommandType returnType;

    public BillEditSundayIceEditHelper(IContextService contextService) {
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
            return MessageUtils.commonUnCheckableVerticalHolder(CollectionUtils.newList(payments.keySet()), "Выбери начисление из списка");
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
