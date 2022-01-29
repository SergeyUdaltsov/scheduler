package com.scheduler.helper.impl.player;

import com.scheduler.helper.IPlayerActionHelper;
import com.scheduler.model.CommandType;
import com.scheduler.model.Context;
import com.scheduler.model.MessageHolder;
import com.scheduler.service.IContextService;
import com.scheduler.service.IPlayerService;
import com.scheduler.utils.MessageUtils;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author Serhii_Udaltsov on 4/25/2021
 */
public class PlayerPaymentSelectHelper implements IPlayerActionHelper {
    private IContextService contextService;
    private IPlayerService playerService;

    public PlayerPaymentSelectHelper(IContextService contextService, IPlayerService playerService) {
        this.contextService = contextService;
        this.playerService = playerService;
    }

    @Override
    public MessageHolder getMessage(Update update, boolean playerCorrect) {
        List<String> buttons = MessageUtils.paymentsList();
        String message = "Введи сумму или выбери действие";
        if (!playerCorrect) {
            message = "Игрок не найден";
            buttons = Collections.emptyList();
        }
        return MessageUtils.commonUnCheckableVerticalHolder(buttons, message);
    }

    @Override
    public boolean executeAction(Update update) {
        Context context = contextService.getContext(update);
        Map<String, Object> params = context.getParams();
        String playerName = MessageUtils.getTextFromUpdate(update);
        if (playerService.getPlayer(playerName) == null) {
            return false;
        }
        params.put("player", playerName);
        contextService.updateContextParams(MessageUtils.getUserIdFromUpdate(update), params);
        return true;
    }

    @Override
    public CommandType getNextCommandType() {
        return CommandType.PAYMENT_CREATE;
    }
}
