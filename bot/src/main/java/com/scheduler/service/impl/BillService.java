package com.scheduler.service.impl;

import com.scheduler.dao.IBillDao;
import com.scheduler.model.Bill;
import com.scheduler.model.PaymentType;
import com.scheduler.service.IBillService;

import java.util.List;

/**
 * @author Serhii_Udaltsov on 5/4/2021
 */
public class BillService implements IBillService {

    private IBillDao billDao;

    public BillService(IBillDao billDao) {
        this.billDao = billDao;
    }

    @Override
    public void save(Bill bill) {
        billDao.save(bill);
    }

    @Override
    public List<Bill> getLastBills(PaymentType type) {
        return billDao.getLastBills(type);
    }

    @Override
    public void remove(Bill bill) {
        billDao.remove(bill);
    }

    @Override
    public List<Bill> getAllBills() {
        return billDao.getAllBills();
    }

    @Override
    public List<Bill> getMonthlyBills() {
        return billDao.getAllBills();
    }
}
