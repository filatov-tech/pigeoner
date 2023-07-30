package tech.filatov.pigeoner.util;

import tech.filatov.pigeoner.dto.SectionDto;
import tech.filatov.pigeoner.model.dovecote.Section;

import java.util.*;
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

    public static Map<Long, SectionDto> getHierarchicalMapFrom(List<SectionDto> elements) {
        return elements.stream().collect(Collectors.toMap(SectionDto::getId, Function.identity()));
    }

    public static List<Section> makeSectionsFrom(List<Long> id) {
        return id.stream()
                .map(Section::new)
                .collect(Collectors.toList());
    }

    public static List<SectionDto> fillAddressData(List<SectionDto> sections) {
        Map<Long, SectionDto> sectionMap = CommonUtil.getLookupMapFrom(sections);
        for (SectionDto section : sections) {
            Deque<String> pathStack = getPathStack(section, sectionMap);
            section.setRootName(getRootName(pathStack).orElse(section.getName()));
            section.setFullAddress(buildAddress(pathStack));
        }
        return sections;
    }

    private static Deque<String> getPathStack(SectionDto section, Map<Long, SectionDto> sectionMap) {
        Deque<String> path = new ArrayDeque<>();

        path.addLast(section.getName());
        if (section.getParentId() != null) {
            SectionDto ancestor = sectionMap.get(section.getParentId());
            path.addLast(ancestor.getName());
            while (ancestor.getParentId() != null) {
                ancestor = sectionMap.get(ancestor.getParentId());
                path.addLast(ancestor.getName());
            }
        }

        return path;
    }

    private static String buildAddress(Deque<String> path) {
        StringBuilder fullAddress = new StringBuilder();
        String sep = " - ";
        while (!path.isEmpty()) {
            fullAddress.append(path.pollLast()).append(sep);
        }
        if (!fullAddress.isEmpty()) {
            fullAddress.delete(fullAddress.length() - sep.length(), fullAddress.length());
        }
        return fullAddress.toString();
    }

    private static Optional<String> getRootName(Deque<String> pathStack) {
        return Optional.ofNullable(pathStack.peekLast());
    }
}
