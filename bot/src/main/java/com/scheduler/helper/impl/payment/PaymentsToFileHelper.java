package com.scheduler.helper.impl.payment;

import com.scheduler.helper.IPaymentActionHelper;
import com.scheduler.model.CommandType;
import com.scheduler.model.MessageHolder;
import com.scheduler.model.Payment;
import com.scheduler.model.Transfer;
import com.scheduler.processor.FileSender;
import com.scheduler.service.IPaymentService;
import com.scheduler.service.ITransferService;
import com.scheduler.utils.MessageUtils;
import com.scheduler.utils.PaymentUtils;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.util.List;

/**
 * @author Serhii_Udaltsov on 5/18/2021
 */
public class PaymentsToFileHelper implements IPaymentActionHelper {

    private IPaymentService paymentService;
    private ITransferService transferService;
    private FileSender sender;

    public PaymentsToFileHelper(IPaymentService paymentService, FileSender sender,
                                ITransferService transferService) {
        this.paymentService = paymentService;
        this.sender = sender;
        this.transferService = transferService;
    }

    @Override
    public MessageHolder getMessage(Update update) {
        long userId = MessageUtils.getUserIdFromUpdate(update);
        List<Payment> payments = paymentService.getPaymentsByOperator(userId);
        payments.removeIf(Payment::isHidden);
        List<Transfer> transfers = transferService.getAllOperatorTransfers(userId);
        File file = PaymentUtils.createAllPaymentsFile(payments, transfers);
        SendDocument sendDocument = new SendDocument();
        sendDocument.setDocument(new InputFile(file));
        sendDocument.setChatId(String.valueOf(userId));
        try {
            sender.sendDocument(sendDocument);
        } catch (TelegramApiException e) {
            e.printStackTrace();
            return MessageUtils.buildDashboardHolder("Файл не был отправлен");
        }
        return MessageUtils.buildDashboardHolder();
    }

    @Override
    public String getHelperParamsValue() {
        return null;
    }

    @Override
    public CommandType getNextCommandType(Update update) {
        return CommandType.DASHBOARD_PROCESSOR;
    }
}
