package com.scheduler.helper.impl.dashboard;

import com.scheduler.helper.IDashboardHelper;
import com.scheduler.model.CommandType;
import com.scheduler.model.MessageHolder;
import com.scheduler.service.IContextService;
import com.scheduler.utils.CollectionUtils;
import com.scheduler.utils.MessageUtils;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * @author Serhii_Udaltsov on 5/28/2021
 */
public class PlayerDashboardHelper implements IDashboardHelper {

    private IContextService contextService;

    public PlayerDashboardHelper(IContextService contextService) {
        this.contextService = contextService;
    }

    @Override
    public String getHelperParamsValue() {
        return null;
    }

    @Override
    public MessageHolder getMessage(Update update) {
        contextService.updateContextParams(update, CollectionUtils.<String, Object>mapBuilder()
                .withPair("dashboardAction", "Игроки").build());
        return MessageUtils.commonUnCheckableVerticalHolder(CollectionUtils.listWithElements("Создать", "Удалить",
                "Воскресный список"),
                "Выбери действие");
    }

    @Override
    public CommandType getNextCommandType() {
        return CommandType.PLAYER_PROCESSOR;
    }
}
