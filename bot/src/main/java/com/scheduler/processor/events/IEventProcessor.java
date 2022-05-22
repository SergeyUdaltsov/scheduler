package com.scheduler.processor.events;

import com.scheduler.model.EventType;

public interface IEventProcessor {

    void process(String event);

    EventType getSupportedEventType();
}
