package com.scheduler.helper.impl.collect;

import com.scheduler.helper.ICollectActionHelper;
import com.scheduler.model.Collect;
import com.scheduler.model.CollectPayment;
import com.scheduler.model.CollectType;
import com.scheduler.model.CommandType;
import com.scheduler.model.MessageHolder;
import com.scheduler.model.User;
import com.scheduler.service.ICollectService;
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
public class BalanceTotalCollectActionHelper implements ICollectActionHelper {
    private static final String EOL = System.lineSeparator();

    private ICollectService collectService;
    private IUserService userService;

    public BalanceTotalCollectActionHelper(ICollectService collectService, IUserService userService) {
        this.collectService = collectService;
        this.userService = userService;
    }

    @Override
    public MessageHolder getMessage(Update update) {
        long oId = MessageUtils.getUserIdFromUpdate(update);
        User user = userService.getUserById(oId);
        List<CollectPayment> collectPayments = collectService.listAllCollects(oId);
        Map<String, List<CollectPayment>> paymentsByCollectName = collectPayments.stream()
                .collect(Collectors.groupingBy(CollectPayment::getName));
        int totalCard = 0;
        int totalCash = 0;
        int totalCoach = 0;
        StringBuilder builder = new StringBuilder();
        for (Collect collect : user.getCollects()) {
            builder.append("⭐").append(collect.getName()).append("⭐").append(EOL);
            List<CollectPayment> payments = paymentsByCollectName.get(collect.getName());
            if (payments != null) {
                Map<Boolean, List<CollectPayment>> paymentsByPayed = payments.stream()
                        .collect(Collectors.groupingBy(CollectPayment::isPayed));
                List<CollectPayment> unPayed = paymentsByPayed.get(false);
                if (CollectionUtils.isNotEmpty(unPayed)) {
                    builder.append("❗").append("Должники:").append("❗").append(EOL);
                    for (CollectPayment unPayedPayment : unPayed) {
                        builder.append(unPayedPayment.getPlayer()).append(EOL);
                    }
                } else {
                    builder.append("Все сдали").append("❤").append(EOL);
                }
                List<CollectPayment> payedPayments = paymentsByPayed.get(true);
                if (payedPayments != null) {
                    int bankSum = getPaymentsSum(payedPayments, CollectType.BANK);
                    int cashSum = getPaymentsSum(payedPayments, CollectType.CASH);
                    int coachSum = getPaymentsSum(payedPayments, CollectType.COACH);
                    builder.append("\uD83C\uDFE6").append("Собрано карта: ").append(bankSum).append(EOL);
                    builder.append("\uD83D\uDCB5").append("Собрано наличные: ").append(cashSum).append(EOL);
                    builder.append("🙎‍♂️").append("Собрано тренером: ").append(coachSum).append(EOL).append(EOL);
                    totalCard += bankSum;
                    totalCash += cashSum;
                    totalCoach += coachSum;
                }
            }
        }
        builder.append("Итого: ").append(EOL)
                .append("Карта: ").append(totalCard).append(EOL)
                .append("Наличные:").append(totalCash).append(EOL)
                .append("Тренеру:").append(totalCoach).append(EOL);
        return MessageUtils.buildDashboardHolder(builder.toString());
    }

    private int getPaymentsSum(List<CollectPayment> payments, CollectType type) {
        return payments.stream().filter(p -> type == p.getType()).mapToInt(CollectPayment::getSumBill).sum();
    }



    @Override
    public CommandType getNextCommandType() {
        return CommandType.DASHBOARD_PROCESSOR;
    }

    @Override
    public String getHelperParamsValue() {
        return null;
    }
}
