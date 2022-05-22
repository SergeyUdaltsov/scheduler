package com.scheduler.dao;

import com.scheduler.model.Role;

import java.util.List;
import java.util.Map;

/**
 * @author Serhii_Udaltsov on 6/5/2021
 */
public interface ISettingDao {

    Map<Role, List<String>> getRoleMapping();

    int getSessionDuration();
}
