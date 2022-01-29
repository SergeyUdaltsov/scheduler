package com.scheduler.helper.impl.collect;

import com.scheduler.helper.ICollectActionHelper;
import com.scheduler.model.Collect;
import com.scheduler.model.CommandType;
import com.scheduler.model.KeyBoardType;
import com.scheduler.model.MessageHolder;
import com.scheduler.model.User;
import com.scheduler.service.IUserService;
import com.scheduler.utils.MessageUtils;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Serhii_Udaltsov on 1/4/2022
 */
public class RemoveCollectActionHelper implements ICollectActionHelper {

    private IUserService userService;

    public RemoveCollectActionHelper(IUserService userService) {
        this.userService = userService;
    }

    @Override
    public MessageHolder getMessage(Update update) {
        User user = userService.getUserById(MessageUtils.getUserIdFromUpdate(update));
        List<Collect> collects = user.getCollects();
        List<String> collectNames = collects.stream().map(Collect::getName).collect(Collectors.toList());
        return MessageUtils.commonHolder(collectNames, "Выбери сбор", KeyBoardType.TWO_ROW, false);
    }

    @Override
    public CommandType getNextCommandType() {
        return CommandType.COLLECT_REMOVE_PROCESSOR;
    }

    @Override
    public String getHelperParamsValue() {
        return "remove";
    }
}
