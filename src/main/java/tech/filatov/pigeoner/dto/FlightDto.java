package tech.filatov.pigeoner.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tech.filatov.pigeoner.model.flight.FlightType;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
public class FlightDto extends BaseDto {
    @NotNull(message = "Вылет должен иметь информацию о точке запуска: место и расстояние")
    private LaunchPointDto launchPoint;
    private Long numberParticipants;
    private Integer totalParticipants;
    private Integer myPassed;
    @NotNull(message = "Вылет должен иметь информацию о дате и времени выпуска голубей")
    private LocalDateTime departure;
    private Integer passingThreshold;
    @NotNull(message = "Вылет должен быть причислен к одному из типов")
    private String flightType;

    public FlightDto(Long id, String locationName, int distance, Long numberParticipants, Integer totalParticipants, Integer myPassed, LocalDateTime departure, Integer passingThreshold, FlightType flightType) {
        this.id = id;
        this.launchPoint = new LaunchPointDto(locationName, distance);
        this.numberParticipants = numberParticipants;
        this.totalParticipants = totalParticipants;
        this.myPassed = myPassed;
        this.departure = departure;
        this.passingThreshold = passingThreshold;
        this.flightType = flightType.getTitle();
    }

    public FlightDto(Long id, Long launchPointId, String locationName, int distance, Integer totalParticipants, Integer myPassed, LocalDateTime departure, Integer passingThreshold, FlightType flightType) {
        this.id = id;
        this.launchPoint = new LaunchPointDto(launchPointId, locationName, distance);
        this.totalParticipants = totalParticipants;
        this.myPassed = myPassed;
        this.departure = departure;
        this.passingThreshold = passingThreshold;
        this.flightType = flightType.getTitle();
    }

    public FlightDto(Long id, String locationName, int distance, Long numberParticipants, Integer totalParticipants, Integer myPassed, LocalDateTime departure, Integer passingThreshold, String flightType) {
        this.id = id;
        this.launchPoint = new LaunchPointDto(locationName, distance);
        this.numberParticipants = numberParticipants;
        this.totalParticipants = totalParticipants;
        this.myPassed = myPassed;
        this.departure = departure;
        this.passingThreshold = passingThreshold;
        this.flightType = flightType;
    }
}
