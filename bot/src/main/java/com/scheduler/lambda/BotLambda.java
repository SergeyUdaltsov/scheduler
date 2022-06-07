package com.scheduler.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.scheduler.application.TelegramBot;
import com.scheduler.dagger.DaggerLambdaComponent;
import com.scheduler.utils.JsonUtils;
import org.telegram.telegrambots.meta.api.objects.Update;

import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

/**
 * @author Serhii_Udaltsov on 4/7/2021
 */
public class BotLambda implements RequestStreamHandler {

    private TelegramBot telegramBot;

    public BotLambda() {
        DaggerLambdaComponent.create().inject(this);
    }

    @Override
    public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) throws IOException {
        Map request = JsonUtils.getObjectFromInputStream(Map.class, inputStream);
        Map<String, Object> body = (Map)request.get("body");
        Map<String, String> params = (Map) request.get("queryParams");
        String customer = params.get("customer");
        Update update = JsonUtils.parseMap(body, Update.class);
        System.out.println("Update ------------ " + JsonUtils.convertObjectToString(update));
        System.out.println("Customer ---------- " + customer);
        telegramBot.onUpdateReceived(update);
    }

    @Inject
    public void setTelegramBot(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }
}
