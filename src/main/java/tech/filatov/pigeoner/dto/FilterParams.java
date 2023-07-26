package tech.filatov.pigeoner.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tech.filatov.pigeoner.model.pigeon.Condition;
import tech.filatov.pigeoner.model.pigeon.Sex;
import tech.filatov.pigeoner.util.NullStringDeserializer;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class FilterParams {
    @JsonDeserialize(using = NullStringDeserializer.class)
    private String ringNumber;
    private Condition condition;
    private Long dovecote;
    @JsonDeserialize(using = NullStringDeserializer.class)
    private String name;
    private String dateFilterType;
    private LocalDate birthdateFrom;
    private LocalDate birthdateTo;
    private Integer ageYearFrom;
    private Integer ageMonthFrom;
    private Integer ageYearTo;
    private Integer ageMonthTo;
    private LocalDate yearFrom;
    private LocalDate yearTo;
    private Long keeper;
    private Sex sex;
    @JsonDeserialize(using = NullStringDeserializer.class)
    private String mate;
}
