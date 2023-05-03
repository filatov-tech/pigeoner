package tech.filatov.pigeoner.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class SectionHierarchicalDto {
    private Long id;
    private String name;
    private Long parentId;
    private List<SectionHierarchicalDto> childSections = new ArrayList<>();

    public SectionHierarchicalDto(Long id, String name, Long parentId) {
        this.id = id;
        this.name = name;
        this.parentId = parentId;
    }
}
