package com.scheduler.helper.impl.player;

import com.scheduler.helper.IPlayerActionHelper;
import com.scheduler.model.CommandType;
import com.scheduler.model.Context;
import com.scheduler.model.MessageHolder;
import com.scheduler.model.Player;
import com.scheduler.service.IContextService;
import com.scheduler.service.IPlayerService;
import com.scheduler.utils.CollectionUtils;
import com.scheduler.utils.MessageUtils;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author Serhii_Udaltsov on 4/25/2021
 */
public class PlayerPaymentEditActionSelectHelper implements IPlayerActionHelper {

    private IContextService contextService;
    private IPlayerService playerService;
    private CommandType nextCommandType;

    public PlayerPaymentEditActionSelectHelper(IContextService contextService, IPlayerService playerService,
                                               CommandType nextCommandType) {
        this.contextService = contextService;
        this.playerService = playerService;
        this.nextCommandType = nextCommandType;
    }

    @Override
    public MessageHolder getMessage(Update update, boolean playerCorrect) {
        String message = "Выбери действие";
        List<String> buttonsList = CollectionUtils.listWithElements("Удалить", "Изменить");
        if (!playerCorrect) {
            message = "Игрок не найден";
            buttonsList = Collections.emptyList();
        }
        return MessageUtils.commonUnCheckableVerticalHolder(buttonsList, message);
    }

    @Override
    public boolean executeAction(Update update) {
        Context context = contextService.getContext(update);
        Map<String, Object> params = context.getParams();
        String playerName = contextService.getMessageTextOrDefault(update, "player");
        Player player = playerService.getPlayer(playerName);
        if (player == null) {
            return false;
        }
        params.put("player", playerName);
        contextService.updateContextParams(MessageUtils.getUserIdFromUpdate(update), params);
        return true;
    }

    @Override
    public CommandType getNextCommandType() {
        return nextCommandType;
    }
}
