package com.scheduler.processor.impl.collect;

import com.scheduler.helper.IExecuteCollectPaymentHelper;
import com.scheduler.model.CollectPayment;
import com.scheduler.model.CollectType;
import com.scheduler.model.CommandType;
import com.scheduler.model.Context;
import com.scheduler.model.MessageHolder;
import com.scheduler.model.Player;
import com.scheduler.processor.IProcessor;
import com.scheduler.service.ICollectService;
import com.scheduler.service.IContextService;
import com.scheduler.service.IPlayerService;
import com.scheduler.service.IUserService;
import com.scheduler.utils.CollectionUtils;
import com.scheduler.utils.MessageUtils;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Serhii_Udaltsov on 1/5/2022
 */
public class CollectPayCreateProcessor implements IProcessor {
    private IContextService contextService;
    private ICollectService collectService;
    private IUserService userService;
    private IPlayerService playerService;
    private CommandType returnType = CommandType.COLLECT_TYPE_SELECT;
    private Map<CollectType, IExecuteCollectPaymentHelper> helpersMap;

    public CollectPayCreateProcessor(IContextService contextService, ICollectService collectService,
                                     IUserService userService, IPlayerService playerService,
                                     Map<CollectType, IExecuteCollectPaymentHelper> helpersMap) {
        this.contextService = contextService;
        this.collectService = collectService;
        this.userService = userService;
        this.playerService = playerService;
        this.helpersMap = helpersMap;
    }

    @Override
    public MessageHolder processRequest(Update update) {
        String collectTypeTitle = MessageUtils.getTextFromUpdate(update);
        CollectType collectType = CollectType.fromTitle(collectTypeTitle);
        IExecuteCollectPaymentHelper helper = helpersMap.get(collectType);
        helper.executeCollectPaymentAction(update, collectType);
//        String playerName = contextService.getStringValueFromParams(update, "playerName");
        String collectName = contextService.getStringValueFromParams(update, "collectName");
        long operatorId = MessageUtils.getUserIdFromUpdate(update);
//        User user = userService.getUserById(operatorId);
//        List<Collect> collects = user.getCollects();
//        Collect collect = collects.stream()
//                .filter(c -> collectName.equals(c.getName()))
//                .findFirst()
//                .orElse(null);
//        collectService.createCollectPayment(collect, update, playerName, collectType);
        Context context = contextService.getContext(update);
        String yearString = (String) context.getParams().get("year");
        List<Player> playersByYear = playerService.getPlayersByYear(Integer.parseInt(yearString));
        List<CollectPayment> unPayedPayments = collectService.listCollectPayments(collectName, operatorId, false);
        List<String> unPayedPlayerNames = unPayedPayments.stream()
                .map(CollectPayment::getPlayer)
                .collect(Collectors.toList());
        List<String> playerNames = playersByYear.stream()
                .map(Player::getName)
                .filter(unPayedPlayerNames::contains)
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(playerNames)) {
            returnType = CommandType.DASHBOARD_PROCESSOR;
            return MessageUtils.buildDashboardHolder("Оплата успешно проведена");
        }
        returnType = CommandType.COLLECT_TYPE_SELECT;
        return MessageUtils.commonUnCheckableVerticalHolder(playerNames, "Оплата успешно проведена \nВыбери игрока");
    }

    @Override
    public CommandType getNextCommandType() {
        return returnType;
    }
}
