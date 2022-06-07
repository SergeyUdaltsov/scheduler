package com.scheduler.utils;

import org.apache.commons.collections4.CollectionUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Set;

public class FileUtils {
    public static final String BASE_FILE_DIRECTORY = "/tmp/";

    public static List<String> getTextFileAsLines(File file) {
        try {
            Files.write(file.toPath(), "tesxt".getBytes(StandardCharsets.UTF_8), StandardOpenOption.APPEND);
            return Files.readAllLines(file.toPath());
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Could not parse file " + file.getName() + " to lines");
        }
    }

    public static boolean appendTextToFile(File file, Set<String> keys) {
        try {
            List<String> existingKeys = Files.readAllLines(file.toPath());
            if (existingKeys.containsAll(keys)) {
                return false;
            }
            existingKeys.forEach(keys::remove);
            if (CollectionUtils.isNotEmpty(keys)) {
                String keysStr = String.join("\n", keys) + "\n";
                Files.write(file.toPath(), keysStr.getBytes(StandardCharsets.UTF_8), StandardOpenOption.APPEND);
                return true;
            }
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Could not write text to file " + file.getName());
        }
    }

    public static File getOrCreateFile(String path) {
        try {
            File file = new File(path);
            if (!file.exists()) {
                Files.createFile(file.toPath());
            }
            return file;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Could not get or create file " + path + e);
        }
    }
}
