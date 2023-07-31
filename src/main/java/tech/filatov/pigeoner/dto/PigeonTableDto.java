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
public class PigeonTableDto extends BaseDto {

    private static final String SEP = ";";
    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    private String ringNumber;
    private String color;
    private String sex;
    private LocalDate birthday;
    private Integer year;
    private String age;
    private Long mateId;
    private String mateRingNumber;
    private String condition;
    private Long sectionId;
    private SectionDto section;


    public PigeonTableDto(Long id, String ringNumber, String color, Sex sex, LocalDate birthday, Long mateId, String mateRingNumber, Condition condition, Long sectionId, String sectionFullName) {
        this.id = id;
        this.ringNumber = ringNumber;
        this.color = color;
        this.sex = sex.getTitle();
        this.birthday = birthday;
        this.mateId = mateId;
        this.mateRingNumber = mateRingNumber;
        this.condition = condition == null ? null : condition.getTitle();
        this.sectionId = sectionId;
    }

    public PigeonTableDto(Long id, String ringNumber, String color, String sex, LocalDate birthday, Long mateId, String condition) {
        this.id = id;
        this.ringNumber = ringNumber;
        this.color = color;
        this.sex = sex;
        this.birthday = birthday;
        this.mateId = mateId;
        this.condition = condition;
    }

    public PigeonTableDto(Long id,
                          String ringNumber,
                          String color,
                          Sex sex,
                          LocalDate birthday,
                          String mateRingNumber,
                          Condition condition,
                          Long sectionId) {
        this.id = id;
        this.ringNumber = ringNumber;
        this.color = color;
        this.sex = sex.getTitle();
        this.birthday = birthday;
        this.year = birthday == null ? null : birthday.getYear();
        this.mateRingNumber = mateRingNumber;
        this.condition = condition == null ? null : condition.getTitle();
        this.sectionId = sectionId;
    }
}
