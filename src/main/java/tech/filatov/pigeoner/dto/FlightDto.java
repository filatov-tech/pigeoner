package tech.filatov.pigeoner.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import tech.filatov.pigeoner.model.flight.FlightType;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
public class FlightDto {
    private Long id;
    private String locationName;
    private int distance;
    private Long numberParticipants;
    private Integer totalParticipants;
    private Integer myPassed;
    private LocalDateTime departure;
    private Integer passingThreshold;
    private String flightType;

    public FlightDto(Long id, String locationName, int distance, Long numberParticipants, Integer totalParticipants, Integer myPassed, LocalDateTime departure, Integer passingThreshold, FlightType flightType) {
        this.id = id;
        this.locationName = locationName;
        this.distance = distance;
        this.numberParticipants = numberParticipants;
        this.totalParticipants = totalParticipants;
        this.myPassed = myPassed;
        this.departure = departure;
        this.passingThreshold = passingThreshold;
        this.flightType = flightType.getTitle();
    }
}
