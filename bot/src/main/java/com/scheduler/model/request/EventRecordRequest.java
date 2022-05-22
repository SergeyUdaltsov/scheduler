package com.scheduler.model.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EventRecordRequest {

    @JsonProperty("Records")
    private List<SqsEvent> records;

    public EventRecordRequest() {
    }

    public List<SqsEvent> getRecords() {
        return records;
    }

    public void setRecords(List<SqsEvent> records) {
        this.records = records;
    }
}
