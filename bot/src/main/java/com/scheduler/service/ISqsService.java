package com.scheduler.service;

import com.scheduler.model.events.EventHolder;

public interface ISqsService {

    void sendEvent(EventHolder holder);
}
