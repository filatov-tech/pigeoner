package tech.filatov.pigeoner.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;
import tech.filatov.pigeoner.model.flight.FlightType;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
public class FlightDto extends BaseDto {
    @NotNull(message = "У точки вылета должны быть название и дистанция")
    @NotEmpty(message = "У точки вылета должны быть название и дистанция")
    private String locationName;
    @Range(min = 1, max = 2000, message = "Дистанция должна быть в диапозоне от 1 км до 2000 км")
    private int distance;
    private Long numberParticipants;
    private Integer totalParticipants;
    private Integer myPassed;
    @NotNull
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
