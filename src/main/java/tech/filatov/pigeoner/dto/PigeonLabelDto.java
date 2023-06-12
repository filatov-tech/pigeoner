package tech.filatov.pigeoner.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import tech.filatov.pigeoner.model.pigeon.Sex;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PigeonLabelDto {
    private Long id;
    private String ringNumber;
    private String sex;
    private Long sectionId;

    public PigeonLabelDto(Long id, String ringNumber, Sex sex, Long sectionId) {
        this.id = id;
        this.ringNumber = ringNumber;
        this.sex = sex.getTitle().toLowerCase();
        this.sectionId = sectionId;
    }
}
