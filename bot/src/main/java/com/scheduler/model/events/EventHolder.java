package com.scheduler.model.events;

import com.scheduler.model.EventType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventHolder {

    private EventType eventType;
    private String content;

}
