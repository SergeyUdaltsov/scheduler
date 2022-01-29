package com.scheduler.processor.impl.bill;

import com.scheduler.helper.IBillEditExecuteHelper;
import com.scheduler.model.CommandType;
import com.scheduler.model.MessageHolder;
import com.scheduler.processor.IProcessor;
import com.scheduler.service.IContextService;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Map;

/**
 * @author Serhii_Udaltsov on 5/8/2021
 */
public class BillEditExecuteProcessor implements IProcessor {

    private IContextService contextService;
    private Map<String, IBillEditExecuteHelper> helperMap;
    private IBillEditExecuteHelper helper;

    public BillEditExecuteProcessor(IContextService contextService, Map<String, IBillEditExecuteHelper> helperMap) {
        this.contextService = contextService;
        this.helperMap = helperMap;
    }

    @Override
    public MessageHolder processRequest(Update update) {
        String editBillAction = contextService.getStringValueFromParams(update, "editBillAction");
        this.helper = helperMap.get(editBillAction);
        return helper.getMessage(update);
    }

    @Override
    public CommandType getNextCommandType() {
        return helper.getNextCommandType();
    }
}
