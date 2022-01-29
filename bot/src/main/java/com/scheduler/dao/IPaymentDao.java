package com.scheduler.dao;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.scheduler.model.Payment;

import java.util.List;

/**
 * @author Serhii_Udaltsov on 4/14/2021
 */
public interface IPaymentDao {

    void save(Payment payment);

    void saveAll(List<Item> payments);

    List<Payment> getLastPaymentsByPlayer(String playerName, long operatorId);

    void remove(Payment payment);

    void removeAllPayments(List<Payment> payments);

    List<Payment> getLastSundayIceBills(String playerName, long operatorId);

    List<Payment> getAllPayments();

    List<Payment> getPaymentsByPlayer(String playerName);

    List<Payment> getPaymentsPortion(long from, long operatorId, boolean isPayment);

    List<Payment> getPaymentsByOperator(long operatorId);

    List<Payment> getAllPaymentsByOperator(long operatorId);

    List<Payment> getMonthlyPaymentsByOperator(long operatorId);

    List<Payment> getLastSundayPayments(long operatorId);
}
