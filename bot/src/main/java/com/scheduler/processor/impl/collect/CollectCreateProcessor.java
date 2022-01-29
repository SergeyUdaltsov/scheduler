package com.scheduler.processor.impl.collect;

import com.scheduler.model.Collect;
import com.scheduler.model.CommandType;
import com.scheduler.model.MessageHolder;
import com.scheduler.model.Player;
import com.scheduler.model.User;
import com.scheduler.processor.IProcessor;
import com.scheduler.service.ICollectService;
import com.scheduler.service.IContextService;
import com.scheduler.service.IPlayerService;
import com.scheduler.service.IUserService;
import com.scheduler.utils.MessageUtils;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Serhii_Udaltsov on 1/4/2022
 */
public class CollectCreateProcessor implements IProcessor {

    private IContextService contextService;
    private IPlayerService playerService;
    private IUserService userService;
    private ICollectService collectService;
    private CommandType returnType = CommandType.DASHBOARD_PROCESSOR;

    public CollectCreateProcessor(IContextService contextService, IPlayerService playerService,
                                  IUserService userService, ICollectService collectService) {
        this.contextService = contextService;
        this.playerService = playerService;
        this.userService = userService;
        this.collectService = collectService;
    }

    @Override
    public MessageHolder processRequest(Update update) {
        String collectName = contextService.getStringValueFromParams(update, "collectName");
        String sumString = MessageUtils.getTextFromUpdate(update);
        try {
            int sum = Integer.parseInt(sumString);
            Collect collect = new Collect(collectName, sum);
            userService.addCollectToUser(update, collect);
            long operatorId = MessageUtils.getUserIdFromUpdate(update);
            User user = userService.getUserById(operatorId);
            List<String> years = user.getYears();
            List<Integer> yearsInt = years.stream().map(Integer::new).collect(Collectors.toList());
            List<Player> players = playerService.getPlayersByYears(yearsInt);
            collectService.createCollectBills(players, collect, operatorId);
            returnType = CommandType.DASHBOARD_PROCESSOR;
            return MessageUtils.buildDashboardHolder("Сбор успешно создан");
        } catch (NumberFormatException ex) {
            returnType = null;
            return MessageUtils.commonUnCheckableVerticalHolder(Collections.emptyList(),
                    "Неправильный формат суммы, введи целое число");
        }
    }

    @Override
    public CommandType getNextCommandType() {
        return returnType;
    }
}
