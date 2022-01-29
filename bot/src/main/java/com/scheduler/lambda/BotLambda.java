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
        Update update = JsonUtils.getObjectFromInputStream(Update.class, inputStream);
        telegramBot.onUpdateReceived(update);
    }

    @Inject
    public void setTelegramBot(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }
}
