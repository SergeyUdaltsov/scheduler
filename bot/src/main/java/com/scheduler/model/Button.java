package com.scheduler.model;

/**
 * @author Serhii_Udaltsov on 6/7/2021
 */
public class Button {
    private String value;
    private boolean checkable;
    private String callback;

    public Button(String value, boolean checkable, String callback) {
        this.value = value;
        this.checkable = checkable;
        this.callback = callback;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isCheckable() {
        return checkable;
    }

    public void setCheckable(boolean checkable) {
        this.checkable = checkable;
    }

    public String getCallback() {
        return callback;
    }

    public void setCallback(String callback) {
        this.callback = callback;
    }

    public static class Builder {
        private String value;
        private boolean checkable;
        private String callback;

        public Builder withValue(String value) {
            this.value = value;
            return this;
        }

        public Builder withCheckable(boolean checkable) {
            this.checkable = checkable;
            return this;
        }

        public Builder withCallBack(String callBack) {
            this.callback = callBack;
            return this;
        }

        public Button build() {
            return new Button(value, checkable, callback);
        }
    }
}
