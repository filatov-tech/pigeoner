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

    private Long id;
    private String ringNumber;
    private String color;
    private String sex;
    private LocalDate birthday;
    private String age;
    private Long mateId;
    private String mateRingNumber;
    private String status;

    public PigeonTableDto(Long id, String ringNumber, String color, Sex sex, LocalDate birthday, Long mateId, String mateRingNumber, Condition status, Long sectionId, String sectionFullName) {
        this.id = id;
        this.ringNumber = ringNumber;
        this.color = color;
        this.sex = sex.getTitle();
        this.birthday = birthday;
        this.mateId = mateId;
        this.mateRingNumber = mateRingNumber;
        this.status = status == null ? null : status.getTitle();
        this.sectionId = sectionId;
        this.sectionFullName = sectionFullName;
    }

    private Long sectionId;
    private String sectionFullName;

    public String toOneRow() {
        return ringNumber + SEP +
                color + SEP +
                sex + SEP +
                birthday.format(dtf) + SEP +
                age + SEP +
                mateId + SEP +
                status + SEP;
    }

    public PigeonTableDto(Long id, String ringNumber, String color, String sex, LocalDate birthday, Long mateId, String status) {
        this.id = id;
        this.ringNumber = ringNumber;
        this.color = color;
        this.sex = sex;
        this.birthday = birthday;
        this.mateId = mateId;
        this.status = status;
    }

    public PigeonTableDto(Long id,
                          String ringNumber,
                          String color,
                          Sex sex,
                          LocalDate birthday,
                          String mateRingNumber,
                          Condition status,
                          Long sectionId) {
        this.id = id;
        this.ringNumber = ringNumber;
        this.color = color;
        this.sex = sex.getTitle();
        this.birthday = birthday;
        this.mateRingNumber = mateRingNumber;
        this.status = status == null ? null : status.getTitle();
        this.sectionId = sectionId;
    }
}
