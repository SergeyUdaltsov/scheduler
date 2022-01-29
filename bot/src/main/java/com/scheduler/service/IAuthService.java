package com.scheduler.service;

import java.util.Map;

/**
 * @author Serhii_Udaltsov on 10/4/2021
 */
public interface IAuthService {

    String validateUser(Map<String, Object> params);

    String generateToken(Map<String, Object> params);

}
