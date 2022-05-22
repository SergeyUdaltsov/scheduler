package com.scheduler.model.events;

import com.scheduler.model.EventType;

public class EventHolder {

    private EventType eventType;
    private String content;

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
