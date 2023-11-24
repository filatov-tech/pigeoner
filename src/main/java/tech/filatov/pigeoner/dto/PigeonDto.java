package tech.filatov.pigeoner.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tech.filatov.pigeoner.model.pigeon.Condition;
import tech.filatov.pigeoner.model.pigeon.Sex;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PigeonDto extends BaseDto {
    private String name;
    private String ringNumber;
    private LocalDate birthdate;
    private Long colorId;
    private String color;
    private String condition;
    private String sex;
    private Integer year;
    private Boolean isOwn;
    private String countryCode;
    private Long mateId;
    private PigeonDto mate;
    private Long fatherId;
    private PigeonDto father;
    private Long motherId;
    private PigeonDto mother;
    private Long keeperId;
    private String keeperName;
    private Long sectionId;
    private SectionDto section;
    private List<FlightResultDto> flights = new ArrayList<>();
    private List<FlightResultDto> topFlights = new ArrayList<>();
    private Integer imageNumber;

    public PigeonDto(Long id,
                     String name,
                     String ringNumber,
                     LocalDate birthdate,
                     String condition,
                     String sex,
                     Boolean isOwn,
                     String countryCode,
                     Long mateId,
                     Long fatherId,
                     Long motherId,
                     Long keeperId,
                     String keeperName,
                     Long sectionId) {
        this.id = id;
        this.name = name;
        this.ringNumber = ringNumber;
        this.birthdate = birthdate;
        this.condition = condition;
        this.sex = sex;
        this.year = birthdate == null ? null : birthdate.getYear();
        this.isOwn = isOwn;
        this.countryCode = countryCode;
        this.mateId = mateId;
        this.fatherId = fatherId;
        this.motherId = motherId;
        this.keeperId = keeperId;
        this.keeperName = keeperName;
        this.sectionId = sectionId;
    }

    public PigeonDto(Long id,
                     String name,
                     String ringNumber,
                     LocalDate birthdate,
                     Long colorId,
                     Condition condition,
                     Sex sex,
                     Boolean isOwn,
                     String countryCode,
                     Long mateId,
                     Long fatherId,
                     Long motherId,
                     Long keeperId,
                     String keeperName,
                     Long sectionId) {
        this.id = id;
        this.name = name;
        this.ringNumber = ringNumber;
        this.birthdate = birthdate;
        this.colorId = colorId;
        this.condition = condition == null ? null : condition.name();
        this.sex = sex == null ? null : sex.name();
        this.year = birthdate == null ? null : birthdate.getYear();
        this.isOwn = isOwn;
        this.countryCode = countryCode;
        this.mateId = mateId;
        this.fatherId = fatherId;
        this.motherId = motherId;
        this.keeperId = keeperId;
        this.keeperName = keeperName;
        this.sectionId = sectionId;
    }
}
