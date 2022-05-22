package com.scheduler.utils;

import com.scheduler.model.Button;
import com.scheduler.model.ButtonsType;
import com.scheduler.model.KeyBoardType;
import com.scheduler.model.Language;
import com.scheduler.model.MessageHolder;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Serhii_Udaltsov on 4/12/2021
 */
public class MessageUtils {

    public static List<String> getFlags() {
        return Arrays.stream(Language.values())
                .map(Language::getValue)
                .collect(Collectors.toList());
    }

    public static String getTextFromUpdate(Update update) {
        CallbackQuery callbackQuery = update.getCallbackQuery();
        if (callbackQuery != null) {
            return callbackQuery.getData();
        }
        return update.getMessage().getText();
    }

    public static long getUserIdFromUpdate(Update update) {
        CallbackQuery callbackQuery = update.getCallbackQuery();
        if (callbackQuery != null) {
            return callbackQuery.getFrom().getId();
        }
        return update.getMessage().getFrom().getId();
    }

    public static Map<String, String> commonButtonsMap() {
        Map<String, String> buttons = new HashMap<>();
        buttons.put("Назад", "Назад");
        buttons.put("Главная", "Главная");
        return buttons;
    }

    public static List<String> dashboardButtons() {
        return Arrays.asList("Платежи", "Начисления", "Баланс", "Игроки", "Переводы", "Сбор");
    }

    public static MessageHolder buildDashboardHolder() {
        return holder(dashboardButtons(), "Выбери раздел", KeyBoardType.TWO_ROW, true,
                false, ButtonsType.KEYBOARD);
    }

    public static Map<String, String> buildButtons(List<Button> buttons, boolean withCommon) {
        Map<String, String> resultMap = new LinkedHashMap<>();
        for (Button button : buttons) {
            resultMap.put(button.getValue(), StringUtils.isBlank(button.getCallback())
                    ? button.getValue()
                    : button.getCallback());
        }
        if (withCommon) {
            resultMap.putAll(commonButtonsMap());
        }
        return resultMap;
    }

    public static SendMessage buildMessage(MessageHolder holder, long operatorId) {
        SendMessage sendMessage = new SendMessage(String.valueOf(operatorId), holder.getMessage());
        Map<String, String> buttonsMap = buildButtons(holder.getButtons(), holder.isWithCommonButtons());
        ReplyKeyboard keyboard = holder.getButtonsType()
                .getButtonsFunction()
                .apply(buttonsMap, holder.getKeyBoardType());
        sendMessage.setReplyMarkup(keyboard);
        return sendMessage;
    }

    public static MessageHolder commonUnCheckableVerticalHolder(List<String> titles, String message) {
        return commonHolder(titles, message, KeyBoardType.VERTICAL, false);
    }

    public static MessageHolder commonCheckableHolder(List<String> titles, String message, KeyBoardType type) {
        return commonHolder(titles, message, type, true);
    }

    public static MessageHolder commonHolder(List<String> titles, String message, KeyBoardType type,
                                             boolean isCheckable) {
        return holder(titles, message, type, isCheckable, true, ButtonsType.KEYBOARD);
    }

    public static MessageHolder getLanguageMessageHolder() {
        return holder(MessageUtils.getFlags(), "Select language",
                KeyBoardType.VERTICAL, false, false, ButtonsType.INLINE);
    }

    public static MessageHolder holder(List<String> titles, String message, KeyBoardType type,
                                       boolean isCheckable, boolean withCommonButtons, ButtonsType buttonsType) {
        return MessageHolder.builder()
                .withCommonButtons(withCommonButtons)
                .withMessage(message)
                .withButtons(commonButtons(titles, isCheckable))
                .withButtonsType(buttonsType)
                .withKeyboardType(type)
                .build();
    }

    public static List<Button> commonButtons(List<String> titles, boolean isCheckable) {
        return titles.stream()
                .map(t -> Button.builder()
                        .withCheckable(isCheckable)
                        .withValue(t)
                        .build())
                .collect(Collectors.toList());
    }

}
