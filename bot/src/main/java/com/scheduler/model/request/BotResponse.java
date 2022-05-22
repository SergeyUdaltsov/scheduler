package com.scheduler.model.request;

/**
 * @author Serhii_Udaltsov on 10/4/2021
 */

public class BotResponse {

    private String status;
    private int statusCode;
    private Object payload;

    public String getStatus() {
        return status;
    }

    public BotResponse setStatus(String status) {
        this.status = status;
        return this;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public BotResponse setStatusCode(int statusCode) {
        this.statusCode = statusCode;
        return this;
    }

    public Object getPayload() {
        return payload;
    }

    public BotResponse setPayload(Object payload) {
        this.payload = payload;
        return this;
    }
}
