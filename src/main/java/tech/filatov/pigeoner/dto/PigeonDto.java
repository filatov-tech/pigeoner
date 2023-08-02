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
    private int year;
    private Boolean isOwn;
    private Long mateId;
    private PigeonDto mate;
    private Long fatherId;
    private PigeonDto father;
    private Long motherId;
    private PigeonDto mother;
    private Long keeperId;
    private String keeper;
    private Long sectionId;
    private SectionDto section;
    private Integer depth;
    private List<FlightResultDto> flights = new ArrayList<>();
    private List<FlightResultDto> topFlights = new ArrayList<>();

    public PigeonDto(Long id,
                     String ringNumber,
                     String name,
                     String sex,
                     LocalDate birthdate,
                     String condition,
                     Boolean isOwn,
                     Long fatherId,
                     Long motherId,
                     Integer depth,
                     String keeper) {
        this.id = id;
        this.ringNumber = ringNumber;
        this.name = name;
        this.sex = Sex.valueOf(sex).getTitle();
        this.year = birthdate.getYear();
        this.condition = Condition.valueOf(condition).getTitle();
        this.isOwn = isOwn;
        this.fatherId = fatherId;
        this.motherId = motherId;
        this.depth = depth;
        this.keeper = keeper;
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

    public void setFather(PigeonDto father) {
        this.father = father;
    }

    public void setMother(PigeonDto mother) {
        this.mother = mother;
    }
}
