package com.scheduler.dao;

import com.scheduler.model.Collect;
import com.scheduler.model.CollectPayment;
import com.scheduler.model.CollectType;
import com.scheduler.model.Player;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

/**
 * @author Serhii_Udaltsov on 1/4/2022
 */
public interface ICollectPaymentDao {

    void createCollectBills(List<Player> players, Collect collect, long operatorId);

    void deleteCollectPaymentsByCollectName(String collectName, long operatorId);

    void createCollectPayment(Collect collect, Update update, String playerName, CollectType collectType);

    List<CollectPayment> listAllCollects(long operatorId);

    List<CollectPayment> listCollectPayments(String collectName, long operatorId, boolean payed);

    List<CollectPayment> collectPaymentsByName(String collectName, long operatorId);

    void deleteCollectPayment(String playerName, String collectName);
}
