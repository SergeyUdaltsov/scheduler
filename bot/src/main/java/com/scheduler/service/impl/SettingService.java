package com.scheduler.service.impl;

import com.scheduler.dao.ISettingDao;
import com.scheduler.model.PaymentType;
import com.scheduler.model.Role;
import com.scheduler.service.ISettingService;

import java.util.List;
import java.util.Map;

/**
 * @author Serhii_Udaltsov on 6/5/2021
 */
public class SettingService implements ISettingService {

    private ISettingDao settingDao;

    public SettingService(ISettingDao settingDao) {
        this.settingDao = settingDao;
    }

    @Override
    public int getPaymentSum(PaymentType paymentType) {
        return settingDao.getPaymentSum(paymentType);
    }

    @Override
    public Map<Role, List<String>> getRoleMapping() {
        return settingDao.getRoleMapping();
    }

    @Override
    public int getSessionDuration() {
        return settingDao.getSessionDuration();
    }
}
