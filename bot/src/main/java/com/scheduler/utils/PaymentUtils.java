package com.scheduler.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.scheduler.model.Bill;
import com.scheduler.model.Context;
import com.scheduler.model.Payment;
import com.scheduler.model.PaymentType;
import com.scheduler.model.Player;
import com.scheduler.model.Transfer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Serhii_Udaltsov on 4/30/2021
 */
public class PaymentUtils {
    public static Map<String, List<Payment>> buildPaymentsByPlayersMap(List<Payment> payments) {
        return payments.stream().collect(Collectors.groupingBy(Payment::getName));
    }

    public static Map<PaymentType, Integer> buildEmptyPaymentBalances() {
        Map<PaymentType, Integer> resultMap = new HashMap<>();
        for (PaymentType type : PaymentType.values()) {
            resultMap.put(type, 0);
        }
        return resultMap;
    }

    public static Map<PaymentType, Integer> mergePaymentsWithBills(Map<PaymentType, Integer> payments,
                                                                   List<Bill> bills) {
        Map<PaymentType, List<Bill>> billsMap = bills.stream()
                .collect(Collectors.groupingBy(Bill::getType));
        for (Map.Entry<PaymentType, List<Bill>> entry : billsMap.entrySet()) {
            List<Bill> billsByType = entry.getValue();
            int billSum = billsByType.stream().mapToInt(Bill::getSum).sum();
            PaymentType billType = entry.getKey();
            Integer paymentSum = payments.getOrDefault(billType, 0);
            payments.put(billType, paymentSum - billSum);
        }
        return payments;
    }

    public static Map<PaymentType, Integer> buildPaymentBalances(List<Payment> payments) {
        if (CollectionUtils.isEmpty(payments)) {
            return buildEmptyPaymentBalances();
        }
        Map<PaymentType, List<Payment>> paymentsByTypes = payments.stream()
                .collect(Collectors.groupingBy(Payment::getType));
        Map<PaymentType, Integer> paymentsBalances = new HashMap<>();
        for (Map.Entry<PaymentType, List<Payment>> entry : paymentsByTypes.entrySet()) {
            List<Payment> paymentsByType = entry.getValue();
            int sum = paymentsByType.stream().mapToInt(Payment::getSum).sum();
            paymentsBalances.put(entry.getKey(), sum);
        }
        List<Payment> sundayIcePayments = paymentsByTypes.get(PaymentType.SUNDAY_ICE);
        if (CollectionUtils.isEmpty(sundayIcePayments)) {
            paymentsBalances.put(PaymentType.SUNDAY_ICE, 0);
            return paymentsBalances;
        }
        int sundayIceBalance = sundayIcePayments.stream()
                .mapToInt(p -> p.getSum() - p.getSumBill())
                .sum();
        paymentsBalances.put(PaymentType.SUNDAY_ICE, sundayIceBalance);

        return paymentsBalances;
    }

    public static List<String> getPaymentTitlesAndFillParams(List<Payment> payments, Map<String, Object> params, boolean isPayment) {
        Function<Payment, Integer> sumFunc = isPayment ? Payment::getSum : Payment::getSumBill;
        Comparator<Payment> comparator = Comparator.comparingLong(Payment::getDate);
        Function<Payment, String> titleFunction = p -> String.format("%s %s %s \"%s\"%s",
                DateUtils.convertToUserDate(p.getDate()), p.getName(), sumFunc.apply(p), p.getType().getValue(),
                p.isHidden() ? " Льг" : StringUtils.EMPTY_STRING);
        return getTitlesMap(payments, comparator, titleFunction, "payments", params);
    }

    public static String getPaymentTitles(List<Payment> payments) {
        List<String> results = payments.stream()
                .filter(p -> !p.isHidden())
                .map(p -> String.format("%s %-13s %4s \"%s\"",
                        DateUtils.convertToUserDate(p.getDate()), p.getName(), p.getSum(),
                        p.getType().getValue()))
                .collect(Collectors.toList());
        return String.join("\n", results);
    }

    public static List<String> getBillTitlesAndFillParams(List<Bill> bills, Map<String, Object> params) {
        Comparator<Bill> comparator = Comparator.comparingInt(Bill::getMonth);
        Function<Bill, String> titleFunction = b -> String.format("Месяц: %s, сумма:  %s",
                MessageUtils.getMonthName(b.getMonth()), b.getSum());
        return getTitlesMap(bills, comparator, titleFunction, "bills", params);
    }

    public static <T> List<String> getTitlesMap(List<T> items, Comparator<T> comparator,
                                                Function<T, String> titleFunction, String paramKey,
                                                Map<String, Object> params) {
        items.sort(comparator);
        Map<String, T> itemsMap = new LinkedHashMap<>();
        for (int i = 0; i < items.size(); i++) {
            T item = items.get(i);
            String title = i + 1 + ". " + titleFunction.apply(item);
            itemsMap.put(title, item);
        }
        params.put(paramKey, itemsMap);
        return new ArrayList<>(itemsMap.keySet());
    }

