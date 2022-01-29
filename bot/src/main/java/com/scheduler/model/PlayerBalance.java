package com.scheduler.model;

import java.util.Map;

/**
 * @author Serhii_Udaltsov on 6/18/2021
 */
public class PlayerBalance {

    private String playerName;
    private Map<PaymentType, Integer> balanceMap;
    private int monthlyIceBalance;
    private int monthlyCoachBalance;
    private int sundayIceBalance;
    private int customPaymentBalance;

    public PlayerBalance(String playerName, Map<PaymentType, Integer> balanceMap) {
        this.playerName = playerName;
        this.monthlyIceBalance = balanceMap.getOrDefault(PaymentType.MONTHLY_ICE, 0);
        this.monthlyCoachBalance = balanceMap.getOrDefault(PaymentType.MONTHLY_COACH, 0);
        this.sundayIceBalance = balanceMap.getOrDefault(PaymentType.SUNDAY_ICE, 0);
        this.customPaymentBalance = balanceMap.getOrDefault(PaymentType.CUSTOM_PAYMENT, 0);
        this.balanceMap = balanceMap;
    }

    public int getMonthlyIceBalance() {
        return monthlyIceBalance;
    }

    public void setMonthlyIceBalance(int monthlyIceBalance) {
        this.monthlyIceBalance = monthlyIceBalance;
    }

    public int getMonthlyCoachBalance() {
        return monthlyCoachBalance;
    }

    public void setMonthlyCoachBalance(int monthlyCoachBalance) {
        this.monthlyCoachBalance = monthlyCoachBalance;
    }

    public int getSundayIceBalance() {
        return sundayIceBalance;
    }

    public void setSundayIceBalance(int sundayIceBalance) {
        this.sundayIceBalance = sundayIceBalance;
    }

    public int getCustomPaymentBalance() {
        return customPaymentBalance;
    }

    public void setCustomPaymentBalance(int customPaymentBalance) {
        this.customPaymentBalance = customPaymentBalance;
    }

    public PlayerBalance(String playerName) {
        this.playerName = playerName;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public Map<PaymentType, Integer> getBalanceMap() {
        return balanceMap;
    }

    public void setBalanceMap(Map<PaymentType, Integer> balanceMap) {
        this.balanceMap = balanceMap;
    }
}
