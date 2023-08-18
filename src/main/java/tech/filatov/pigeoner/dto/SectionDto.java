package tech.filatov.pigeoner.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tech.filatov.pigeoner.model.dovecote.SectionType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SectionDto extends BaseDto {
    @NotNull(message = "У голубятни и ее составляющих должно быть название")
    @NotBlank(message = "У голубятни и ее составляющих должно быть название")
    @Pattern(regexp = "^[a-zA-Zа-яА-Я \\d-.]+$", message = "Допустимы только буквы, пробелы, точки и знак дефиса \"-\"")
    private String name;
    private Long parentId;
    private String fullAddress;
    @NotNull
    @NotBlank
    private String sectionType;
    private Integer pigeonsNumber;
    private String rootName;
    private List<SectionDto> children = new ArrayList<>();
    private List<PigeonLabelDto> pigeons = new ArrayList<>();

    public SectionDto(Long id, String name, Long parentId) {
        this.id = id;
        this.name = name;
        this.parentId = parentId;
    }

    public SectionDto(Long id, String name, Long parentId, SectionType sectionType) {
        this.id = id;
        this.name = name;
        this.parentId = parentId;
        this.sectionType = sectionType == null ? null : sectionType.name();
    }

    public SectionDto(Long id, String name, String sectionType, Integer pigeonsNumber) {
        this.id = id;
        this.name = name;
        this.sectionType = sectionType;
        this.pigeonsNumber = pigeonsNumber;
    }

    public SectionDto(Long id, String name, String sectionType, Long parentId, Integer pigeonsNumber) {
        this.id = id;
        this.name = name;
        this.sectionType = sectionType;
        this.parentId = parentId;
        this.pigeonsNumber = pigeonsNumber;
    }
}
