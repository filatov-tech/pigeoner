package tech.filatov.pigeoner.util;

import tech.filatov.pigeoner.dto.SectionHierarchicalDto;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SectionUtil {
    private SectionUtil() {}

    public static List<SectionHierarchicalDto> makeHierarchy(List<SectionHierarchicalDto> sections) {
        Map<Long, SectionHierarchicalDto> sectionsMap = sections.stream()
                .collect(Collectors.toMap(SectionHierarchicalDto::getId, Function.identity()));

        int numberOfRootObject = 0;

        for (SectionHierarchicalDto section : sections) {
            if (section.getParentId() == null) {
                numberOfRootObject++;
                continue;
            }
            sectionsMap.get(section.getParentId()).getChildSections().add(section);
        }
        sections.clear();

        for (SectionHierarchicalDto section : sectionsMap.values()) {
            if (section.getParentId() == null) {
                sections.add(section);
                if (--numberOfRootObject == 0) break;
            }
        }

        return sections;
    }
}
