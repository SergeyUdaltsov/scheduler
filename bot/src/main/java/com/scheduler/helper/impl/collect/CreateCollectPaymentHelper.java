package com.scheduler.helper.impl.collect;

import com.scheduler.helper.IExecuteCollectPaymentHelper;
import com.scheduler.model.Collect;
import com.scheduler.model.CollectType;
import com.scheduler.model.User;
import com.scheduler.service.ICollectService;
import com.scheduler.service.IContextService;
import com.scheduler.service.IUserService;
import com.scheduler.utils.MessageUtils;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

/**
 * @author Serhii_Udaltsov on 1/15/2022
 */
public class CreateCollectPaymentHelper implements IExecuteCollectPaymentHelper {

    private ICollectService collectService;
    private IUserService userService;
    private IContextService contextService;

    public CreateCollectPaymentHelper(ICollectService collectService, IUserService userService,
                                      IContextService contextService) {
        this.collectService = collectService;
        this.userService = userService;
        this.contextService = contextService;
    }

    @Override
    public void executeCollectPaymentAction(Update update, CollectType collectType) {
        String playerName = contextService.getStringValueFromParams(update, "playerName");
        String collectName = contextService.getStringValueFromParams(update, "collectName");
        long operatorId = MessageUtils.getUserIdFromUpdate(update);
        User user = userService.getUserById(operatorId);
        List<Collect> collects = user.getCollects();
        Collect collect = collects.stream()
                .filter(c -> collectName.equals(c.getName()))
                .findFirst()
                .orElse(null);
        collectService.createCollectPayment(collect, update, playerName, collectType);
    }
}
