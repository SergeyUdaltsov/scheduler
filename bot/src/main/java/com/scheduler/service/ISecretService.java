package com.scheduler.service;

/**
 * @author Serhii_Udaltsov on 6/12/2021
 */
public interface ISecretService {

    String getBotToken();

    String getUserPasswordByName(String userName);

    String getParameterValue(String paramName);
}
