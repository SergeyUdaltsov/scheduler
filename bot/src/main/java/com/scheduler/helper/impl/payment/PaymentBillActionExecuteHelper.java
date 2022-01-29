package com.scheduler.helper.impl.payment;

import com.scheduler.helper.IPaymentActionHelper;
import com.scheduler.model.CommandType;
import com.scheduler.model.Context;
import com.scheduler.model.MessageHolder;
import com.scheduler.service.IContextService;
import com.scheduler.service.IUserService;
import com.scheduler.utils.CollectionUtils;
import com.scheduler.utils.MessageUtils;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

/**
 * @author Serhii_Udaltsov on 4/19/2021
 */
public class PaymentBillActionExecuteHelper implements IPaymentActionHelper {
    private static final String PARAMS_VALUE = "Провести";

    private IContextService contextService;
    private boolean hiddenPayment;
    private IUserService userService;

    public PaymentBillActionExecuteHelper(IContextService contextService, boolean hiddenPayment, IUserService userService) {
        this.contextService = contextService;
        this.hiddenPayment = hiddenPayment;
        this.userService = userService;
    }

    @Override
    public MessageHolder getMessage(Update update) {
        return defineMessage(update);
    }

    @Override
    public String getHelperParamsValue() {
        return PARAMS_VALUE;
    }

    private MessageHolder defineMessage(Update update) {
        switch (getDashboardAction(update)) {
            case "Платежи": {
                contextService.updateContextParams(update, CollectionUtils.<String, Object>mapBuilder()
                        .withPair("playerAction", "paymentExecution")
                        .withPair("hiddenPayment", hiddenPayment)
                        .build());
                return userService.buildYearMessageHolder(MessageUtils.getUserIdFromUpdate(update));
            }
            case "Начисления": {
                List<String> buttonsList = MessageUtils.paymentsList();
                return MessageUtils.commonUnCheckableVerticalHolder(buttonsList, "Выбери тип начисления");
            }
            default: {
                throw new IllegalArgumentException("Unknown dashboard action");
            }
        }
    }

    @Override
    public CommandType getNextCommandType(Update update) {
        switch (getDashboardAction(update)) {
            case "Платежи": {
                return CommandType.PLAYER_SELECT;
            }
            case "Начисления": {
                return CommandType.BILL_EXECUTE;
            }
            default: {
                throw new IllegalArgumentException("Unknown dashboard action");
            }
        }
    }

    private String getDashboardAction(Update update) {
        Context context = contextService.getContext(update);
        return (String) context.getParams().get("dashboardAction");
    }
}
