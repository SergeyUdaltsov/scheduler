package com.scheduler.model.request;

import com.amazonaws.internal.SdkInternalMap;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SqsEvent {

    private String messageId;
    private String receiptHandle;
    private String mD5OfBody;
    private String body;
    private SdkInternalMap<String, String> attributes;

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getReceiptHandle() {
        return receiptHandle;
    }

    public void setReceiptHandle(String receiptHandle) {
        this.receiptHandle = receiptHandle;
    }

    public String getmD5OfBody() {
        return mD5OfBody;
    }

    public void setmD5OfBody(String mD5OfBody) {
        this.mD5OfBody = mD5OfBody;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public SdkInternalMap<String, String> getAttributes() {
        return attributes;
    }

    public void setAttributes(SdkInternalMap<String, String> attributes) {
        this.attributes = attributes;
    }
}
