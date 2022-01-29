package com.scheduler.utils;

import com.scheduler.model.Button;
import com.scheduler.model.KeyBoardType;
import com.scheduler.model.MessageHolder;
import com.scheduler.model.PaymentType;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

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
    private static final Map<Integer, String> monthMap = CollectionUtils
            .<Integer, String>mapBuilder()
            .withPair(1, "Январь")
            .withPair(2, "Февраль")
            .withPair(3, "Март")
            .withPair(4, "Апрель")
            .withPair(5, "Май")
            .withPair(6, "Июнь")
            .withPair(7, "Июль")
            .withPair(8, "Август")
            .withPair(9, "Сентябрь")
            .withPair(10, "Октябрь")
            .withPair(11, "Ноябрь")
            .withPair(12, "Декабрь").build();

    public static String getTextFromUpdate(Update update) {
        return update.getMessage().getText();
    }

    public static long getUserIdFromUpdate(Update update) {
        return update.getMessage().getFrom().getId();
    }

    public static String buildPlayerBalanceMessage(Map<PaymentType, Integer> playerBalance) {
        StringBuilder message = new StringBuilder();
        for (Map.Entry<PaymentType, Integer> entry : playerBalance.entrySet()) {
            message.append(entry.getKey().getValue()).append(": ").append(entry.getValue()).append("\n");
        }
        return message.toString();
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

    public static SendMessage buildDashboardMessage(Update update) {
        return buildMessage(dashboardButtons(), "Выбери раздел", update, KeyBoardType.TWO_ROW, false);
    }

    public static MessageHolder buildDashboardHolder() {
        return holder(dashboardButtons(), "Выбери раздел", KeyBoardType.TWO_ROW, true, false);
    }

    public static MessageHolder buildDashboardHolder(String message) {
        return holder(dashboardButtons(), String.format("%s%nВыбери раздел", message),
                KeyBoardType.TWO_ROW, true, false);
    }

    public static Map<String, String> buildButtons(List<String> buttons, boolean withCommon) {
        Map<String, String> resultMap = new LinkedHashMap<>();
        for (String button : buttons) {
            resultMap.put(button, button);
        }
        if (withCommon) {
            resultMap.putAll(commonButtonsMap());
        }
        return resultMap;
    }

    public static SendMessage buildMessage(List<String> buttons, String message, Update update, KeyBoardType type,
                                           boolean withCommonButtons) {
        long chatId = MessageUtils.getUserIdFromUpdate(update);
        return buildMessage(buttons, message, chatId, type, withCommonButtons);
    }

    public static SendMessage buildMessage(List<String> buttons, String message, long operatorId, KeyBoardType type,
                                           boolean withCommonButtons) {
        SendMessage sendMessage = new SendMessage(String.valueOf(operatorId), message);
        Map<String, String> buttonsMap = buildButtons(buttons, withCommonButtons);
        ReplyKeyboardMarkup keyboard = KeyBoardUtils.buildReplyKeyboard(buttonsMap, type);
        sendMessage.setReplyMarkup(keyboard);
        return sendMessage;
    }

    public static List<String> paymentsList() {
        return CollectionUtils.listWithElements("Тренерские", "Лед", "Воскресный лед");
    }

    public static List<String> balanceTitlesList() {
        return CollectionUtils.listWithElements("Должники", "По игроку", "На карте", "Переплаты");
    }

    public static MessageHolder commonCheckableVerticalHolder(List<String> titles, String message) {
        return commonCheckableHolder(titles, message, KeyBoardType.VERTICAL);
    }

    public static MessageHolder commonUnCheckableVerticalHolder(List<String> titles, String message) {
        return commonHolder(titles, message, KeyBoardType.VERTICAL, false);
    }

    public static MessageHolder commonCheckableHolder(List<String> titles, String message, KeyBoardType type) {
        return commonHolder(titles, message, type, true);
    }

    public static MessageHolder commonHolder(List<String> titles, String message, KeyBoardType type,
                                             boolean isCheckable) {
        return holder(titles, message, type, isCheckable, true);
    }

    public static MessageHolder holder(List<String> titles, String message, KeyBoardType type,
                                       boolean isCheckable, boolean withCommonButtons) {
        return MessageHolder.builder()
                .withCommonButtons(withCommonButtons)
                .withMessage(message)
                .withButtons(commonButtons(titles, isCheckable))
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

    public static String getMonthName(int number) {
        return monthMap.get(number);
    }

    public static List<String> paymentsButtons() {
        List<String> buttons = paymentBillsButtons();
        buttons.add("Провести Льготный");
        buttons.add("Показать Последние");
        buttons.add("Файл с платежами");
        return buttons;
    }

    public static List<String> billsButtons() {
        return paymentBillsButtons();
    }

    public static List<String> paymentBillsButtons() {
        return CollectionUtils.listWithElements("Провести", "Редактировать");
    }
}
