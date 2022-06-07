package com.scheduler.localization;

import com.fasterxml.jackson.core.type.TypeReference;
import com.scheduler.model.Button;
import com.scheduler.model.Context;
import com.scheduler.model.EventType;
import com.scheduler.model.Language;
import com.scheduler.model.MessageHolder;
import com.scheduler.model.events.EventHolder;
import com.scheduler.model.events.LocalizationEvent;
import com.scheduler.service.IContextService;
import com.scheduler.service.ISettingService;
import com.scheduler.service.ISqsService;
import com.scheduler.utils.JsonUtils;
import com.scheduler.utils.StringUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.text.StrSubstitutor;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RequiredArgsConstructor
public class Localizer implements ILocalizer {

    private final IContextService contextService;
    private final ISqsService sqsService;
    private final ISettingService settingService;
    private final Map<Language, Map<String, String>> dictionaries = new HashMap<>();
    private final Set<String> unlocalizedKeys = new HashSet<>();

    @Override
    public void localize(List<MessageHolder> holders, Update update) {
        Context context = contextService.getContext(update);
        Language language;
        if (context == null || context.getLanguage() == null) {
            language = Language.US;
        } else {
            language = context.getLanguage();
        }

        Map<String, String> dictionary = dictionaries.get(language);
        if (dictionary == null) {
            dictionary = getDictionary(language);
            dictionaries.put(language, dictionary);
        }
        Set<String> keysToLog = new HashSet<>();
        for (MessageHolder holder : holders) {
            holder.setMessage(localizeAndFillKeysToLog(holder.getMessage(), dictionary, holder.getPlaceholders(), keysToLog));
            List<Button> buttons = holder.getButtons();
            for (Button button : buttons) {
                button.setValue(localizeAndFillKeysToLog(button.getValue(), dictionary, holder.getPlaceholders(), keysToLog));
            }
        }
        if (!CollectionUtils.isEmpty(keysToLog) && settingService.isLocalizationLoggingEnabled()) {
            EventHolder eventHolder = EventHolder.builder()
                    .eventType(EventType.LOCALIZATION)
                    .content(JsonUtils.convertObjectToString(
                            LocalizationEvent.builder()
                                    .keys(keysToLog).build()
                    )).build();
            sqsService.sendEvent(eventHolder);
        }
    }

    private Map<String, String> getDictionary(Language language) {
        Map<String, String> fromCache = dictionaries.get(language);
        if (fromCache != null) {
            return fromCache;
        }
        InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream(language.getLocalizationFilePath());
        return JsonUtils.getObjectFromInputStream(new TypeReference<>() {
        }, resourceAsStream);

    }

    private String localizeAndFillKeysToLog(String text, Map<String, String> dictionary, Map<String, String> placeholders,
                                            Set<String> keysToLog) {
        localizePlaceholdersAndFillKeysToLog(dictionary, placeholders, keysToLog);
        String localizedText = localizeStringAndFillKeysToLog(text, dictionary, keysToLog);
        return substitutePlaceholders(localizedText, placeholders);
    }

    private String substitutePlaceholders(String text, Map<String, String> placeholders) {
        if (placeholders == null) {
            return text;
        }
        StrSubstitutor sub = new StrSubstitutor(placeholders, "${", "}");
        return sub.replace(text);
    }

    private void localizePlaceholdersAndFillKeysToLog(Map<String, String> dictionary, Map<String, String> placeholders,
                                                      Set<String> keysToLog) {
        if (placeholders == null) {
            return;
        }
        for (Map.Entry<String, String> entry : placeholders.entrySet()) {
            entry.setValue(localizeStringAndFillKeysToLog(entry.getValue(), dictionary, keysToLog));
        }
    }

    private String localizeStringAndFillKeysToLog(String key, Map<String, String> dictionary, Set<String> keysToLog) {
        String localizedText = dictionary.get(key);
        System.out.println("Key to localize ---------- " + key);
        if (StringUtils.isBlank(localizedText) && !unlocalizedKeys.contains(key)) {
            System.out.println("key was not localized and is not in set of unlocalized keys --------- " + key);
            unlocalizedKeys.add(key);
            keysToLog.add(key);
        }
        return StringUtils.isBlank(localizedText) ? key : localizedText;
    }
}
