package com.scheduler.ui.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.scheduler.model.User;

/**
 * @author Serhii_Udaltsov on 10/23/2021
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class UiRequest {
    private String handlerType;
    private User user;

    public UiRequest() {
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getHandlerType() {
        return handlerType;
    }

    public void setHandlerType(String handlerType) {
        this.handlerType = handlerType;
    }
}
