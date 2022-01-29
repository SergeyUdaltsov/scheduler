package com.scheduler.processor.impl.bill;

import com.scheduler.helper.IBillEditSelectHelper;
import com.scheduler.model.CommandType;
import com.scheduler.model.MessageHolder;
import com.scheduler.processor.IProcessor;
import com.scheduler.service.IContextService;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Map;

/**
 * @author Serhii_Udaltsov on 5/8/2021
 */
public class BillEditProcessor implements IProcessor {

    private IContextService contextService;
    private Map<String, IBillEditSelectHelper> helperMap;
    private IBillEditSelectHelper helper;

    public BillEditProcessor(IContextService contextService, Map<String, IBillEditSelectHelper> helperMap) {
        this.contextService = contextService;
        this.helperMap = helperMap;
    }

    @Override
    public MessageHolder processRequest(Update update) {
        String actionType = contextService.getMessageTextOrDefault(update, "someStr");
        this.helper = helperMap.get(actionType);
        return helper.getMessage(update);
    }

    @Override
    public CommandType getNextCommandType() {
        return helper.getNextCommandType();
    }
}
