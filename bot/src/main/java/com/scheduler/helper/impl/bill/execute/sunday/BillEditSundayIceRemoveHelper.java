package com.scheduler.helper.impl.bill.execute.sunday;

import com.scheduler.helper.IBillSundayIceEditHelper;
import com.scheduler.model.CommandType;
import com.scheduler.model.Context;
import com.scheduler.model.MessageHolder;
import com.scheduler.model.Payment;
import com.scheduler.service.IContextService;
import com.scheduler.service.IPaymentService;
import com.scheduler.utils.CollectionUtils;
import com.scheduler.utils.MessageUtils;
import com.scheduler.utils.PaymentUtils;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;
import java.util.Map;

/**
 * @author Serhii_Udaltsov on 5/10/2021
 */
public class BillEditSundayIceRemoveHelper implements IBillSundayIceEditHelper {

    private IContextService contextService;
    private IPaymentService paymentService;

    public BillEditSundayIceRemoveHelper(IContextService contextService, IPaymentService paymentService) {
        this.contextService = contextService;
        this.paymentService = paymentService;
    }

    @Override
    public MessageHolder getMessage(Update update) {
        Context context = contextService.getContext(update);
        Map<String, Object> params = context.getParams();
        String paymentTitle = MessageUtils.getTextFromUpdate(update);
        Payment payment = PaymentUtils.getPaymentFromParams(params, paymentTitle);
        if (payment == null) {
            Map payments = (Map) params.get("payments");
            return MessageUtils.commonUnCheckableVerticalHolder(CollectionUtils.newList(payments.keySet()),
                    "Выбери начисление из списка");
        }
        paymentService.remove(payment);
        long operatorId = MessageUtils.getUserIdFromUpdate(update);
        List<Payment> lastBills = paymentService.getLastSundayIceBills(payment.getName(), operatorId);
        List<String> billsTitles = PaymentUtils.getPaymentTitlesAndFillParams(lastBills, params, false);
        contextService.updateContextParams(update, params);
        return MessageUtils.commonUnCheckableVerticalHolder(billsTitles, "Начисление удалено");
    }

    @Override
    public CommandType getNextCommandType() {
        return null;
    }
}
