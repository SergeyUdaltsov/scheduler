package com.scheduler.processor.impl.balance;

import com.scheduler.model.CommandType;
import com.scheduler.model.MessageHolder;
import com.scheduler.processor.FileSender;
import com.scheduler.processor.IProcessor;
import com.scheduler.utils.MessageUtils;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;

/**
 * @author Serhii_Udaltsov on 5/15/2021
 */
public class BalanceSendProcessor implements IProcessor {

    private FileSender fileSender;

    public BalanceSendProcessor(FileSender fileSender) {
        this.fileSender = fileSender;
    }

    @Override
    public MessageHolder processRequest(Update update) throws TelegramApiException {
        File file = new File("/tmp/playerPayments.txt");
        SendDocument sendDocument = new SendDocument();
        sendDocument.setChatId(String.valueOf(MessageUtils.getUserIdFromUpdate(update)));
        sendDocument.setDocument(new InputFile(file));
        fileSender.sendDocument(sendDocument);
        return MessageUtils.buildDashboardHolder();
//        return MessageUtils.buildDashboardMessage(update);
    }

    @Override
    public CommandType getNextCommandType() {
        return CommandType.DASHBOARD_PROCESSOR;
    }
}
