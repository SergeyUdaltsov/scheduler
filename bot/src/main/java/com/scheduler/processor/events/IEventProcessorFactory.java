package com.scheduler.processor.events;

import com.scheduler.model.EventType;

public interface IEventProcessorFactory {

    IEventProcessor getEventProcessor(EventType eventType);
}
