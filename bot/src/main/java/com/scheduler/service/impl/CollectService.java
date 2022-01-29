package com.scheduler.service.impl;

import com.scheduler.dao.ICollectPaymentDao;
import com.scheduler.model.Collect;
import com.scheduler.model.CollectPayment;
import com.scheduler.model.CollectType;
import com.scheduler.model.Player;
import com.scheduler.service.ICollectService;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

/**
 * @author Serhii_Udaltsov on 1/4/2022
 */
public class CollectService implements ICollectService {

    private ICollectPaymentDao collectPaymentDao;

    public CollectService(ICollectPaymentDao collectPaymentDao) {
        this.collectPaymentDao = collectPaymentDao;
    }

    @Override
    public void createCollectBills(List<Player> players, Collect collect, long operatorId) {
        collectPaymentDao.createCollectBills(players, collect, operatorId);
    }

    @Override
    public void deleteCollect(String collectName, long operatorId) {
        collectPaymentDao.deleteCollectPaymentsByCollectName(collectName, operatorId);
    }

    @Override
    public void createCollectPayment(Collect collect, Update update, String playerName, CollectType collectType) {
        collectPaymentDao.createCollectPayment(collect, update, playerName, collectType);
    }

    @Override
    public List<CollectPayment> listAllCollects(long operatorId) {
        return collectPaymentDao.listAllCollects(operatorId);
    }

    @Override
    public List<CollectPayment> listCollectPayments(String collectName, long operatorId, boolean payed) {
        return collectPaymentDao.listCollectPayments(collectName, operatorId, payed);
    }

    @Override
    public void deleteCollectPayment(String playerName, String collectName) {
        collectPaymentDao.deleteCollectPayment(playerName, collectName);
    }

    @Override
    public List<CollectPayment> collectPaymentsByName(String collectName, long operatorId) {
        return collectPaymentDao.collectPaymentsByName(collectName, operatorId);
    }
}
