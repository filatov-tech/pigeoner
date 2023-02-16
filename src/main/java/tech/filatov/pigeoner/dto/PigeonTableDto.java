package tech.filatov.pigeoner.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PigeonTableDto {

    private static final String SEP = ";";
    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    private Long id;
    private String ringNumber;
    private String color;
    private String sex;
    private LocalDate birthday;
    private String age;
    private String mate;
    private String status;

    public String toOneRow() {
        return ringNumber + SEP +
                color + SEP +
                sex + SEP +
                birthday.format(dtf) + SEP +
                age + SEP +
                mate + SEP +
                status + SEP;
    }
}
