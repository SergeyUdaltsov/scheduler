package com.scheduler.processor.impl.player;

import com.scheduler.model.CommandType;
import com.scheduler.model.Context;
import com.scheduler.model.MessageHolder;
import com.scheduler.model.Player;
import com.scheduler.processor.IProcessor;
import com.scheduler.service.IContextService;
import com.scheduler.service.IPlayerService;
import com.scheduler.utils.MessageUtils;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Serhii_Udaltsov on 4/11/2021
 */
public class PlayersSelectProcessor implements IProcessor {

    private IPlayerService playerService;
    private IContextService contextService;

    public PlayersSelectProcessor(IPlayerService playerService, IContextService contextService) {
        this.playerService = playerService;
        this.contextService = contextService;
    }

    @Override
    public MessageHolder processRequest(Update update) {
        String yearString = contextService.getMessageTextOrDefault(update, "year");
        Context context = contextService.getContext(update);
        List<Player> playersByYear = playerService.getPlayersByYear(Integer.parseInt(yearString));
        Map<String, Object> params = context.getParams();
        params.put("year", yearString);
        List<String> playerNames = playersByYear.stream()
                .map(Player::getName)
                .collect(Collectors.toList());
        contextService.updateContextParams(update, params);
        return MessageUtils.commonUnCheckableVerticalHolder(playerNames, "Выбери игрока");
    }

    @Override
    public CommandType getNextCommandType() {
        return CommandType.PLAYER_ACTION_EXECUTE;
    }
}