    public static Payment getPaymentFromParams(Map<String, Object> params, String paymentTitle) {
        Map<String, Object> paymentMap = (Map) params.get("payments");
        return JsonUtils.getObjectFromJsonString(Payment.class,
                JsonUtils.convertObjectToString(paymentMap.get(paymentTitle)));
    }

    public static Bill getBillFromParams(Map<String, Object> params, String billTitle) {
        Map<String, Object> billMap = (Map) params.get("bills");
        return JsonUtils.getObjectFromJsonString(Bill.class,
                JsonUtils.convertObjectToString(billMap.get(billTitle)));
    }

    public static Payment getSelectedPayment(Context context) {
        Map<String, Object> params = context.getParams();
        Map payments = (Map) params.get("payments");
        Map paymentMap = (Map) payments.get("payment");
        return JsonUtils.parseMap(paymentMap, new TypeReference<Payment>() {
        });
    }

    public static Bill getSelectedBill(Context context) {
        Map<String, Object> params = context.getParams();
        return getBillFromParams(params, "bill");
    }

    public static File createAllPaymentsFile(List<Payment> payments, List<Transfer> transfers) {
        try {
            File file = new File("/tmp/allPlayersPayments.txt");
            FileOutputStream fos = new FileOutputStream(file);
            OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
            BufferedWriter writer = new BufferedWriter(osw);
            writer.append(String.format("%18s%15s %15s %-4s %4s %14s %10s%n", "Дата", "Игрок", "Тип", "Опл", "Нач",
                    "Оператор", "Опер/Архив"));
            int totalSum = 0;
            for (Payment payment : payments) {
                totalSum += payment.getSum();
                writer.append(String.format("%18s%15s %15s %-4s %4s %-4s %-10s %-15s%n",
                        DateUtils.convertToUserDate(payment.getDate()), payment.getName(), payment.getType().getValue(),
                        payment.getSum(), payment.getSumBill(), payment.isHidden() ? "Льг" : "", payment.getOperatorId(),
                        payment.getOperator()));
            }
            writer.append(String.format("%s%47s%n%n%n", "Итого платежи: ", totalSum));
            if (CollectionUtils.isNotEmpty(transfers)) {
                int transferSum = transfers.stream().mapToInt(Transfer::getSum).sum();
                for (Transfer transfer : transfers) {
                    writer.append(String.format("%20s %-6s %-10s%n", DateUtils.convertToUserDate(transfer.getDate()),
                            transfer.getSum(), transfer.getType().getValue()));
                }
                writer.append(String.format("%s%46s", "Итого переводы: ", transferSum));
            }
            writer.close();
            return file;
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalStateException("File with payments was not created");
        }
    }

    public static File createPlayerPaymentsFile(List<Payment> payments, List<Bill> bills, String playerName) {
        try {
            File file = new File("/tmp/playerPayments.txt");
            FileOutputStream fos = new FileOutputStream(file);
            OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
            BufferedWriter writer = new BufferedWriter(osw);
            writer.append("Платежи по игроку: ").append(playerName).append("\n");
            writer.append(String.format("%18s%15s %4s %-4s", "Дата", "Тип", "Опл", "Нач"))
                    .append("\n");
            for (Payment payment : payments) {
                writer.append(String.format("%s%15s %4s %-4s %s%n", DateUtils.convertToUserDate(payment.getDate()),
                        payment.getType().getValue(), payment.getSum(), payment.getSumBill(),
                        payment.isHidden() ? "Льг" : StringUtils.EMPTY_STRING));
            }
            writer.append("\nНачисления\n");
            for (Bill bill : bills) {
                writer.append(String.format("%4s%15s %4s%n", MessageUtils.getMonthName(bill.getMonth()),
                        bill.getType().getValue(), bill.getSum()));
            }
            writer.close();
            return file;
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalStateException("File with player payments was not created");
        }
    }

    public static Map<String, Map<PaymentType, Integer>> getPlayerBalancesV2(List<Payment> allPayments,
                                                                             List<Bill> allBills,
                                                                             List<Player> allPlayers) {
        List<String> allPlayerNames = allPlayers.stream()
                .map(Player::getName)
                .collect(Collectors.toList());
        Map<PaymentType, Integer> billsMap = allBills.stream()
                .collect(Collectors.groupingBy(Bill::getType, Collectors.summingInt(Bill::getSum)));
        Map<String, List<Payment>> paymentsByPlayer = allPayments.stream()
                .collect(Collectors.groupingBy(Payment::getName));
        paymentsByPlayer.entrySet().removeIf(entry -> !allPlayerNames.contains(entry.getKey()));

        allPlayerNames.stream()
                .filter(p -> !paymentsByPlayer.containsKey(p))
                .forEach(p -> paymentsByPlayer.put(p, Collections.emptyList()));

        Map<String, Map<PaymentType, Integer>> playerBalances = new HashMap<>();
        for (Map.Entry<String, List<Payment>> entry : paymentsByPlayer.entrySet()) {
            String player = entry.getKey();
            List<Payment> payments = entry.getValue();
            Map<PaymentType, Integer> playerBalance = getPlayerBalance(payments, billsMap);
            playerBalances.put(player, playerBalance);
        }
        return playerBalances;
    }

