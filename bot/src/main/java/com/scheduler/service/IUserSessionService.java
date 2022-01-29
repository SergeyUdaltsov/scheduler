package com.scheduler.service;

import java.util.List;

/**
 * @author Serhii_Udaltsov on 6/6/2021
 */
public interface IUserSessionService {

    List<String> getUserActionList(long operatorId);
}
