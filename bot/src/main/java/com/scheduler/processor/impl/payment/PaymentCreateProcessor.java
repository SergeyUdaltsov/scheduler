package com.scheduler.processor.impl.payment;

import com.scheduler.model.CommandType;
import com.scheduler.model.Context;
import com.scheduler.model.KeyBoardType;
import com.scheduler.model.MessageHolder;
import com.scheduler.model.Payment;
import com.scheduler.model.PaymentType;
import com.scheduler.processor.IProcessor;
import com.scheduler.service.IContextService;
import com.scheduler.service.IPaymentService;
import com.scheduler.service.ISettingService;
import com.scheduler.utils.MessageUtils;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.Map;

/**
 * @author Serhii_Udaltsov on 4/14/2021
 */
public class PaymentCreateProcessor implements IProcessor {
    private IContextService contextService;
    private IPaymentService paymentService;
    private ISettingService settingService;

    public PaymentCreateProcessor(IContextService contextService, IPaymentService paymentService,
                                  ISettingService settingService) {
        this.contextService = contextService;
        this.paymentService = paymentService;
        this.settingService = settingService;
    }

    @Override
    public MessageHolder processRequest(Update update) {
        Context context = contextService.getContext(update);
        String paymentNameOrValue = update.getMessage().getText();
        PaymentType paymentType = PaymentType.fromValue(paymentNameOrValue);
        Map<String, Object> params = context.getParams();
        String playerName = (String) params.get("player");
        boolean hiddenPayment = (boolean) contextService.getValueFromParams(update, "hiddenPayment");
        User from = update.getMessage().getFrom();
        int paymentSum = paymentType == PaymentType.CUSTOM_PAYMENT
                ? Integer.parseInt(paymentNameOrValue)
                : settingService.getPaymentSum(paymentType);
        Payment payment = Payment.builder()
                .withName(playerName)
                .withDate(LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli())
                .withSum(paymentSum)
                .withType(paymentType)
                .withHidden(hiddenPayment)
                .withOperator(from.getFirstName() + " " + from.getLastName())
                .withOperatorId(from.getId())
                .build();
        paymentService.save(payment);
        return MessageUtils.holder(Collections.emptyList(), paymentSum +
                        "грн зачислено на счет " + playerName + ". Тип платежа: " + paymentType.getValue(),
                KeyBoardType.VERTICAL, false, false);
    }

    @Override
    public CommandType getNextCommandType() {
        return null;
    }
}
