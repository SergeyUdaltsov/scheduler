package com.scheduler.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Serhii_Udaltsov on 6/7/2021
 */
public class MessageHolder {
    private List<Button> buttons;
    private String message;
    private KeyBoardType keyBoardType;
    private ButtonsType buttonsType;
    private boolean withCommonButtons;
    private Map<String, String> placeholders = new HashMap<>();

    public MessageHolder(List<Button> buttons, String message, KeyBoardType keyBoardType,
                         ButtonsType buttonsType, boolean withCommonButtons, Map<String, String> placeholders) {
        this.buttons = buttons;
        this.message = message;
        this.keyBoardType = keyBoardType;
        this.buttonsType = buttonsType;
        this.withCommonButtons = withCommonButtons;
        this.placeholders = placeholders;
    }

    public void addPlaceholder(String key, String value) {
        this.placeholders.put(key, value);
    }

    public static Builder builder() {
        return new Builder();
    }

    public ButtonsType getButtonsType() {
        return buttonsType;
    }

    public void setButtonsType(ButtonsType buttonsType) {
        this.buttonsType = buttonsType;
    }

    public List<Button> getButtons() {
        return buttons;
    }

    public void setButtons(List<Button> buttons) {
        this.buttons = buttons;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public KeyBoardType getKeyBoardType() {
        return keyBoardType;
    }

    public void setKeyBoardType(KeyBoardType keyBoardType) {
        this.keyBoardType = keyBoardType;
    }

    public boolean isWithCommonButtons() {
        return withCommonButtons;
    }

    public void setWithCommonButtons(boolean withCommonButtons) {
        this.withCommonButtons = withCommonButtons;
    }

    public Map<String, String> getPlaceholders() {
        return placeholders;
    }

    public void setPlaceholders(Map<String, String> placeholders) {
        this.placeholders = placeholders;
    }

    public static class Builder {
        private List<Button> buttons;
        private String message;
        private KeyBoardType keyBoardType;
        private ButtonsType buttonsType;
        private boolean withCommonButtons;
        private Map<String, String> placeholders = new HashMap<>();

        public Builder withButtons(List<Button> buttons) {
            this.buttons = buttons;
            return this;
        }

        public Builder withMessage(String message) {
            this.message = message;
            return this;
        }

        public Builder withKeyboardType(KeyBoardType keyboardType) {
            this.keyBoardType = keyboardType;
            return this;
        }

        public Builder withButtonsType(ButtonsType buttonsType) {
            this.buttonsType = buttonsType;
            return this;
        }

        public Builder withCommonButtons(boolean withCommonButtons) {
            this.withCommonButtons = withCommonButtons;
            return this;
        }

        public Builder withPlaceholders(Map<String, String> placeholders) {
            this.placeholders = placeholders;
            return this;
        }

        public MessageHolder build() {
            return new MessageHolder(buttons, message, keyBoardType, buttonsType, withCommonButtons, placeholders);
        }
    }
}
