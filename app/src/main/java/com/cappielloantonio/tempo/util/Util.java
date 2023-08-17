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

    public static String toPascalCase(String name) {
        if (name == null || name.isEmpty()) {
            return name;
        }

        StringBuilder pascalCase = new StringBuilder();

        char newChar;
        boolean toUpper = false;
        char[] charArray = name.toCharArray();

        for (int ctr = 0; ctr <= charArray.length - 1; ctr++) {
            if (ctr == 0) {
                newChar = Character.toUpperCase(charArray[ctr]);
                pascalCase = new StringBuilder(Character.toString(newChar));
                continue;
            }

            if (charArray[ctr] == '_') {
                toUpper = true;
                continue;
            }

            if (toUpper) {
                newChar = Character.toUpperCase(charArray[ctr]);
                pascalCase.append(newChar);
                toUpper = false;
                continue;
            }

            pascalCase.append(charArray[ctr]);
        }

        return pascalCase.toString();
    }
}
