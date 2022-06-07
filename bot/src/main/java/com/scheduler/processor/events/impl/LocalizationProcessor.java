package com.scheduler.processor.events.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.scheduler.model.EventType;
import com.scheduler.model.events.LocalizationEvent;
import com.scheduler.processor.events.IEventProcessor;
import com.scheduler.service.IS3Service;
import com.scheduler.utils.FileUtils;
import com.scheduler.utils.JsonUtils;

import java.io.File;
import java.util.Set;

public class LocalizationProcessor implements IEventProcessor {
    private static final String UNLOCALIZED_KEYS_FILE_NAME = "keysToLocalize.txt";
    private static final String LOCALIZATION_BUCKET_NAME = "unlocalized-messages";

    private final IS3Service s3Service;

    public LocalizationProcessor(IS3Service s3Service) {
        this.s3Service = s3Service;
    }

    @Override
    public void process(String eventStr) {
        LocalizationEvent event = JsonUtils.getObjectFromJsonString(new TypeReference<>() {
        }, eventStr);
        Set<String> keys = event.getKeys();
        File file = s3Service.getFileFromS3(LOCALIZATION_BUCKET_NAME, UNLOCALIZED_KEYS_FILE_NAME);
        if (file == null) {
            file = FileUtils.getOrCreateFile(FileUtils.BASE_FILE_DIRECTORY + UNLOCALIZED_KEYS_FILE_NAME);
        }

        boolean inserted = FileUtils.appendTextToFile(file, keys);
        if (inserted) {
            s3Service.saveFileToS3(file, LOCALIZATION_BUCKET_NAME, UNLOCALIZED_KEYS_FILE_NAME);
        }
    }

    @Override
    public EventType getSupportedEventType() {
        return EventType.LOCALIZATION;
    }
}
