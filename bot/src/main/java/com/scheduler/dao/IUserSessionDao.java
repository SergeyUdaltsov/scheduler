package com.scheduler.dao;

import java.util.List;

/**
 * @author Serhii_Udaltsov on 6/6/2021
 */
public interface IUserSessionDao {

    List<String> getUserActionList(long operatorId);

    void saveUserSession(long operatorId, List<String> actions);

}
