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
 * @author Serhii_Udaltsov on 4/25/2021
 */
public class PaymentBillActionEditHelper implements IPaymentActionHelper {
    private static final String PARAMS_VALUE = "Редактировать";

    private IContextService contextService;
    private IUserService userService;

    public PaymentBillActionEditHelper(IContextService contextService, IUserService userService) {
        this.contextService = contextService;
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
                        .withPair("playerAction", "paymentEdit")
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
                return CommandType.BILL_EDIT_TYPE_SELECT;
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
