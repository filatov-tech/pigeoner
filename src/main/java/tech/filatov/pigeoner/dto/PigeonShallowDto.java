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


    public PigeonShallowDto(Long id, String ringNumber, String color, Sex sex, LocalDate birthdate, Long mateId, String mateRingNumber, Condition condition, Long sectionId, String sectionFullName) {
        this.id = id;
        this.ringNumber = ringNumber;
        this.color = color;
        this.sex = sex.getTitle();
        this.birthdate = birthdate;
        this.mateId = mateId;
        this.mateRingNumber = mateRingNumber;
        this.condition = condition == null ? null : condition.getTitle();
        this.sectionId = sectionId;
    }

    public PigeonShallowDto(Long id, String ringNumber, String color, String sex, LocalDate birthdate, Long mateId, String condition) {
        this.id = id;
        this.ringNumber = ringNumber;
        this.color = color;
        this.sex = sex;
        this.birthdate = birthdate;
        this.mateId = mateId;
        this.condition = condition;
    }

    public PigeonShallowDto(Long id,
                            String ringNumber,
                            String color,
                            Sex sex,
                            LocalDate birthdate,
                            String mateRingNumber,
                            Condition condition,
                            Long sectionId) {
        this.id = id;
        this.ringNumber = ringNumber;
        this.color = color;
        this.sex = sex.getTitle();
        this.birthdate = birthdate;
        this.year = birthdate == null ? null : birthdate.getYear();
        this.mateRingNumber = mateRingNumber;
        this.condition = condition == null ? null : condition.getTitle();
        this.sectionId = sectionId;
    }
}
