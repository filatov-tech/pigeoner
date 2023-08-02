package tech.filatov.pigeoner.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tech.filatov.pigeoner.model.pigeon.Condition;
import tech.filatov.pigeoner.model.pigeon.Sex;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PigeonShallowDto extends BaseDto {

    private static final String SEP = ";";
    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    private String name;
    private String ringNumber;
    private LocalDate birthdate;
    private String color;
    private String condition;
    private String sex;
    private Integer year;
    private Boolean isOwn;
    private Long mateId;
    private String mateRingNumber;
    private Long fatherId;
    private Long motherId;
    private Long keeperId;
    private Long sectionId;
    private SectionDto section;

    public PigeonShallowDto(Long id,
                            String name,
                            String ringNumber,
                            LocalDate birthdate,
                            String color,
                            Condition condition,
                            Sex sex,
                            Boolean isOwn,
                            Long mateId,
                            String mateRingNumber,
                            Long fatherId,
                            Long motherId,
                            Long keeperId,
                            Long sectionId) {
        this.id = id;
        this.name = name;
        this.ringNumber = ringNumber;
        this.birthdate = birthdate;
        this.color = color;
        this.condition = condition == null ? null : condition.getTitle();
        this.sex = sex == null ? null : sex.getTitle();
        this.year = birthdate == null ? null : birthdate.getYear();
        this.isOwn = isOwn;
        this.mateId = mateId;
        this.mateRingNumber = mateRingNumber;
        this.fatherId = fatherId;
        this.motherId = motherId;
        this.keeperId = keeperId;
        this.sectionId = sectionId;
    }
}
