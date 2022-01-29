package com.scheduler.helper.impl.balance;

import com.scheduler.helper.IBalanceHelper;
import com.scheduler.helper.impl.AbstractHelper;
import com.scheduler.model.CommandType;
import com.scheduler.model.MessageHolder;
import com.scheduler.model.PaymentType;
import com.scheduler.service.IBillService;
import com.scheduler.service.IPaymentService;
import com.scheduler.service.IPlayerService;
import com.scheduler.service.IUserService;
import com.scheduler.utils.CollectionUtils;
import com.scheduler.utils.MessageUtils;
import com.scheduler.utils.StringUtils;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Collections;
import java.util.Map;

/**
 * @author Serhii_Udaltsov on 5/11/2021
 */
public class DebtorsBalanceHelper extends AbstractHelper implements IBalanceHelper {
    private static final String NO_DEBTORS = "Должников нет";

    private CommandType nextCommandType;

    public DebtorsBalanceHelper(IPaymentService paymentService, IPlayerService playerService,
                                IBillService billService, IUserService userService) {
        super(paymentService, playerService, billService, userService);
    }

    @Override
    public CommandType getNextCommandType() {
        return nextCommandType;
    }

    @Override
    public MessageHolder getMessage(Update update) {
        Map<String, Map<PaymentType, Integer>> playerBalances = getPlayerBalances(update);
        if (CollectionUtils.isEmpty(playerBalances)) {
            this.nextCommandType = CommandType.DASHBOARD_PROCESSOR;
            return MessageUtils.buildDashboardHolder(NO_DEBTORS);
        }
        StringBuilder result = new StringBuilder();
        for (Map.Entry<String, Map<PaymentType, Integer>> entry : playerBalances.entrySet()) {
            Map<PaymentType, Integer> balance = entry.getValue();
            balance.entrySet().removeIf(e -> e.getValue() >= 0);
            if (CollectionUtils.isNotEmpty(balance)) {
                result.append(entry.getKey()).append(": \n");
                for (Map.Entry<PaymentType, Integer> balanceEntry : balance.entrySet()) {
                    result.append(balanceEntry.getKey().getValue()).append(": ").append(balanceEntry.getValue()).append(" ");
                }
                result.append("\n\n");
            }
        }
        if (StringUtils.EMPTY_STRING.equalsIgnoreCase(result.toString())) {
            this.nextCommandType = CommandType.DASHBOARD_PROCESSOR;
            return MessageUtils.buildDashboardHolder(NO_DEBTORS);
        }
        return MessageUtils.commonUnCheckableVerticalHolder(Collections.emptyList(), result.toString());
    }
}
