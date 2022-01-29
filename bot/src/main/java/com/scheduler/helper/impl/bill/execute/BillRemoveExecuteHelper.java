package com.scheduler.helper.impl.bill.execute;

import com.scheduler.helper.IBillEditExecuteHelper;
import com.scheduler.model.Bill;
import com.scheduler.model.CommandType;
import com.scheduler.model.Context;
import com.scheduler.model.MessageHolder;
import com.scheduler.service.IBillService;
import com.scheduler.service.IContextService;
import com.scheduler.utils.CollectionUtils;
import com.scheduler.utils.MessageUtils;
import com.scheduler.utils.PaymentUtils;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author Serhii_Udaltsov on 5/8/2021
 */
public class BillRemoveExecuteHelper implements IBillEditExecuteHelper {

    private IContextService contextService;
    private IBillService billService;

    public BillRemoveExecuteHelper(IContextService contextService, IBillService billService) {
        this.contextService = contextService;
        this.billService = billService;
    }

    @Override
    public MessageHolder getMessage(Update update) {
        String billTitle = MessageUtils.getTextFromUpdate(update);
        Context context = contextService.getContext(update);
        Map<String, Object> params = context.getParams();
        Bill bill = PaymentUtils.getBillFromParams(context.getParams(), billTitle);
        if (bill == null) {
            Map bills = (Map) params.get("bills");
            return MessageUtils.commonUnCheckableVerticalHolder(CollectionUtils.newList(bills.keySet()),
                    "Выбери начисление из списка");
        }
        billService.remove(bill);
        List<Bill> lastBills = billService.getLastBills(bill.getType());
        List<String> titles = PaymentUtils.getBillTitlesAndFillParams(lastBills, params);
        contextService.updateContextParams(update, params);
        return MessageUtils.commonUnCheckableVerticalHolder(Collections.emptyList(), "Начисление было удалено");
    }

    @Override
    public CommandType getNextCommandType() {
        return null;
    }
}
