package com.scheduler.processor.impl.player;

import com.scheduler.model.CommandType;
import com.scheduler.model.MessageHolder;
import com.scheduler.model.Player;
import com.scheduler.processor.IProcessor;
import com.scheduler.service.IContextService;
import com.scheduler.service.IPlayerService;
import com.scheduler.utils.MessageUtils;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * @author Serhii_Udaltsov on 5/28/2021
 */
public class PlayerSaveProcessor implements IProcessor {

    private IContextService contextService;
    private IPlayerService playerService;

    public PlayerSaveProcessor(IContextService contextService, IPlayerService playerService) {
        this.contextService = contextService;
        this.playerService = playerService;
    }

    @Override
    public MessageHolder processRequest(Update update) throws TelegramApiException {
        String year = contextService.getStringValueFromParams(update, "year");
        String playerName = MessageUtils.getTextFromUpdate(update);
        Player player = new Player(playerName, Integer.parseInt(year));
        playerService.save(player);
        contextService.clearContext(update);
        return MessageUtils.buildDashboardHolder("Игрок был создан: " + playerName + " " + year);
    }

    @Override
    public CommandType getNextCommandType() {
        return CommandType.DASHBOARD_PROCESSOR;
    }
}
