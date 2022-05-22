package com.scheduler.processor.events.impl;

import com.scheduler.model.EventType;
import com.scheduler.processor.events.IEventProcessor;
import com.scheduler.processor.events.IEventProcessorFactory;

import java.util.Map;

public class EventProcessorFactory implements IEventProcessorFactory {

    private final Map<EventType, IEventProcessor> processorMap;

    public EventProcessorFactory(Map<EventType, IEventProcessor> processorMap) {
        this.processorMap = processorMap;
    }

    @Override
    public IEventProcessor getEventProcessor(EventType eventType) {
        IEventProcessor processor = processorMap.get(eventType);
        if (processor == null) {
            throw new IllegalArgumentException("Processor not found for event type " + eventType);
        }
        return processor;
    }
}
