package com.scheduler.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.fasterxml.jackson.core.type.TypeReference;
import com.scheduler.model.BotResponse;
import com.scheduler.model.User;
import com.scheduler.service.IAuthService;
import com.scheduler.service.IUserService;
import com.scheduler.utils.JsonUtils;
import com.scheduler.utils.StringUtils;

import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

/**
 * @author Serhii_Udaltsov on 10/23/2021
 */
public abstract class AbstractLambda implements RequestStreamHandler {

    private IAuthService authService;
    private IUserService userService;

    @Override
    public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) throws IOException {
        Map<String, Object> params = JsonUtils.getObjectFromInputStream(new TypeReference<Map<String, Object>>() {
        }, inputStream);

        String userNameFromToken = authService.validateUser(params);
        if (StringUtils.isBlank(userNameFromToken)) {
            JsonUtils.writeObjectToOutput(outputStream, new BotResponse().setStatusCode(401));
            return;
        }
        User userFromDb = userService.getUserByName(userNameFromToken);
        if (userFromDb == null) {
            JsonUtils.writeObjectToOutput(outputStream, new BotResponse().setStatusCode(401));
            return;
        }
        Map<String, Object> body = (Map) params.get("body");
        body.put("user", userFromDb);
        Object response = handle(body);
        JsonUtils.writeObjectToOutput(outputStream, response);
    }

    protected abstract Object handle(Map<String, Object> body);

    @Inject
    public void setAuthService(IAuthService authService) {
        this.authService = authService;
    }

    @Inject
    public void setUserService(IUserService userService) {
        this.userService = userService;
    }
}
