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
}
