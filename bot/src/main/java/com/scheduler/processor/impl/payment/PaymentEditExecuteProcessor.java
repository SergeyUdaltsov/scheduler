package com.scheduler.processor.impl.payment;

import com.scheduler.helper.IPaymentEditHelper;
import com.scheduler.model.CommandType;
import com.scheduler.model.Context;
import com.scheduler.model.MessageHolder;
import com.scheduler.processor.IProcessor;
import com.scheduler.service.IContextService;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Map;

/**
 * @author Serhii_Udaltsov on 4/27/2021
 */
public class PaymentEditExecuteProcessor implements IProcessor {

    private IContextService contextService;
    private Map<String, IPaymentEditHelper> helpersMap;
    private IPaymentEditHelper helper;

    public PaymentEditExecuteProcessor(IContextService contextService, Map<String, IPaymentEditHelper> helpersMap) {
        this.contextService = contextService;
        this.helpersMap = helpersMap;
    }

    @Override
    public MessageHolder processRequest(Update update) {
        Context context = contextService.getContext(update);
        String action = (String) context.getParams().get("paymentEditAction");
        IPaymentEditHelper helper = helpersMap.get(action);
        this.helper = helper;
        return helper.getMessage(update);
    }

    @Override
    public CommandType getNextCommandType() {
        return helper.getNextCommandType();
    }
}
