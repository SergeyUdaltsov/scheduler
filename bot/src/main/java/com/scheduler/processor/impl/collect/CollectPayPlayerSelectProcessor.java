package com.scheduler.processor.impl.collect;

import com.scheduler.model.CollectPayment;
import com.scheduler.model.CommandType;
import com.scheduler.model.Context;
import com.scheduler.model.MessageHolder;
import com.scheduler.model.Player;
import com.scheduler.processor.IProcessor;
import com.scheduler.service.ICollectService;
import com.scheduler.service.IContextService;
import com.scheduler.service.IPlayerService;
import com.scheduler.utils.CollectionUtils;
import com.scheduler.utils.MessageUtils;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Serhii_Udaltsov on 1/5/2022
 */
public class CollectPayPlayerSelectProcessor implements IProcessor {

    private IContextService contextService;
    private IPlayerService playerService;
    private ICollectService collectService;

    public CollectPayPlayerSelectProcessor(IPlayerService playerService, IContextService contextService,
                                           ICollectService collectService) {
        this.playerService = playerService;
        this.contextService = contextService;
        this.collectService = collectService;
    }

    @Override
    public MessageHolder processRequest(Update update) {
        String yearString = contextService.getMessageTextOrDefault(update, "year");
        Context context = contextService.getContext(update);
        List<Player> playersByYear = playerService.getPlayersByYear(Integer.parseInt(yearString));
        Map<String, Object> params = context.getParams();
        params.put("year", yearString);
        String collectName = contextService.getStringValueFromParams(update, "collectName");
        List<CollectPayment> collectUnPayedPayments = collectService.listCollectPayments(collectName,
                MessageUtils.getUserIdFromUpdate(update), false);
        List<String> unPayedPlayerNames = collectUnPayedPayments.stream()
                .map(CollectPayment::getPlayer)
                .collect(Collectors.toList());
        List<String> playerNames = playersByYear.stream()
                .map(Player::getName)
                .filter(unPayedPlayerNames::contains)
                .collect(Collectors.toList());
        contextService.updateContextParams(update, params);
        String message = "Выбери игрока";
        if (CollectionUtils.isEmpty(playerNames)) {
            message = "В этом году все оплатили";
        }
        return MessageUtils.commonUnCheckableVerticalHolder(playerNames, message);
    }

    @Override
    public CommandType getNextCommandType() {
        return CommandType.COLLECT_TYPE_SELECT;
    }
}