    public static Map<String, Map<PaymentType, Integer>> getPlayerBalances(List<Payment> allPayments,
                                                                           List<Bill> allBills,
                                                                           List<Player> allPlayers) {
        List<String> allPlayerNames = allPlayers.stream()
                .map(Player::getName)
                .collect(Collectors.toList());
        Map<PaymentType, Integer> billsMap = allBills.stream()
                .collect(Collectors.groupingBy(Bill::getType, Collectors.summingInt(Bill::getSum)));
        Map<String, List<Payment>> paymentsByPlayer = allPayments.stream()
                .collect(Collectors.groupingBy(Payment::getName));
        paymentsByPlayer.entrySet().removeIf(entry -> !allPlayerNames.contains(entry.getKey()));
        allPlayerNames.stream()
                .filter(p -> !paymentsByPlayer.containsKey(p))
                .forEach(p -> paymentsByPlayer.put(p, Collections.emptyList()));
        Map<String, Map<PaymentType, Integer>> playerBalances
                = new HashMap<>();
        for (Map.Entry<String, List<Payment>> entry : paymentsByPlayer.entrySet()) {
            Map<PaymentType, Integer> playerBalance = new HashMap<>();
            List<Payment> payments = entry.getValue();
            Map<PaymentType, List<Payment>> paymentsByTypes;
            if (CollectionUtils.isNotEmpty(payments)) {
                paymentsByTypes = payments.stream()
                        .collect(Collectors.groupingBy(Payment::getType));
                List<Payment> sundayIcePayments = paymentsByTypes.getOrDefault(PaymentType.SUNDAY_ICE, Collections.emptyList());
                int sum = 0;
                int sumBill = 0;
                for (Payment payment : sundayIcePayments) {
                    sum += payment.getSum();
                    sumBill += payment.getSumBill();
                }
                int resSum = sum - sumBill;
                playerBalance.put(PaymentType.SUNDAY_ICE, resSum);
                for (Map.Entry<PaymentType, List<Payment>> paymentEntry : paymentsByTypes.entrySet()) {
                    PaymentType paymentType = paymentEntry.getKey();
                    if (PaymentType.SUNDAY_ICE == paymentType) {
                        continue;
                    }
                    Integer billSum = billsMap.getOrDefault(paymentType, 0);
                    int paymentsSum = paymentEntry.getValue().stream().mapToInt(Payment::getSum).sum();
                    playerBalance.put(paymentType, paymentsSum - billSum);
                }
                playerBalances.put(entry.getKey(), playerBalance);
                continue;
            }
            for (Map.Entry<PaymentType, Integer> billsEntry : billsMap.entrySet()) {
                playerBalance.put(billsEntry.getKey(), -billsEntry.getValue());
            }
            playerBalances.put(entry.getKey(), playerBalance);
        }
        return playerBalances;
    }

    private static Map<PaymentType, Integer> getPlayerBalance(List<Payment> payments, Map<PaymentType, Integer> billsMap) {
        Map<PaymentType, Integer> playerBalance = new HashMap<>();
        Map<PaymentType, List<Payment>> paymentsByTypes = payments.stream()
                .collect(Collectors.groupingBy(Payment::getType));
        List<Payment> sundayIcePayments = paymentsByTypes.getOrDefault(PaymentType.SUNDAY_ICE, Collections.emptyList());
        int sundayIceSum = getSundayIceSum(sundayIcePayments);
        playerBalance.put(PaymentType.SUNDAY_ICE, sundayIceSum);

        Set<PaymentType> allTypes = new HashSet<>(paymentsByTypes.keySet());
        allTypes.addAll(billsMap.keySet());
        allTypes.removeIf(t -> PaymentType.SUNDAY_ICE == t);

        for (PaymentType type : allTypes) {
            Integer billSum = billsMap.getOrDefault(type, 0);
            List<Payment> paymentList = paymentsByTypes.getOrDefault(type, Collections.emptyList());
            int paymentsSum = paymentList.stream().mapToInt(Payment::getSum).sum();
            playerBalance.put(type, paymentsSum - billSum);
        }
       return playerBalance;
    }

    private static int getSundayIceSum(List<Payment> sundayIcePayments) {
        int result = 0;
        for (Payment payment : sundayIcePayments) {
            result += payment.getSum();
            result -= payment.getSumBill();
        }
        return result;
    }
}
