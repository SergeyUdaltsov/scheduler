package com.scheduler.helper.impl.bill.create;

import com.scheduler.helper.IPlayerActionHelper;
import com.scheduler.model.CommandType;
import com.scheduler.model.Context;
import com.scheduler.model.MessageHolder;
import com.scheduler.model.Payment;
import com.scheduler.model.PaymentType;
import com.scheduler.model.Player;
import com.scheduler.service.IContextService;
import com.scheduler.service.IPaymentService;
import com.scheduler.service.IPlayerService;
import com.scheduler.service.ISettingService;
import com.scheduler.utils.MessageUtils;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Serhii_Udaltsov on 5/4/2021
 */
public class SundayIceBillCreateHelper implements IPlayerActionHelper {
    private IContextService contextService;
    private IPaymentService paymentService;
    private IPlayerService playerService;
    private ISettingService settingService;

    public SundayIceBillCreateHelper(IContextService contextService, IPaymentService paymentService,
                                     IPlayerService playerService, ISettingService settingService) {
        this.contextService = contextService;
        this.paymentService = paymentService;
        this.playerService = playerService;
        this.settingService = settingService;
    }

    @Override
    public MessageHolder getMessage(Update update, boolean playerCorrect) {
        String playerName = MessageUtils.getTextFromUpdate(update);
        int paymentSum = settingService.getPaymentSum(PaymentType.SUNDAY_ICE);
        String message = "Начислено за \"Воскресный лед\", игрок: " +
                playerName + ", сумма: " + paymentSum;
        if (!playerCorrect) {
            message = "Игрок не найден";
        }
        Context context = contextService.getContext(update);
        String year = (String) context.getParams().get("year");
        List<Player> playersByYear = playerService.getPlayersByYear(Integer.parseInt(year));
        List<String> playerNames = playersByYear.stream()
                .map(Player::getName)
                .collect(Collectors.toList());
        return MessageUtils.commonUnCheckableVerticalHolder(playerNames, message);
    }

    @Override
    public boolean executeAction(Update update) {
        String playerName = MessageUtils.getTextFromUpdate(update);
        Player player = playerService.getPlayer(playerName);
        if (player == null) {
            return false;
        }
        User from = update.getMessage().getFrom();
        PaymentType paymentType = PaymentType.SUNDAY_ICE;
        int paymentSum = settingService.getPaymentSum(paymentType);
        Payment payment = Payment.builder()
                .withName(playerName)
                .withDate(LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli())
                .withSumBill(paymentSum)
                .withType(paymentType)
                .withOperatorId(from.getId())
                .withOperator(from.getFirstName() + " " + from.getLastName())
                .build();
        paymentService.save(payment);
        return true;
    }

    @Override
    public CommandType getNextCommandType() {
        return null;
    }
}
