package com.scheduler.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.scheduler.dagger.DaggerLambdaComponent;
import com.scheduler.model.Payment;
import com.scheduler.service.IPaymentService;
import com.scheduler.service.IS3Service;
import com.scheduler.utils.CollectionUtils;
import com.scheduler.utils.PaymentUtils;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

/**
 * @author Serhii_Udaltsov on 5/18/2021
 */
public class ArchiveLambda implements RequestStreamHandler {

    private IPaymentService paymentService;
    private IS3Service s3Service;

    public ArchiveLambda() {
        DaggerLambdaComponent.create().inject(this);
    }

    @Override
    public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) throws IOException {
        List<Payment> payments = paymentService.archivePayments();
        if (CollectionUtils.isEmpty(payments)) {
            return;
        }
        File file = PaymentUtils.createAllPaymentsFile(payments, Collections.emptyList());

        LocalDate now = LocalDate.now();
        int month = now.getMonth().getValue();
        int year = now.getYear();
        String fileName = year + "-" + month;
        s3Service.saveFileToS3(file, "coach-helper-payments", fileName);
    }

    @Inject
    public void setPaymentService(IPaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @Inject
    public void setS3Service(IS3Service s3Service) {
        this.s3Service = s3Service;
    }
}
