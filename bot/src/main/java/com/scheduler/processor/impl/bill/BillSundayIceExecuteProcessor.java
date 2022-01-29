package com.scheduler.processor.impl.bill;

import com.scheduler.helper.IBillSundayIceEditHelper;
import com.scheduler.model.CommandType;
import com.scheduler.model.Context;
import com.scheduler.model.MessageHolder;
import com.scheduler.processor.IProcessor;
import com.scheduler.service.IContextService;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Map;

/**
 * @author Serhii_Udaltsov on 5/10/2021
 */
public class BillSundayIceExecuteProcessor implements IProcessor {

    private IContextService contextService;
    private Map<String, IBillSundayIceEditHelper> helpersMap;
    private IBillSundayIceEditHelper helper;

    public BillSundayIceExecuteProcessor(IContextService contextService,
                                         Map<String, IBillSundayIceEditHelper> helpersMap) {
        this.contextService = contextService;
        this.helpersMap = helpersMap;
    }

    @Override
    public MessageHolder processRequest(Update update) {
        Context context = contextService.getContext(update);
        String action = (String) context.getParams().get("billEditAction");
        IBillSundayIceEditHelper helper = helpersMap.get(action);
        this.helper = helper;
        return helper.getMessage(update);
    }

    @Override
    public CommandType getNextCommandType() {
        return helper.getNextCommandType();
    }
}
