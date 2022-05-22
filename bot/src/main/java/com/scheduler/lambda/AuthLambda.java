package com.scheduler.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.fasterxml.jackson.core.type.TypeReference;
import com.scheduler.dagger.DaggerLambdaComponent;
import com.scheduler.model.request.BotResponse;
import com.scheduler.service.IAuthService;
import com.scheduler.service.ISecretService;
import com.scheduler.utils.JsonUtils;

import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Serhii_Udaltsov on 10/3/2021
 */
public class AuthLambda implements RequestStreamHandler {

    private ISecretService secretService;
    private IAuthService authService;

    public AuthLambda() {
        DaggerLambdaComponent.create().inject(this);
    }

    @Override
    public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) throws IOException {
        Map<String, Object> params = JsonUtils.getObjectFromInputStream(new TypeReference<Map<String, Object>>() {
        }, inputStream);
        String userName = (String) params.get("user");
        String password = (String) params.get("password");
        String passFromDb = secretService.getUserPasswordByName(userName);
        if (password.equals(passFromDb)) {
            String token = authService.generateToken(params);
            Map<String, String> response = new HashMap<>();
            response.put("token", token);
            JsonUtils.writeObjectToOutput(outputStream, response);
        } else {
            JsonUtils.writeObjectToOutput(outputStream, new BotResponse().setStatusCode(401));
        }
    }

    @Inject
    public void setSecretService(ISecretService secretService) {
        this.secretService = secretService;
    }

    @Inject
    public void setAuthService(IAuthService authService) {
        this.authService = authService;
    }
}
