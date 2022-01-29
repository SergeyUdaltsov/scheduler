package com.scheduler.helper.impl;

import com.scheduler.model.Bill;
import com.scheduler.model.Payment;
import com.scheduler.model.PaymentType;
import com.scheduler.model.Player;
import com.scheduler.model.User;
import com.scheduler.service.IBillService;
import com.scheduler.service.IPaymentService;
import com.scheduler.service.IPlayerService;
import com.scheduler.service.IUserService;
import com.scheduler.utils.CollectionUtils;
import com.scheduler.utils.MessageUtils;
import com.scheduler.utils.PaymentUtils;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Serhii_Udaltsov on 10/26/2021
 */
public class AbstractHelper {

    private IPaymentService paymentService;
    private IPlayerService playerService;
    private IBillService billService;
    private IUserService userService;

    public AbstractHelper(IPaymentService paymentService, IPlayerService playerService,
                          IBillService billService, IUserService userService) {
        this.paymentService = paymentService;
        this.playerService = playerService;
        this.billService = billService;
        this.userService = userService;
    }

    protected Map<String, Map<PaymentType, Integer>> getPlayerBalances(Update update) {
        long userId = MessageUtils.getUserIdFromUpdate(update);
        User user = userService.getUserById(userId);
        List<Payment> allPayments = paymentService.getAllPaymentsByOperator(userId);
        List<Bill> allBills = billService.getAllBills();
        List<Player> allPlayers = playerService.getPlayersByYears(user.getYears()
                .stream().map(Integer::parseInt)
                .collect(Collectors.toList()));
        if (CollectionUtils.isEmpty(allPayments) || CollectionUtils.isEmpty(allPlayers)) {
            return Collections.emptyMap();
        }
        return PaymentUtils.getPlayerBalancesV2(allPayments, allBills, allPlayers);
    }
}
