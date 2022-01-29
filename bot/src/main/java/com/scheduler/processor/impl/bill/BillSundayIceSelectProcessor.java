package com.scheduler.processor.impl.bill;

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

import java.util.List;
import java.util.Map;

/**
 * @author Serhii_Udaltsov on 5/9/2021
 */
public class BillSundayIceSelectProcessor implements IProcessor {

    private IContextService contextService;
    private IPaymentService paymentService;

    public BillSundayIceSelectProcessor(IContextService contextService, IPaymentService paymentService) {
        this.contextService = contextService;
        this.paymentService = paymentService;
    }

    @Override
    public MessageHolder processRequest(Update update) {
        String actionName = contextService.getMessageTextOrDefault(update, "billEditType");
        Context context = contextService.getContext(update);
        String playerName = contextService.getStringValueFromParams(update, "player");
        Map<String, Object> params = context.getParams();
        params.put("billEditAction", actionName);
        long operatorId = MessageUtils.getUserIdFromUpdate(update);
        List<Payment> lastBills = paymentService.getLastSundayIceBills(playerName, operatorId);
        params.put("payments", lastBills);
        List<String> titles = PaymentUtils.getPaymentTitlesAndFillParams(lastBills, params, false);
        contextService.updateContextParams(update, params);
        return MessageUtils.commonUnCheckableVerticalHolder(titles, "Выбери начисление");
    }

    @Override
    public CommandType getNextCommandType() {
        return CommandType.BILL_SUNDAY_ICE_EXECUTE;
    }
}
