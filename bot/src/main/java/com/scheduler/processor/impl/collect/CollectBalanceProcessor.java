package com.scheduler.processor.impl.collect;

import com.scheduler.model.CollectPayment;
import com.scheduler.model.CollectType;
import com.scheduler.model.CommandType;
import com.scheduler.model.MessageHolder;
import com.scheduler.processor.IProcessor;
import com.scheduler.service.ICollectService;
import com.scheduler.utils.CollectionUtils;
import com.scheduler.utils.MessageUtils;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Serhii_Udaltsov on 1/15/2022
 */
public class CollectBalanceProcessor implements IProcessor {
    private static final String EOL = System.lineSeparator();

    private ICollectService collectService;

    public CollectBalanceProcessor(ICollectService collectService) {
        this.collectService = collectService;
    }

    @Override
    public MessageHolder processRequest(Update update) throws TelegramApiException {
        String collectName = MessageUtils.getTextFromUpdate(update);
        List<CollectPayment> allCollectPayments = collectService.collectPaymentsByName(collectName,
                MessageUtils.getUserIdFromUpdate(update));
        Map<Boolean, List<CollectPayment>> paymentsByPayed = allCollectPayments.stream()
                .collect(Collectors.groupingBy(CollectPayment::isPayed));
        Map<CollectType, List<CollectPayment>> payedPaymentsByType = paymentsByPayed.getOrDefault(true,
                Collections.emptyList()).stream()
                .collect(Collectors.groupingBy(CollectPayment::getType));
        StringBuilder result = new StringBuilder(collectName).append(EOL);
        int playerNumber = 1;
        for (Map.Entry<CollectType, List<CollectPayment>> entry : payedPaymentsByType.entrySet()) {
            List<CollectPayment> payments = entry.getValue();
            int resultSum = 0;
            for (CollectPayment payment : payments) {
                String player = payment.getPlayer();
                CollectType type = payment.getType();
                resultSum += payment.getSumBill();
                result.append(playerNumber++).append(" ").append(player).append(" ").append(type.getTitle()).append(EOL);
            }
            result.append("Собрано ").append(entry.getKey().getTitle()).append(" ").append(resultSum).append(EOL).append(EOL);
        }
        List<CollectPayment> unPayedPayments = paymentsByPayed.get(false);
        if (CollectionUtils.isNotEmpty(unPayedPayments)) {
            result.append("❗").append("Должники:").append("❗").append(EOL);
            int unPayedPlayerNumber = 1;
            for (CollectPayment unPayedPayment : unPayedPayments) {
                result.append(unPayedPlayerNumber++).append(" ").append(unPayedPayment.getPlayer()).append(EOL);
            }
        }
        return MessageUtils.buildDashboardHolder(result.toString());
    }

    @Override
    public CommandType getNextCommandType() {
        return CommandType.DASHBOARD_PROCESSOR;
    }
}
