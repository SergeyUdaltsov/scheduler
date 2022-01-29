package com.scheduler.model;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Serhii_Udaltsov on 4/14/2021
 */
public enum PaymentType {
    MONTHLY_COACH("Тренерские", true),
    SUNDAY_ICE("Воскресный лед", true),
    CUSTOM_PAYMENT("Произвольный платеж", false),
    MONTHLY_ICE("Лед", true);

    private String value;
    private boolean isForUi;

    PaymentType(String value, boolean isForUi) {
        this.isForUi = isForUi;
        this.value = value;
    }

    public static PaymentType fromValue(String value) {
        for (PaymentType paymentType : values()) {
            if (paymentType.getValue().equalsIgnoreCase(value)) {
                return paymentType;
            }
        }
        try{
            Integer.parseInt(value);
            return CUSTOM_PAYMENT;
        } catch (NumberFormatException ex) {
            throw new RuntimeException("Сумма произвольного платежа введена неверно");
        }
    }

    public static List<PaymentType> uiTypes() {
        return Stream.of(values()).filter(PaymentType::isForUi).collect(Collectors.toList());
    }

    public boolean isForUi() {
        return isForUi;
    }

    public String getValue() {
        return value;
    }

}
