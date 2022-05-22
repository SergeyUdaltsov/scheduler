package com.scheduler.service.impl;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.scheduler.model.events.EventHolder;
import com.scheduler.service.ISqsService;
import com.scheduler.utils.JsonUtils;

public class SqsService implements ISqsService {

    private final AmazonSQS sqs;

    public SqsService(AmazonSQS sqs) {
        this.sqs = sqs;
    }

    @Override
    public void sendEvent(EventHolder holder) {
        SendMessageRequest request = new SendMessageRequest()
                .withMessageBody(JsonUtils.convertObjectToString(holder))
                .withQueueUrl("https://sqs.eu-central-1.amazonaws.com/773974733061/event_queue");
        sqs.sendMessage(request);
    }
}
