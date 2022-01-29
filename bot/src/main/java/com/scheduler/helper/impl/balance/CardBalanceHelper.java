package com.scheduler.helper.impl.balance;

import com.scheduler.helper.IBalanceHelper;
import com.scheduler.model.CommandType;
import com.scheduler.model.MessageHolder;
import com.scheduler.model.Payment;
import com.scheduler.model.PaymentType;
import com.scheduler.model.Transfer;
import com.scheduler.service.IPaymentService;
import com.scheduler.service.ITransferService;
import com.scheduler.utils.CollectionUtils;
import com.scheduler.utils.MessageUtils;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Serhii_Udaltsov on 5/15/2021
 */
public class CardBalanceHelper implements IBalanceHelper {

    private IPaymentService paymentService;
    private ITransferService transferService;

    public CardBalanceHelper(IPaymentService paymentService, ITransferService transferService) {
        this.paymentService = paymentService;
        this.transferService = transferService;
    }

    @Override
    public MessageHolder getMessage(Update update) {
        long userId = MessageUtils.getUserIdFromUpdate(update);
        List<Payment> payments = paymentService.getPaymentsByOperator(userId);
        List<Transfer> transfers = transferService.getAllOperatorTransfers(userId);
        String message = String.format("Остаток на карте: \n%s",
                getBalanceMessage(payments, transfers));
        return MessageUtils.buildDashboardHolder(message);
    }

    private String getBalanceMessage(List<Payment> payments, List<Transfer> allTransfers) {
        Map<PaymentType, List<Payment>> paymentsByType = payments.stream()
                .filter(p -> !p.isHidden())
                .collect(Collectors.groupingBy(Payment::getType));
        Map<PaymentType, List<Transfer>> transfersByType = allTransfers.stream()
                .collect(Collectors.groupingBy(Transfer::getType));
        StringBuilder result = new StringBuilder();
        int total = 0;
        for (Map.Entry<PaymentType, List<Payment>> entry : paymentsByType.entrySet()) {
            int paymentSum = entry.getValue().stream().mapToInt(Payment::getSum).sum();
            int transferSum = 0;
            List<Transfer> transfers = transfersByType.get(entry.getKey());
            if (CollectionUtils.isNotEmpty(transfers)) {
                transferSum = transfers.stream().mapToInt(Transfer::getSum).sum();
            }
            total = total + paymentSum - transferSum;
            result.append(entry.getKey().getValue())
                    .append(": ").append(paymentSum - transferSum).append("\n");
        }
        result.append("\n").append(String.format("Итого: %s", total));
        return result.toString();
    }

    @Override
    public CommandType getNextCommandType() {
        return CommandType.DASHBOARD_PROCESSOR;
    }
}
