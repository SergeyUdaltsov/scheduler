package com.scheduler.model;

import com.scheduler.utils.KeyBoardUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

import java.util.Map;
import java.util.function.BiFunction;

/**
 * @author Serhii_Udaltsov on 12/24/2021
 */
@RequiredArgsConstructor
@Getter
public enum ButtonsType {
    KEYBOARD(KeyBoardUtils::buildReplyKeyboard),
    CONTACTS(KeyBoardUtils::buildContactsKeyboard),
    INLINE(KeyBoardUtils::buildInlineKeyboard);

    private final BiFunction<Map<String, String>, KeyBoardType, ReplyKeyboard> buttonsFunction;
}
