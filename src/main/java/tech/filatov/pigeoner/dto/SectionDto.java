package tech.filatov.pigeoner.dto;

import lombok.Getter;

@Getter
public class SectionDto {
    private long id;
    private String name;
    private String sectionType;
    private int pigeonsNumber;

    public SectionDto(long id, String name, String sectionType, int pigeonsNumber) {
        this.id = id;
        this.name = name;
        this.sectionType = sectionType;
        this.pigeonsNumber = pigeonsNumber;
    }

    public SectionDto() {
    }
}
