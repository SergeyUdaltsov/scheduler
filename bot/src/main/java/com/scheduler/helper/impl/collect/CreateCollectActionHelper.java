package com.scheduler.helper.impl.collect;

import com.scheduler.helper.ICollectActionHelper;
import com.scheduler.model.CommandType;
import com.scheduler.model.KeyBoardType;
import com.scheduler.model.MessageHolder;
import com.scheduler.service.IContextService;
import com.scheduler.utils.CollectionUtils;
import com.scheduler.utils.MessageUtils;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Collections;

/**
 * @author Serhii_Udaltsov on 1/4/2022
 */
public class CreateCollectActionHelper implements ICollectActionHelper {

    private static final String PARAMS_VALUE = "Создать";

    private IContextService contextService;

    public CreateCollectActionHelper(IContextService contextService) {
        this.contextService = contextService;
    }

    @Override
    public MessageHolder getMessage(Update update) {
        contextService.updateContextParams(update, CollectionUtils.<String, Object>mapBuilder()
                .withPair("collectAction", "Создать").build());
        return MessageUtils.commonHolder(Collections.emptyList(), "Введи название", KeyBoardType.TWO_ROW, false);
    }

    @Override
    public CommandType getNextCommandType() {
        return CommandType.COLLECT_NAME_CREATE_SELECT;
    }

    @Override
    public String getHelperParamsValue() {
        return PARAMS_VALUE;
    }
}
