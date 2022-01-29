package com.scheduler.processor.impl.player;

import com.fasterxml.jackson.core.type.TypeReference;
import com.scheduler.model.CommandType;
import com.scheduler.model.MessageHolder;
import com.scheduler.model.Player;
import com.scheduler.processor.IProcessor;
import com.scheduler.service.IContextService;
import com.scheduler.service.IPlayerService;
import com.scheduler.utils.JsonUtils;
import com.scheduler.utils.MessageUtils;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Map;

/**
 * @author Serhii_Udaltsov on 5/31/2021
 */
public class PlayerRemoveProcessor implements IProcessor {

    private IContextService contextService;
    private IPlayerService playerService;

    public PlayerRemoveProcessor(IContextService contextService, IPlayerService playerService) {
        this.contextService = contextService;
        this.playerService = playerService;
    }

    @Override
    public MessageHolder processRequest(Update update) throws TelegramApiException {
        String playerTitle = MessageUtils.getTextFromUpdate(update);
        Map<String, Object> players = (Map) contextService.getValueFromParams(update, "players");
        Map playerMap = (Map) players.get(playerTitle);
        Player player = JsonUtils.parseMap(playerMap, new TypeReference<Player>() {
        });
        playerService.remove(player);
        contextService.clearContext(update);
        return MessageUtils.buildDashboardHolder("Игрок был удален");
    }

    @Override
    public CommandType getNextCommandType() {
        return CommandType.DASHBOARD_PROCESSOR;
    }
}
