package tech.filatov.pigeoner.util;

import tech.filatov.pigeoner.dto.HierarchicalDto;
import tech.filatov.pigeoner.model.dovecote.Section;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SectionUtil {
    private SectionUtil() {}

    public static <T extends HierarchicalDto<T>> List<T> makeHierarchy(List<T> elements) {
        Map<Long, T> elementsMap = elements.stream()
                .collect(Collectors.toMap(T::getId, Function.identity()));

        int numberOfRootObject = 0;

        for (T element : elements) {
            if (element.getParentId() == null) {
                numberOfRootObject++;
                continue;
            }
            elementsMap.get(element.getParentId()).getChildren().add(element);
        }
        elements.clear();

        for (T element : elementsMap.values()) {
            if (element.getParentId() == null) {
                elements.add(element);
                if (--numberOfRootObject == 0) break;
            }
        }

        return elements;
    }

    public static List<Section> makeSectionsFrom(List<Long> id) {
        return id.stream()
                .map(Section::new)
                .collect(Collectors.toList());
    }
}
