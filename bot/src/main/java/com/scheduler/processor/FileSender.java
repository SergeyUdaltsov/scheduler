package com.scheduler.processor;

import com.scheduler.service.ISecretService;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * @author Serhii_Udaltsov on 5/15/2021
 */
public class FileSender extends DefaultAbsSender {

    private ISecretService secretService;

    public FileSender(ISecretService secretService) {
        this(new DefaultBotOptions());
        this.secretService = secretService;
    }

    protected FileSender(DefaultBotOptions options) {
        super(options);
    }

    public void sendDocument(SendDocument document) throws TelegramApiException {
        execute(document);
    }

    @Override
    public String getBotToken() {
        return secretService.getBotToken();
    }
}
