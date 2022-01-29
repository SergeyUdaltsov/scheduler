package com.scheduler.service.impl;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.scheduler.dao.IPaymentDao;
import com.scheduler.model.Payment;
import com.scheduler.model.PaymentType;
import com.scheduler.service.IPaymentService;
import com.scheduler.utils.CollectionUtils;
import com.scheduler.utils.DateUtils;
import com.scheduler.utils.PaymentUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Serhii_Udaltsov on 4/14/2021
 */
public class PaymentService implements IPaymentService {

    private IPaymentDao paymentDao;

    public PaymentService(IPaymentDao paymentDao) {
        this.paymentDao = paymentDao;
    }

    @Override
    public void save(Payment payment) {
        paymentDao.save(payment);
    }

    public List<Payment> getLastPaymentsByPlayerAndOperator(String playerName, long operatorId) {
        return paymentDao.getLastPaymentsByPlayer(playerName, operatorId);
    }

    @Override
    public List<Payment> getSundayPayments(long operatorId) {
        return paymentDao.getLastSundayPayments(operatorId);
    }

    @Override
    public List<Payment> getMonthlyPaymentsByOperator(long operatorId) {
        return paymentDao.getPaymentsByOperator(operatorId);
    }

    @Override
    public void remove(Payment payment) {
        paymentDao.remove(payment);
    }


    @Override
    public List<Payment> getLastSundayIceBills(String playerName, long operatorId) {
        return paymentDao.getLastSundayIceBills(playerName, operatorId);
    }

    @Override
    public List<Payment> getAllPayments() {
        return paymentDao.getAllPayments();
    }

    @Override
    public List<Payment> getPaymentsPortion(long from, long operatorId) {
        return paymentDao.getPaymentsPortion(from, operatorId, true);
    }

    @Override
    public List<Payment> getPaymentsByPlayer(String playerName) {
        return paymentDao.getPaymentsByPlayer(playerName);
    }

    @Override
    public List<Payment> getPaymentsByOperator(long operatorId) {
        return paymentDao.getPaymentsByOperator(operatorId);
    }

    @Override
    public List<Payment> getAllPaymentsByOperator(long operatorId) {
        return paymentDao.getAllPaymentsByOperator(operatorId);
    }

    @Override
    public List<Payment> archivePayments() {
        List<Payment> allPayments = getAllPayments();
        if (CollectionUtils.isEmpty(allPayments)) {
            return allPayments;
        }
        List<Payment> updatedPayments = new ArrayList<>();
        Map<String, List<Payment>> paymentsByPlayersMap = PaymentUtils.buildPaymentsByPlayersMap(allPayments);
        for (Map.Entry<String, List<Payment>> entry : paymentsByPlayersMap.entrySet()) {
            updatedPayments.addAll(mergePlayerPayments(entry.getValue(), entry.getKey()));
        }
        paymentDao.removeAllPayments(allPayments);

        List<Item> paymentsToSave = convertPaymentsToItems(updatedPayments);
        paymentDao.saveAll(paymentsToSave);
        return allPayments;
    }

    private List<Item> convertPaymentsToItems(List<Payment> payments) {
        return payments.stream().map(p -> new Item()
                .withPrimaryKey("n", p.getName(), "d", p.getDate())
                .withNumber("s", p.getSum())
                .withNumber("sb", p.getSumBill())
                .withNumber("oId", p.getOperatorId())
                .withString("o", p.getOperator())
                .withString("t", p.getType().name())
                .withNumber("h", p.isHidden() ? 1 : 0))
                .collect(Collectors.toList());
    }

    private List<Payment> mergePlayerPayments(List<Payment> payments, String playerName) {
        Map<Long, List<Payment>> paymentsByOperator = payments.stream()
                .collect(Collectors.groupingBy(Payment::getOperatorId));
        List<Payment> resultPayments = new ArrayList<>();

        for (Map.Entry<Long, List<Payment>> operatorEntry : paymentsByOperator.entrySet()) {
            List<Payment> operatorPayments = operatorEntry.getValue();
            Long operatorId = operatorEntry.getKey();
            Map<PaymentType, List<Payment>> paymentsByTypeMap = operatorPayments.stream()
                    .collect(Collectors.groupingBy(Payment::getType));

            List<Payment> sundayIcePayments = paymentsByTypeMap.remove(PaymentType.SUNDAY_ICE);
            resultPayments.addAll(mergeSundayIcePayments(sundayIcePayments, operatorId, playerName));
            for (Map.Entry<PaymentType, List<Payment>> paymentsEntry : paymentsByTypeMap.entrySet()) {
                List<Payment> paymentsByType = paymentsEntry.getValue();
                resultPayments.addAll(mergePaymentsOfSameType(paymentsByType, operatorId, playerName,
                        paymentsEntry.getKey()));
            }
        }
        return resultPayments;
    }

    private List<Payment> mergeSundayIcePayments(List<Payment> payments, long operatorId, String playerName) {
        if (CollectionUtils.isEmpty(payments)) {
            return Collections.emptyList();
        }
        List<Payment> resultPayments = new ArrayList<>();
        Map<Boolean, List<Payment>> paymentsByHidden = payments.stream()
                .collect(Collectors.groupingBy(Payment::isHidden));
        for (Map.Entry<Boolean, List<Payment>> entry : paymentsByHidden.entrySet()) {
            List<Payment> paymentsList = entry.getValue();
            int sum = paymentsList.stream().mapToInt(Payment::getSum).sum();
            int sumBill = paymentsList.stream().mapToInt(Payment::getSumBill).sum();
            Payment sundayIcePayment = new Payment(playerName, sum, DateUtils.uniqueCurrentTime(),
                    PaymentType.SUNDAY_ICE, "архивный", operatorId, sumBill, entry.getKey());
            resultPayments.add(sundayIcePayment);
        }
        return resultPayments;
    }

    private List<Payment> mergePaymentsOfSameType(List<Payment> payments, long operatorId, String playerName,
                                                  PaymentType type) {
        List<Payment> resultPayments = new ArrayList<>();
        Map<Boolean, List<Payment>> paymentsByHidden = payments.stream()
                .collect(Collectors.groupingBy(Payment::isHidden));
        for (Map.Entry<Boolean, List<Payment>> entry : paymentsByHidden.entrySet()) {
            List<Payment> paymentsList = entry.getValue();
            int sum = paymentsList.stream().mapToInt(Payment::getSum).sum();
            Payment payment = new Payment(playerName, sum, DateUtils.uniqueCurrentTime(),
                    type, "архивный", operatorId, 0, entry.getKey());
            resultPayments.add(payment);
        }
        return resultPayments;
    }
}
