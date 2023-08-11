package com.cappielloantonio.tempo.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

public class Util {
    public static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
        try {
            Map<Object, Boolean> uniqueMap = new ConcurrentHashMap<>();
            return t -> uniqueMap.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
        } catch (NullPointerException exception) {
            return null;
        }
    }
}
