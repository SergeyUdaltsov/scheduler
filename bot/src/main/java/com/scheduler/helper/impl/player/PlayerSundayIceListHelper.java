package com.scheduler.helper.impl.player;

import com.scheduler.helper.IPlayerHelper;
import com.scheduler.model.CommandType;
import com.scheduler.model.MessageHolder;
import com.scheduler.model.Payment;
import com.scheduler.service.IPaymentService;
import com.scheduler.utils.MessageUtils;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Serhii_Udaltsov on 6/16/2021
 */
public class PlayerSundayIceListHelper implements IPlayerHelper {

    private IPaymentService paymentService;

    public PlayerSundayIceListHelper(IPaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @Override
    public MessageHolder getMessage(Update update) {
        long userId = MessageUtils.getUserIdFromUpdate(update);
        List<Payment> sundayPayments = paymentService.getSundayPayments(userId);
        List<String> namesList = new ArrayList<>();
        int count = 1;
        for (Payment payment : sundayPayments) {
            namesList.add(String.format("%-1s. %s", count++, payment.getName()));
        }
        String resultMessage = String.join("\n", namesList);
        return MessageUtils.buildDashboardHolder(resultMessage + "\n");
    }

    @Override
    public CommandType getNextCommandType() {
        return CommandType.DASHBOARD_PROCESSOR;
    }
}
