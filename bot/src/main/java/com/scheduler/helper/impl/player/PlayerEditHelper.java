package com.scheduler.helper.impl.player;

import com.scheduler.helper.IPlayerHelper;
import com.scheduler.model.CommandType;
import com.scheduler.model.MessageHolder;
import com.scheduler.service.IUserService;
import com.scheduler.utils.MessageUtils;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * @author Serhii_Udaltsov on 6/16/2021
 */
public class PlayerEditHelper implements IPlayerHelper {
    private IUserService userService;

    public PlayerEditHelper(IUserService userService) {
        this.userService = userService;
    }

    @Override
    public MessageHolder getMessage(Update update) {
        return userService.buildYearMessageHolder(MessageUtils.getUserIdFromUpdate(update));
    }

    @Override
    public CommandType getNextCommandType() {
        return CommandType.PLAYER_EDIT_EXECUTE;
    }
}
