package com.scheduler.service.impl;

import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagement;
import com.amazonaws.services.simplesystemsmanagement.model.GetParameterRequest;
import com.amazonaws.services.simplesystemsmanagement.model.GetParameterResult;
import com.amazonaws.services.simplesystemsmanagement.model.Parameter;
import com.scheduler.service.ISecretService;
import com.scheduler.utils.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Serhii_Udaltsov on 6/12/2021
 */
public class SecretService implements ISecretService {
    private static final String BOT_PARAMETER_NAME = "botToken";

    private AWSSimpleSystemsManagement manager;
    private Map<String, String> paramsCache = new HashMap<>();

    public SecretService(AWSSimpleSystemsManagement manager) {
        this.manager = manager;
    }

    @Override
    public String getBotToken() {
        String tokenFromCache = paramsCache.get(BOT_PARAMETER_NAME);
        if (!StringUtils.isBlank(tokenFromCache)) {
            return tokenFromCache;
        }
        String tokenFromDb = getParameterValue(BOT_PARAMETER_NAME);
        paramsCache.put(BOT_PARAMETER_NAME, tokenFromDb);
        return tokenFromDb;
    }

    @Override
    public String getUserPasswordByName(String userName) {
        String paramName = String.format("coach_helper.%s.credentials", userName);
        return getParameterValue(paramName);
    }

    @Override
    public String getParameterValue(String paramName) {
        String valueFromCache = paramsCache.get(paramName);
        if (!StringUtils.isBlank(valueFromCache)) {
            return valueFromCache;
        }
        GetParameterRequest request = new GetParameterRequest();
        request.setName(paramName);
        request.withWithDecryption(true);
        GetParameterResult result = manager.getParameter(request);
        if (result == null) {
            String errorMessage = "Secret not found for name " + paramName;
            System.out.println("ERROR ---- " + errorMessage);
            throw new IllegalArgumentException(errorMessage);
        }
        Parameter parameter = result.getParameter();
        String value = parameter.getValue();
        if (StringUtils.isBlank(value)) {
            String errorMessage = "Value is blank for secret " + paramName;
            System.out.println("ERROR ---- " + errorMessage);
            throw new IllegalStateException(errorMessage);
        }
        paramsCache.put(paramName, value);
        return value;
    }
}
