package com.scheduler.processor.impl.payment;

import com.scheduler.helper.IPaymentActionHelper;
import com.scheduler.model.CommandType;
import com.scheduler.model.MessageHolder;
import com.scheduler.processor.IProcessor;
import com.scheduler.service.IContextService;
import com.scheduler.utils.CollectionUtils;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Map;

/**
 * @author Serhii_Udaltsov on 4/19/2021
 */
public class PaymentActionSelectProcessor implements IProcessor {
    private static final String PARAMS_KEY = "paymentBillAction";

    private Map<String, IPaymentActionHelper> helpersMap;
    private IContextService contextService;
    private CommandType nextCommandType;

    public PaymentActionSelectProcessor(Map<String, IPaymentActionHelper> helpersMap,
                                        IContextService contextService) {
        this.helpersMap = helpersMap;
        this.contextService = contextService;
    }

    @Override
    public MessageHolder processRequest(Update update) {
        String text = contextService.getMessageTextOrDefault(update, PARAMS_KEY);
        IPaymentActionHelper helper = helpersMap.get(text);
        this.nextCommandType = helper.getNextCommandType(update);
        String paramsValue = helper.getHelperParamsValue();
        contextService.updateContextParams(update,
                CollectionUtils.<String, Object>mapBuilder()
                        .withPair(PARAMS_KEY, paramsValue)
                        .build());
        return helper.getMessage(update);
    }

    @Override
    public CommandType getNextCommandType() {
        return nextCommandType;
    }
}
