package com.scheduler.helper.impl.player;

import com.scheduler.helper.IPlayerEditExecuteHelper;
import com.scheduler.model.CommandType;
import com.scheduler.model.MessageHolder;
import com.scheduler.utils.MessageUtils;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Collections;

/**
 * @author Serhii_Udaltsov on 5/30/2021
 */
public class PlayerCreateHelper implements IPlayerEditExecuteHelper {

    @Override
    public MessageHolder getMessage(Update update) {
        return MessageUtils.commonUnCheckableVerticalHolder(Collections.emptyList(), "Введи фамилию");
    }

    @Override
    public CommandType getNextCommandType() {
        return CommandType.PLAYER_SAVE;
    }
}
