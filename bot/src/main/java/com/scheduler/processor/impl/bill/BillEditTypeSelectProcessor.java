package com.scheduler.processor.impl.bill;

import com.scheduler.helper.IBillEditExecuteBillTypeHelper;
import com.scheduler.model.CommandType;
import com.scheduler.model.MessageHolder;
import com.scheduler.processor.IProcessor;
import com.scheduler.service.IContextService;
import com.scheduler.utils.CollectionUtils;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Map;

/**
 * @author Serhii_Udaltsov on 5/8/2021
 */
public class BillEditTypeSelectProcessor implements IProcessor {

    private IContextService contextService;
    private Map<String, IBillEditExecuteBillTypeHelper> helperMap;
    private IBillEditExecuteBillTypeHelper helper;

    public BillEditTypeSelectProcessor(IContextService contextService, Map<String, IBillEditExecuteBillTypeHelper> helperMap) {
        this.contextService = contextService;
        this.helperMap = helperMap;
    }

    @Override
    public MessageHolder processRequest(Update update) {
        String billType = contextService.getMessageTextOrDefault(update, "someStr");
        contextService.updateContextParams(update, CollectionUtils.<String, Object>mapBuilder()
                .withPair("billEditType", billType)
                .build());
        this.helper = helperMap.get(billType);
        return helper.getMessage(update);
    }

    @Override
    public CommandType getNextCommandType() {
        return helper.getNextCommandType();
    }
}
