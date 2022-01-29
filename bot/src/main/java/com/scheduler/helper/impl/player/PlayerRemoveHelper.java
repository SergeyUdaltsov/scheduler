package com.scheduler.helper.impl.player;

import com.scheduler.helper.IPlayerEditExecuteHelper;
import com.scheduler.model.CommandType;
import com.scheduler.model.MessageHolder;
import com.scheduler.model.Player;
import com.scheduler.service.IContextService;
import com.scheduler.service.IPlayerService;
import com.scheduler.utils.CollectionUtils;
import com.scheduler.utils.MessageUtils;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Serhii_Udaltsov on 5/30/2021
 */
public class PlayerRemoveHelper implements IPlayerEditExecuteHelper {

    private IPlayerService playerService;
    private IContextService contextService;

    public PlayerRemoveHelper(IPlayerService playerService, IContextService contextService) {
        this.playerService = playerService;
        this.contextService = contextService;
    }

    @Override
    public MessageHolder getMessage(Update update) {
        String year = MessageUtils.getTextFromUpdate(update);
        List<Player> players = playerService.getPlayersByYear(Integer.parseInt(year));
        Map<String, Player> playerByTitles = players.stream()
                .collect(Collectors.toMap(Player::getName, Function.identity()));
        contextService.updateContextParams(update, CollectionUtils.<String, Object>mapBuilder()
                .withPair("players", playerByTitles).build());
        return MessageUtils.commonUnCheckableVerticalHolder(CollectionUtils.newList(playerByTitles.keySet()),
                "Выбери игрока из списка");
    }

    @Override
    public CommandType getNextCommandType() {
        return CommandType.PLAYER_REMOVE;
    }
}
