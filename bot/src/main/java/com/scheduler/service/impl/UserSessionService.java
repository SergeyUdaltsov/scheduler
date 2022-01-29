package com.scheduler.service.impl;

import com.scheduler.dao.IUserSessionDao;
import com.scheduler.model.Role;
import com.scheduler.model.User;
import com.scheduler.service.ISettingService;
import com.scheduler.service.IUserService;
import com.scheduler.service.IUserSessionService;
import com.scheduler.utils.CollectionUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Serhii_Udaltsov on 6/6/2021
 */
public class UserSessionService implements IUserSessionService {

    private IUserSessionDao userSessionDao;
    private ISettingService settingService;
    private IUserService userService;
    private Map<Long, List<String>> cachedUserActions = new HashMap<>();

    public UserSessionService(IUserSessionDao userSessionDao, ISettingService settingService,
                              IUserService userService) {
        this.userSessionDao = userSessionDao;
        this.settingService = settingService;
        this.userService = userService;
    }

    @Override
    public List<String> getUserActionList(long operatorId) {
        List<String> userActions = cachedUserActions.get(operatorId);
        if (CollectionUtils.isNotEmpty(userActions)) {
            return userActions;
        }
        List<String> actionListFromDb = userSessionDao.getUserActionList(operatorId);
        if (CollectionUtils.isNotEmpty(userActions)) {
            cachedUserActions.put(operatorId, actionListFromDb);
            return actionListFromDb;
        }
        User user = userService.getUserById(operatorId);
        if (user == null) {
            return Collections.emptyList();
        }

        Role role = user.getRole();
        Map<Role, List<String>> roleMapping = settingService.getRoleMapping();
        List<String> actions = roleMapping.get(role);
        if (CollectionUtils.isEmpty(actions)) {
            return Collections.emptyList();
        }
        cachedUserActions.put(operatorId, actions);
        userSessionDao.saveUserSession(operatorId, actions);
        return actions;
    }
}
