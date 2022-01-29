package com.scheduler.helper.impl.bill.execute.sunday;

import com.scheduler.helper.IBillEditExecuteBillTypeHelper;
import com.scheduler.model.CommandType;
import com.scheduler.model.MessageHolder;
import com.scheduler.service.IContextService;
import com.scheduler.service.IUserService;
import com.scheduler.utils.CollectionUtils;
import com.scheduler.utils.MessageUtils;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * @author Serhii_Udaltsov on 5/9/2021
 */
public class BillEditExecuteSundayIceTypeHelper implements IBillEditExecuteBillTypeHelper {

    private IContextService contextService;
    private IUserService userService;

    public BillEditExecuteSundayIceTypeHelper(IContextService contextService, IUserService userService) {
        this.contextService = contextService;
        this.userService = userService;
    }

    @Override
    public CommandType getNextCommandType() {
        return CommandType.PLAYER_SELECT;
    }

    @Override
    public MessageHolder getMessage(Update update) {
        contextService.updateContextParams(update, CollectionUtils.<String, Object>mapBuilder()
                .withPair("playerAction", "billEdit")
                .build());
        return userService.buildYearMessageHolder(MessageUtils.getUserIdFromUpdate(update));
    }
}
