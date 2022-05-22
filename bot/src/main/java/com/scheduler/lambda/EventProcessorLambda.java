package com.scheduler.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.fasterxml.jackson.core.type.TypeReference;
import com.scheduler.dagger.DaggerLambdaComponent;
import com.scheduler.model.EventType;
import com.scheduler.model.events.EventHolder;
import com.scheduler.model.request.EventRecordRequest;
import com.scheduler.model.request.SqsEvent;
import com.scheduler.processor.events.IEventProcessor;
import com.scheduler.processor.events.IEventProcessorFactory;
import com.scheduler.utils.JsonUtils;

import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class EventProcessorLambda implements RequestStreamHandler {

    private IEventProcessorFactory processorFactory;

    public EventProcessorLambda() {
        DaggerLambdaComponent.create().inject(this);
    }

    @Override
    public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) throws IOException {
        EventRecordRequest request = JsonUtils.getObjectFromInputStream(new TypeReference<EventRecordRequest>() {
        }, inputStream);
        System.out.println("Received message ---------- " + JsonUtils.convertObjectToString(request));
        for (SqsEvent event : request.getRecords()) {
            String body = event.getBody();
            EventHolder holder = JsonUtils.getObjectFromJsonString(new TypeReference<EventHolder>() {
            }, body);
            EventType eventType = holder.getEventType();
            IEventProcessor processor = processorFactory.getEventProcessor(eventType);
            processor.process(holder.getContent());
        }
    }

    @Inject
    public void setProcessorFactory(IEventProcessorFactory processorFactory) {
        this.processorFactory = processorFactory;
    }
}
