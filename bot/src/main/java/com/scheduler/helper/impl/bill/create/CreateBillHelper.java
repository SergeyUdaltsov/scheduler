package com.scheduler.helper.impl.bill.create;

import com.scheduler.helper.IBillExecuteHelper;
import com.scheduler.model.Bill;
import com.scheduler.model.CommandType;
import com.scheduler.model.MessageHolder;
import com.scheduler.model.PaymentType;
import com.scheduler.service.IBillService;
import com.scheduler.service.IContextService;
import com.scheduler.service.ISettingService;
import com.scheduler.utils.CollectionUtils;
import com.scheduler.utils.MessageUtils;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import java.time.LocalDateTime;
import java.util.Collections;

/**
 * @author Serhii_Udaltsov on 5/4/2021
 */
public class CreateBillHelper implements IBillExecuteHelper {

    private IContextService contextService;
    private IBillService billService;
    private ISettingService settingService;

    public CreateBillHelper(IContextService contextService, IBillService billService,
                            ISettingService settingService) {
        this.contextService = contextService;
        this.billService = billService;
        this.settingService = settingService;
    }

    @Override
    public MessageHolder getMessage(Update update) {
        String billCommand = contextService.getMessageTextOrDefault(update, "someStr");
        contextService.updateContextParams(update, CollectionUtils.<String, Object>mapBuilder()
                .withPair("billType", billCommand)
                .build());
        PaymentType paymentType = PaymentType.fromValue(billCommand);
        int paymentSum = settingService.getPaymentSum(paymentType);
        LocalDateTime timeNow = LocalDateTime.now();
        User operator = update.getMessage().getFrom();
        Bill bill = Bill.builder()
                .withSum(paymentSum)
                .withMonth(timeNow.getMonthValue())
                .withType(paymentType)
                .withOperator(operator.getFirstName() + " " + operator.getLastName())
                .build();
        try {
            billService.save(bill);
        } catch (Exception e) {
            System.out.println("Not saved");
            e.printStackTrace();
        }
        return MessageUtils.commonUnCheckableVerticalHolder(Collections.emptyList(), "Начислено, тип: " +
                paymentType.getValue() + ", месяц: " + timeNow.getMonth().name());
    }

    @Override
    public CommandType getNextCommandType() {
        return null;
    }
}
