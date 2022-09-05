package com.scheduler.service.impl;

import com.scheduler.Constants;
import com.scheduler.dao.ISettingDao;
import com.scheduler.model.Role;
import com.scheduler.service.ISettingService;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * @author Serhii_Udaltsov on 6/5/2021
 */
@RequiredArgsConstructor
public class SettingService implements ISettingService {

    private final ISettingDao settingDao;

    @Override
    public Map<Role, List<String>> getRoleMapping() {
        return settingDao.getRoleMapping();
    }

    @Override
    public int getSessionDuration() {
        return settingDao.getSessionDuration();
    }

    @Override
    public boolean isLocalizationLoggingEnabled() {
        return settingDao.getBooleanSettingValue(Constants.LOCALIZATION_LOGGING_ENABLED_SETTING, true);
    }
}
