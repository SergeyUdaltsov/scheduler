package com.scheduler.processor.impl.bill;

import com.scheduler.model.Bill;
import com.scheduler.model.CommandType;
import com.scheduler.model.Context;
import com.scheduler.model.MessageHolder;
import com.scheduler.processor.IProcessor;
import com.scheduler.service.IBillService;
import com.scheduler.service.IContextService;
import com.scheduler.utils.MessageUtils;
import com.scheduler.utils.PaymentUtils;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Collections;

/**
 * @author Serhii_Udaltsov on 5/8/2021
 */
public class BillEditSaveProcessor implements IProcessor {

    private IContextService contextService;
    private IBillService billService;
    private CommandType returnType = CommandType.DASHBOARD_PROCESSOR;

    public BillEditSaveProcessor(IContextService contextService, IBillService billService) {
        this.contextService = contextService;
        this.billService = billService;
    }

    @Override
    public MessageHolder processRequest(Update update) {
        Context context = contextService.getContext(update);
        Bill bill = PaymentUtils.getSelectedBill(context);
        String sum = MessageUtils.getTextFromUpdate(update);
        SendMessage message = MessageUtils.buildDashboardMessage(update);
        try {
            int sumInt = Integer.parseInt(sum);
            bill.setSum(sumInt);
            billService.save(bill);
        } catch (NumberFormatException ex) {
            returnType = null;
            return MessageUtils.commonUnCheckableVerticalHolder(Collections.emptyList(), "Неправильный " +
                    "формат суммы, введи целое число");
        }
        message.setText("Начисление было изменено, выбери раздел");
        contextService.clearContext(update);
        return MessageUtils.buildDashboardHolder("Начисление было изменено, выбери раздел");
    }

    @Override
    public CommandType getNextCommandType() {
        return returnType;
    }
}
