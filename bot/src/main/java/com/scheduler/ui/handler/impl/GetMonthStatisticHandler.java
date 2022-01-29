package com.scheduler.ui.handler.impl;

import com.scheduler.Constants;
import com.scheduler.model.Bill;
import com.scheduler.model.Payment;
import com.scheduler.model.PaymentType;
import com.scheduler.model.Player;
import com.scheduler.model.PlayerBalance;
import com.scheduler.model.User;
import com.scheduler.service.IBillService;
import com.scheduler.service.IPaymentService;
import com.scheduler.service.IPlayerService;
import com.scheduler.ui.handler.IUiHandler;
import com.scheduler.ui.request.MonthlyStatisticRequest;
import com.scheduler.utils.PaymentUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Serhii_Udaltsov on 10/23/2021
 */
public class GetMonthStatisticHandler implements IUiHandler<MonthlyStatisticRequest> {

    private IPaymentService paymentService;
    private IBillService billService;
    private IPlayerService playerService;

    public GetMonthStatisticHandler(IPaymentService paymentService, IBillService billService, IPlayerService playerService) {
        this.paymentService = paymentService;
        this.billService = billService;
        this.playerService = playerService;
    }

    @Override
    public Object handleRequest(MonthlyStatisticRequest request) {
        User user = request.getUser();
        List<Player> players = playerService.getPlayersByYears(user.getYears()
                .stream().map(Integer::parseInt)
                .collect(Collectors.toList()));
        List<Payment> allPayments = paymentService.getAllPaymentsByOperator(user.getId());
        List<Bill> allBills = billService.getAllBills();
        Map<String, Map<PaymentType, Integer>> playerBalances = PaymentUtils.getPlayerBalancesV2(allPayments,
                allBills, players);
        return playerBalances.entrySet().stream()
                .map(e -> new PlayerBalance(e.getKey(), e.getValue()))
                .collect(Collectors.toList());
    }

    @Override
    public String getSupportedHandlerType() {
        return Constants.UiEndpoint.MONTHLY_STATISTIC_HANDLER;
    }

    @Override
    public Class<MonthlyStatisticRequest> getHandlerClass() {
        return MonthlyStatisticRequest.class;
    }
}
