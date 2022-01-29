package com.scheduler.service;

import com.scheduler.model.PaymentType;
import com.scheduler.model.Role;

import java.util.List;
import java.util.Map;

/**
 * @author Serhii_Udaltsov on 5/3/2021
 */
public interface ISettingService {

    int getPaymentSum(PaymentType paymentType);

    Map<Role, List<String>> getRoleMapping();

    int getSessionDuration();
}
