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
import com.scheduler.service.ISqsService;
import com.scheduler.utils.JsonUtils;
import com.scheduler.utils.StringUtils;
import org.apache.commons.lang3.text.StrSubstitutor;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Localizer implements ILocalizer {

    private final IContextService contextService;
    private final ISqsService sqsService;
    private final Map<Language, Map<String, String>> dictionaries = new HashMap<>();
    private final Set<String> unlocalizedKeys = new HashSet<>();

    public Localizer(IContextService contextService, ISqsService sqsService) {
        this.contextService = contextService;
        this.sqsService = sqsService;
    }

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

        for (MessageHolder holder : holders) {
            holder.setMessage(localize(holder.getMessage(), dictionary, holder.getPlaceholders()));
            List<Button> buttons = holder.getButtons();
            for (Button button : buttons) {
                button.setValue(localize(button.getValue(), dictionary, holder.getPlaceholders()));
            }
        }
    }

    private Map<String, String> getDictionary(Language language) {
        Map<String, String> fromCache = dictionaries.get(language);
        if (fromCache != null) {
            return fromCache;
        }
        InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream(language.getLocalizationFilePath());
        return JsonUtils.getObjectFromInputStream(new TypeReference<Map<String, String>>() {
        }, resourceAsStream);

    }

    private String localize(String text, Map<String, String> dictionary, Map<String, String> placeholders) {
        localizePlaceholders(dictionary, placeholders);
        String localizedText = localizeString(text, dictionary);
        return substitutePlaceholders(localizedText, placeholders);
    }

    private String substitutePlaceholders(String text, Map<String, String> placeholders) {
        if (placeholders == null) {
            return text;
        }
        StrSubstitutor sub = new StrSubstitutor(placeholders, "${", "}");
        return sub.replace(text);
    }

    private void localizePlaceholders(Map<String, String> dictionary, Map<String, String> placeholders) {
        if (placeholders == null) {
            return;
        }
        for (Map.Entry<String, String> entry : placeholders.entrySet()) {
            entry.setValue(localizeString(entry.getValue(), dictionary));
        }
    }

    private String localizeString(String key, Map<String, String> dictionary) {
        String localizedText = dictionary.get(key);
        System.out.println("Key to localize ---------- " + key);
        if (StringUtils.isBlank(localizedText) && !unlocalizedKeys.contains(key)) {
            System.out.println("key was not localized and is not in set of unlocalized keys --------- " + key);
            EventHolder eventHolder = new EventHolder();
            eventHolder.setEventType(EventType.LOCALIZATION);
            LocalizationEvent localizationEvent = new LocalizationEvent();
            localizationEvent.setText(key);
            eventHolder.setContent(JsonUtils.convertObjectToString(localizationEvent));
            sqsService.sendEvent(eventHolder);
            unlocalizedKeys.add(key);
        }
        return StringUtils.isBlank(localizedText) ? key : localizedText;
    }
}
