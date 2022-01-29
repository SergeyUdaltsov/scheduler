package com.scheduler.dao;

import com.scheduler.model.Bill;
import com.scheduler.model.PaymentType;

import java.util.List;

/**
 * @author Serhii_Udaltsov on 5/4/2021
 */
public interface IBillDao {

    void save(Bill bill);

    List<Bill> getLastBills(PaymentType type);

    void remove(Bill bill);

    List<Bill> getAllBills();

    List<Bill> getMonthlyBills();
}
