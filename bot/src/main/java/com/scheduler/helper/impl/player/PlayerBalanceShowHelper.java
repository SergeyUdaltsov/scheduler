package com.scheduler.helper.impl.player;

import com.scheduler.helper.IPlayerActionHelper;
import com.scheduler.model.Bill;
import com.scheduler.model.CommandType;
import com.scheduler.model.Context;
import com.scheduler.model.MessageHolder;
import com.scheduler.model.Payment;
import com.scheduler.model.PaymentType;
import com.scheduler.model.Player;
import com.scheduler.service.IBillService;
import com.scheduler.service.IContextService;
import com.scheduler.service.IPaymentService;
import com.scheduler.service.IPlayerService;
import com.scheduler.utils.CollectionUtils;
import com.scheduler.utils.MessageUtils;
import com.scheduler.utils.PaymentUtils;
import com.scheduler.utils.StringUtils;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Serhii_Udaltsov on 5/13/2021
 */
public class PlayerBalanceShowHelper implements IPlayerActionHelper {

    private IContextService contextService;
    private IPaymentService paymentService;
    private IBillService billService;
    private IPlayerService playerService;

    public PlayerBalanceShowHelper(IContextService contextService, IPaymentService paymentService,
                                   IBillService billService, IPlayerService playerService) {
        this.contextService = contextService;
        this.paymentService = paymentService;
        this.billService = billService;
        this.playerService = playerService;
    }

    @Override
    public MessageHolder getMessage(Update update, boolean playerCorrect) {
        String message;
        if (!playerCorrect) {
            message = "Игрок не найден";
            Context context = contextService.getContext(update);
            String year = (String) context.getParams().get("year");
            List<Player> playersByYear = playerService.getPlayersByYear(Integer.parseInt(year));
            List<String> playerNames = playersByYear.stream()
                    .map(Player::getName)
                    .collect(Collectors.toList());
            return MessageUtils.commonUnCheckableVerticalHolder(playerNames, message);
        }
        String playerName = MessageUtils.getTextFromUpdate(update);
        List<Bill> allBills = billService.getAllBills();
        List<Payment> payments = paymentService.getPaymentsByPlayer(playerName);
        Map<PaymentType, Integer> paymentBalances = PaymentUtils.buildPaymentBalances(payments);
        if (CollectionUtils.isEmpty(paymentBalances)) {
            paymentBalances = PaymentUtils.buildEmptyPaymentBalances();
        }
        Map<PaymentType, Integer> playerBalance = PaymentUtils.mergePaymentsWithBills(paymentBalances, allBills);
        List<String> buttons = Collections.singletonList("Получить файл");
        message = MessageUtils.buildPlayerBalanceMessage(playerBalance);
        if (StringUtils.isBlank(message)) {
            message = "Информация отсутствует";
            buttons = Collections.emptyList();
        }
        PaymentUtils.createPlayerPaymentsFile(payments, allBills, playerName);
        return MessageUtils.commonUnCheckableVerticalHolder(buttons, message);
    }

    @Override
    public boolean executeAction(Update update) {
        String playerName = MessageUtils.getTextFromUpdate(update);
        Player player = playerService.getPlayer(playerName);
        return player != null;
    }

    @Override
    public CommandType getNextCommandType() {
        return CommandType.BALANCE_SEND;
    }
}
