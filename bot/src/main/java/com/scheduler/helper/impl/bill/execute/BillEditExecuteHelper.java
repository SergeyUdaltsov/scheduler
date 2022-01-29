package com.scheduler.helper.impl.bill.execute;

import com.scheduler.helper.IBillEditExecuteHelper;
import com.scheduler.model.Bill;
import com.scheduler.model.CommandType;
import com.scheduler.model.Context;
import com.scheduler.model.MessageHolder;
import com.scheduler.service.IContextService;
import com.scheduler.utils.CollectionUtils;
import com.scheduler.utils.MessageUtils;
import com.scheduler.utils.PaymentUtils;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Collections;
import java.util.Map;

/**
 * @author Serhii_Udaltsov on 5/8/2021
 */
public class BillEditExecuteHelper implements IBillEditExecuteHelper {

    private IContextService contextService;
    private CommandType returnType = CommandType.BILL_EDIT_EXECUTE_SAVE;

    public BillEditExecuteHelper(IContextService contextService) {
        this.contextService = contextService;
    }

    @Override
    public MessageHolder getMessage(Update update) {
        String billTitle = MessageUtils.getTextFromUpdate(update);
        Context context = contextService.getContext(update);
        Map<String, Object> params = context.getParams();
        Bill bill = PaymentUtils.getBillFromParams(context.getParams(), billTitle);
        if (bill == null) {
            this.returnType = null;
            Map bills = (Map) params.get("bills");
            return MessageUtils.commonUnCheckableVerticalHolder(CollectionUtils.newList(bills.keySet()),
                    "Выбери начисление из списка");
        }
        params.put("bills", CollectionUtils.mapBuilder().withPair("bill", bill).build());
        contextService.updateContextParams(update, params);
        this.returnType = CommandType.BILL_EDIT_EXECUTE_SAVE;
        return MessageUtils.commonUnCheckableVerticalHolder(Collections.emptyList(), "Введи новую сумму");
    }

    @Override
    public CommandType getNextCommandType() {
        return returnType;
    }
}
