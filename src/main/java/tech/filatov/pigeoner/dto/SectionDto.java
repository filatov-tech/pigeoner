package tech.filatov.pigeoner.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SectionDto extends BaseDto {
    private String name;
    private String fullName;
    private String sectionType;
    private int pigeonsNumber;
    private Long parentId;
    private String rootParentName;
    private List<SectionDto> children = new ArrayList<>();
    private List<PigeonLabelDto> pigeons = new ArrayList<>();

    public SectionDto(Long id, String name, Long parentId) {
        this.id = id;
        this.name = name;
        this.parentId = parentId;
    }

    public SectionDto(Long id, String name, String sectionType, int pigeonsNumber) {
        this.id = id;
        this.name = name;
        this.sectionType = sectionType;
        this.pigeonsNumber = pigeonsNumber;
    }

    public SectionDto(Long id, String name, String sectionType, Long parentId, int pigeonsNumber) {
        this.id = id;
        this.name = name;
        this.sectionType = sectionType;
        this.parentId = parentId;
        this.pigeonsNumber = pigeonsNumber;
    }
}
