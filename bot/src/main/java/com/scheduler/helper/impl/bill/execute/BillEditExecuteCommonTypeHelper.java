package com.scheduler.helper.impl.bill.execute;

import com.scheduler.helper.IBillEditExecuteBillTypeHelper;
import com.scheduler.model.CommandType;
import com.scheduler.model.MessageHolder;
import com.scheduler.service.IContextService;
import com.scheduler.utils.CollectionUtils;
import com.scheduler.utils.MessageUtils;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

/**
 * @author Serhii_Udaltsov on 5/9/2021
 */
public class BillEditExecuteCommonTypeHelper implements IBillEditExecuteBillTypeHelper {
    private IContextService contextService;

    public BillEditExecuteCommonTypeHelper(IContextService contextService) {
        this.contextService = contextService;
    }

    @Override
    public CommandType getNextCommandType() {
        return CommandType.BILL_EDIT;
    }

    @Override
    public MessageHolder getMessage(Update update) {
        String billType = contextService.getMessageTextOrDefault(update, "someStr");
        contextService.updateContextParams(update, CollectionUtils.<String, Object>mapBuilder()
                .withPair("billEditType", billType)
                .build());
        String message = "Выбери действие над начислением";
        List<String> buttonsList = CollectionUtils.listWithElements("Удалить", "Изменить");
        return MessageUtils.commonUnCheckableVerticalHolder(buttonsList, message);
    }
}
