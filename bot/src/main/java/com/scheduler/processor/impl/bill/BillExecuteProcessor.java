package com.scheduler.processor.impl.bill;

import com.scheduler.helper.IBillExecuteHelper;
import com.scheduler.model.CommandType;
import com.scheduler.model.MessageHolder;
import com.scheduler.processor.IProcessor;
import com.scheduler.service.IContextService;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Map;

/**
 * @author Serhii_Udaltsov on 5/3/2021
 */
public class BillExecuteProcessor implements IProcessor {
    private IContextService contextService;
    private Map<String, IBillExecuteHelper> helperMap;
    private IBillExecuteHelper helper;

    public BillExecuteProcessor(IContextService contextService, Map<String, IBillExecuteHelper> helperMap) {
        this.contextService = contextService;
        this.helperMap = helperMap;
    }

    @Override
    public MessageHolder processRequest(Update update) {
        String billCommand = contextService.getMessageTextOrDefault(update, "billType");
        IBillExecuteHelper helper = helperMap.get(billCommand);
        this.helper = helper;
        return helper.getMessage(update);
    }

    @Override
    public CommandType getNextCommandType() {
        return helper.getNextCommandType();
    }
}
