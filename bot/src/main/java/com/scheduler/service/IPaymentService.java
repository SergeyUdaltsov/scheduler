package com.scheduler.service;

import com.scheduler.model.Payment;

import java.util.List;

/**
 * @author Serhii_Udaltsov on 4/14/2021
 */
public interface IPaymentService {

    void save(Payment payment);

    List<Payment> getLastPaymentsByPlayerAndOperator(String playerName, long operatorId);

    void remove(Payment payment);

    List<Payment> getLastSundayIceBills(String playerName, long operatorId);

    List<Payment> getAllPayments();

    List<Payment> getPaymentsByPlayer(String playerName);

    List<Payment> getPaymentsPortion(long from, long operatorId);

    List<Payment> getPaymentsByOperator(long operatorId);

    List<Payment> getAllPaymentsByOperator(long operatorId);

    List<Payment> getMonthlyPaymentsByOperator(long operatorId);

    List<Payment> getSundayPayments(long operatorId);

    List<Payment> archivePayments();
}
