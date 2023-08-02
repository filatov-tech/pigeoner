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
    private String color;
    private String condition;
    private String sex;
    private Integer year;
    private Boolean isOwn;
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

    public PigeonDto(Long id,
                     String name,
                     String ringNumber,
                     LocalDate birthdate,
                     String condition,
                     String sex,
                     Boolean isOwn,
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
        this.condition = Condition.valueOf(condition).getTitle();
        this.sex = Sex.valueOf(sex).getTitle();
        this.year = birthdate == null ? null : birthdate.getYear();
        this.isOwn = isOwn;
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
                     Condition condition,
                     Sex sex,
                     Boolean isOwn,
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
        this.condition = condition == null ? null : condition.getTitle();
        this.sex = sex == null ? null : sex.getTitle();
        this.year = birthdate == null ? null : birthdate.getYear();
        this.isOwn = isOwn;
        this.mateId = mateId;
        this.fatherId = fatherId;
        this.motherId = motherId;
        this.keeperId = keeperId;
        this.keeperName = keeperName;
        this.sectionId = sectionId;
    }

    public void setFlights(List<FlightResultDto> flights) {
        this.flights = flights;
        this.setTopFlights(flights);
    }

    private void setTopFlights(List<FlightResultDto> flights) {
        List<FlightResultDto> flightsCopy = flights.stream()
                .map(FlightResultDto::new).sorted((o1, o2) -> {
                    int relativePosition1;
                    int relativePosition2;
                    if (o1.getTotalParticipants() == 0 || o2.getTotalParticipants() == 0) {
                        relativePosition1 = o1.getPosition();
                        relativePosition2 = o2.getPosition();
                    } else {
                        relativePosition1 = (o1.getPosition() * 100) / o1.getTotalParticipants();
                        relativePosition2 = (o2.getPosition() * 100) / o2.getTotalParticipants();
                    }
                    return relativePosition1 - relativePosition2;
        }).toList();
        if (flightsCopy.size() < 3) {
            topFlights = flightsCopy;
        } else {
            for (int i = 0; i < 3; i++) {
                topFlights.add(flightsCopy.get(i));
            }
        }
    }
}
