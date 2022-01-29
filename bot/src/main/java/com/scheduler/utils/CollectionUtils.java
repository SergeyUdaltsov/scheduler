package com.scheduler.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Serhii_Udaltsov on 4/18/2021
 */
public class CollectionUtils {

    public static Map<String, Object> contextParamMap(String key, String value) {
        Map<String, Object> param = new HashMap<>();
        param.put(key, value);
        return param;
    }

    public static <T> List<T> listWithElement(T element) {
        List<T> elementsList = new ArrayList<>();
        elementsList.add(element);
        return elementsList;
    }

    public static <T> List<T> listWithElements(T... elements) {
        List<T> elementsList = new ArrayList<>();
        Collections.addAll(elementsList, elements);
        return elementsList;
    }

    public static <T> List<T> newList(Collection<T> collection) {
        return new ArrayList<>(collection);
    }

    public static boolean isEmpty(Map coll) {
        return coll == null || coll.isEmpty();
    }

    public static boolean isNotEmpty(Map map) {
        return !isEmpty(map);
    }

    public static <T> List<T> removeLastElements(List<T> collection, int elementsCount) {
        return collection.stream().limit(collection.size() - elementsCount).collect(Collectors.toList());
    }

    public static boolean isNotEmpty(Collection collection) {
        return !isEmpty(collection);
    }

    public static boolean isEmpty(Collection collection) {
        return collection == null || collection.size() == 0;
    }

    public static <K, V> Builder<K, V> mapBuilder() {
        return new Builder();
    }

    public static <T> T getLastElement(List<T> collection) {
        if (isEmpty(collection)) {
            return null;
        }
        return collection.get(collection.size() - 1);
    }

    public static <T> List<List<T>> chunked(List<T> list, int length) {
        List<List<T>> parts = new ArrayList<>();
        final int size = list.size();
        for (int i = 0; i < size; i += length) {
            parts.add(new ArrayList<>(list.subList(i, Math.min(size, i + length))));
        }
        return parts;
    }

    public static class Builder<K, V> {
        private Map<K, V> map;

        public Builder() {
            this.map = new HashMap<>();
        }

        public Builder<K, V> withPair(K key, V value) {
            this.map.put(key, value);
            return this;
        }

        public Map<K, V> build() {
            return map;
        }
    }
}
