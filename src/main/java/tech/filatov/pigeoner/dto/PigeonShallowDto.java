package tech.filatov.pigeoner.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tech.filatov.pigeoner.model.pigeon.Condition;
import tech.filatov.pigeoner.model.pigeon.Sex;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PigeonShallowDto extends BaseDto {

    private static final String SEP = ";";
    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    @Pattern(regexp = "^[a-zA-Zа-яА-Я \\d-]+$", message = "Кличка может содержать только буквы, пробелы и знак дефиса \"-\"")
    private String name;
    @Pattern(regexp = "^[-\\d ]+$", message = "Номер кольца может содержать только цифры, пробелы и знак дефиса \"-\"")
    private String ringNumber;
    @PastOrPresent(message = "Датой рождения может быть только прошедшая дата или сегодняшнее число")
    private LocalDate birthdate;
    @Pattern(regexp = "^[a-zA-Zа-яА-Я -]+$", message = "Цвет может содержать только буквы, пробелы и знак дефиса \"-\"")
    private String color;
    @NotNull(message = "Должно быть указано состояние голубя")
    @NotBlank(message = "Должно быть указано состояние голубя")
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
