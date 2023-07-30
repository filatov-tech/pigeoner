package tech.filatov.pigeoner.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import tech.filatov.pigeoner.model.pigeon.Condition;
import tech.filatov.pigeoner.model.pigeon.Sex;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PigeonWithAncestorsDto extends BaseDto {
    private String ringNumber;
    private String name;
    private String sex;
    private int year;
    private LocalDate birthday;
    private String status;
    private Boolean isOwn;
    private Long fatherId;
    private Long motherId;
    private String keeper;
    private Integer depth;
    private PigeonWithAncestorsDto father;
    private PigeonWithAncestorsDto mother;
    private List<FlightResultDto> flights = new ArrayList<>();
    private List<FlightResultDto> topFlights = new ArrayList<>();

    public PigeonWithAncestorsDto(Long id,
                                  String ringNumber,
                                  String name,
                                  String sex,
                                  LocalDate birthdate,
                                  String status,
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
        this.status = Condition.valueOf(status).getTitle();
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

    public void setFather(PigeonWithAncestorsDto father) {
        this.father = father;
    }

    public void setMother(PigeonWithAncestorsDto mother) {
        this.mother = mother;
    }
}
