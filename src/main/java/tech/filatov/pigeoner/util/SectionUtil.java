package tech.filatov.pigeoner.util;

import tech.filatov.pigeoner.dto.SectionDto;
import tech.filatov.pigeoner.model.dovecote.Section;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SectionUtil {
    private SectionUtil() {}

    public static List<SectionDto> makeHierarchy(List<SectionDto> elements) {
        Map<Long, SectionDto> elementsMap = getHierarchicalMapFrom(elements);

        int numberOfRootObject = 0;

        for (SectionDto element : elements) {
            if (element.getParentId() == null) {
                numberOfRootObject++;
                continue;
            }
            elementsMap.get(element.getParentId()).getChildren().add(element);
        }
        elements.clear();

        for (SectionDto element : elementsMap.values()) {
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
