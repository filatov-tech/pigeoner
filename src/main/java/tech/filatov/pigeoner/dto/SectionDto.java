package tech.filatov.pigeoner.dto;


import lombok.Getter;
import java.util.ArrayList;
import java.util.List;

@Getter
public class SectionDto implements HierarchicalDto<SectionDto>{
    private Long id;
    private String name;
    private String sectionType;
    private int pigeonsNumber;
    private Long parentId;
    private List<SectionDto> children = new ArrayList<>();
    private List<PigeonLabelDto> pigeons = new ArrayList<>();

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

    public SectionDto() {
    }
}
