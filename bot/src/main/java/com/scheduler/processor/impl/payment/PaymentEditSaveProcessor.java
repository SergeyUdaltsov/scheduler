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
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Collections;

/**
 * @author Serhii_Udaltsov on 5/1/2021
 */
public class PaymentEditSaveProcessor implements IProcessor {
    private IContextService contextService;
    private IPaymentService paymentService;
    private CommandType returnType = CommandType.DASHBOARD_PROCESSOR;

    public PaymentEditSaveProcessor(IContextService contextService, IPaymentService paymentService) {
        this.contextService = contextService;
        this.paymentService = paymentService;
    }

    @Override
    public MessageHolder processRequest(Update update) {
        Context context = contextService.getContext(update);
        Payment payment = PaymentUtils.getSelectedPayment(context);
        String sum = MessageUtils.getTextFromUpdate(update);
        SendMessage message = MessageUtils.buildDashboardMessage(update);
        String resultMessage;
        try {
            int sumInt = Integer.parseInt(sum);
            resultMessage = fillPaymentWithSumAndGetMessage(update, payment, sumInt);
            paymentService.save(payment);
        } catch (NumberFormatException ex) {
            returnType = null;
            return MessageUtils.commonUnCheckableVerticalHolder(Collections.emptyList(),
                    "Неправильный формат суммы, введи целое число");
        }
        message.setText(resultMessage + " выбери раздел");
        contextService.clearContext(update);
        return MessageUtils.buildDashboardHolder();
    }

    private String fillPaymentWithSumAndGetMessage(Update update, Payment payment, int sum) {
        String playerAction = contextService.getStringValueFromParams(update, "playerAction");
        switch (playerAction) {
            case "paymentEdit": {
                payment.setSum(sum);
                return "Платеж был изменен,";
            }
            case "billEdit": {
                payment.setSumBill(sum);
                return "Начисление изменено,";
            }
            default: {
                throw new IllegalArgumentException("Wrong value for playerAction parameter");
            }
        }
    }

    @Override
    public CommandType getNextCommandType() {
        return returnType;
    }
}
