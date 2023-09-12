package tech.filatov.pigeoner.util;

import tech.filatov.pigeoner.HasId;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CommonUtil {
    private CommonUtil() {}

    public static <T extends HasId> Map<Long, T> getLookupMapFrom(List<T> elements) {
        return elements.stream().collect(Collectors.toMap(T::getId, element -> element));
    }

    public static String toCamelCase(String input) {
        String[] array = input.split("_");
        StringBuilder result = new StringBuilder(array[0].toLowerCase());
        for (int i = 1; i < array.length; i++) {
            result.append(capitalizeFirstLetter(array[i]));
        }
        return result.toString();
    }

    private static String capitalizeFirstLetter(String word) {
        StringBuilder result = new StringBuilder(word.length());
        result.append(Character.toUpperCase(word.charAt(0)));
        result.append(word.substring(1));
        return result.toString();
    }
}
