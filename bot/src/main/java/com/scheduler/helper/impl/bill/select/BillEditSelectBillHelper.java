package com.scheduler.helper.impl.bill.select;

import com.scheduler.helper.IBillEditSelectHelper;
import com.scheduler.model.Bill;
import com.scheduler.model.CommandType;
import com.scheduler.model.MessageHolder;
import com.scheduler.model.PaymentType;
import com.scheduler.service.IBillService;
import com.scheduler.service.IContextService;
import com.scheduler.utils.CollectionUtils;
import com.scheduler.utils.MessageUtils;
import com.scheduler.utils.PaymentUtils;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;
import java.util.Map;

/**
 * @author Serhii_Udaltsov on 5/8/2021
 */
public class BillEditSelectBillHelper implements IBillEditSelectHelper {
    private static final String PARAM_KEY = "billEditType";

    private IContextService contextService;
    private IBillService billService;
    private String editAction;

    public BillEditSelectBillHelper(IContextService contextService, IBillService billService, String editAction) {
        this.contextService = contextService;
        this.billService = billService;
        this.editAction = editAction;
    }

    @Override
    public MessageHolder getMessage(Update update) {
        String billTypeName = contextService.getStringValueFromParams(update, PARAM_KEY);
        PaymentType paymentType = PaymentType.fromValue(billTypeName);
        List<Bill> lastBills = billService.getLastBills(paymentType);
        Map<String, Object> params = CollectionUtils
                .<String, Object>mapBuilder()
                .withPair("editBillAction", editAction)
                .build();
        List<String> titles = PaymentUtils.getBillTitlesAndFillParams(lastBills, params);
        contextService.updateContextParams(update, params);
        return MessageUtils.commonUnCheckableVerticalHolder(titles, "Выбери начисление");
    }

    @Override
    public CommandType getNextCommandType() {
        return CommandType.BILL_EDIT_EXECUTE;
    }
}
