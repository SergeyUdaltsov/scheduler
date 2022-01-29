package com.scheduler.helper.impl.collect;

import com.scheduler.helper.IExecuteCollectPaymentHelper;
import com.scheduler.model.CollectType;
import com.scheduler.service.ICollectService;
import com.scheduler.service.IContextService;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * @author Serhii_Udaltsov on 1/15/2022
 */
public class RemoveCollectPaymentHelper implements IExecuteCollectPaymentHelper {
    private IContextService contextService;
    private ICollectService collectService;

    public RemoveCollectPaymentHelper(IContextService contextService, ICollectService collectService) {
        this.contextService = contextService;
        this.collectService = collectService;
    }

    @Override
    public void executeCollectPaymentAction(Update update, CollectType collectType) {
        String playerName = contextService.getStringValueFromParams(update, "playerName");
        String collectName = contextService.getStringValueFromParams(update, "collectName");
        collectService.deleteCollectPayment(playerName, collectName);
    }
}
