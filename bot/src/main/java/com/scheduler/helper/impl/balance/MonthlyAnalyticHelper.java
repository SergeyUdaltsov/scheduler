package com.scheduler.helper.impl.balance;

import com.scheduler.helper.IBalanceHelper;
import com.scheduler.model.Bill;
import com.scheduler.model.CommandType;
import com.scheduler.model.MessageHolder;
import com.scheduler.model.Payment;
import com.scheduler.model.PaymentType;
import com.scheduler.model.Player;
import com.scheduler.model.User;
import com.scheduler.service.IBillService;
import com.scheduler.service.IPaymentService;
import com.scheduler.service.IPlayerService;
import com.scheduler.service.IUserService;
import com.scheduler.utils.MessageUtils;
import com.scheduler.utils.PaymentUtils;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Serhii_Udaltsov on 6/17/2021
 */
public class MonthlyAnalyticHelper implements IBalanceHelper {

    private IPaymentService paymentService;
    private IBillService billService;
    private IUserService userService;
    private IPlayerService playerService;

    public MonthlyAnalyticHelper(IPaymentService paymentService, IBillService billService,
                                 IUserService userService, IPlayerService playerService) {
        this.paymentService = paymentService;
        this.billService = billService;
        this.userService = userService;
        this.playerService = playerService;
    }

    @Override
    public MessageHolder getMessage(Update update) {
        long userId = MessageUtils.getUserIdFromUpdate(update);
        List<Payment> payments = paymentService.getMonthlyPaymentsByOperator(userId);
        List<Bill> monthlyBills = billService.getMonthlyBills();
        User user = userService.getUserById(userId);
        List<String> yearsString = user.getYears();
        List<Integer> years = yearsString.stream().map(Integer::parseInt).collect(Collectors.toList());
        List<Player> playersByYears = playerService.getAllPlayers();
        List<Player> filteredPlayers = playersByYears.stream()
                .filter(p -> years.contains(p.getYear()))
                .collect(Collectors.toList());

        Map<PaymentType, List<Bill>> billsByTypes = monthlyBills.stream()
                .collect(Collectors.groupingBy(Bill::getType));
        Map<String, List<Payment>> paymentsByPlayers = PaymentUtils.buildPaymentsByPlayersMap(payments);
        List<String> balanceStrings = new ArrayList<>();

        for (Player player : filteredPlayers) {
            StringBuilder message = new StringBuilder(player.getName()).append("\n");
            Map<PaymentType, Integer> paymentBalances = PaymentUtils.buildPaymentBalances(paymentsByPlayers.get(player.getName()));
            for (Map.Entry<PaymentType, Integer> paymentEntry : paymentBalances.entrySet()) {
                List<Bill> bills = billsByTypes.computeIfAbsent(paymentEntry.getKey(), b -> Collections.emptyList());
                int sumBill = bills.stream().mapToInt(Bill::getSum).sum();
                message.append(paymentEntry.getKey().getValue()).append(" ").append(paymentEntry.getValue() - sumBill).append("\n");
            }
            balanceStrings.add(message.toString());
        }

        String result = String.join("\n", balanceStrings);
        return MessageUtils.buildDashboardHolder(result);
    }

    @Override
    public CommandType getNextCommandType() {
        return CommandType.DASHBOARD_PROCESSOR;
    }
}
