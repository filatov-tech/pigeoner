package tech.filatov.pigeoner.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import tech.filatov.pigeoner.model.flight.AfterFlightCondition;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class FlightResultDto extends BaseDto {
    @NotNull
    private Long pigeonId;
    private String ringNumber;
    private LaunchPointDto launchPoint;
    private Integer position;
    private Integer totalParticipants;
    private LocalDateTime arrivalTime;
    private Boolean isPass;
    private Double winPoints;
    private Double averageSpeed;
    private String afterFlightCondition;
    private String keeper;

    public FlightResultDto(Long pigeonId,
                           Long launchPointId,
                           String launchPointName,
                           Integer launchPointDistance,
                           Integer position,
                           Integer totalParticipants,
                           LocalDateTime arrivalTime,
                           Boolean isPass) {
        this.pigeonId = pigeonId;
        this.launchPoint = new LaunchPointDto(launchPointId, launchPointName, launchPointDistance);
        this.position = position;
        this.totalParticipants = totalParticipants == null ? 0 : totalParticipants;
        this.arrivalTime = arrivalTime;
        this.isPass = isPass;
    }

    public FlightResultDto(Long id,
                           Long pigeonId,
                           String ringNumber,
                           Integer position,
                           LocalDateTime arrivalTime,
                           Boolean isPass,
                           Double winPoints,
                           Double averageSpeed,
                           AfterFlightCondition afterFlightCondition,
                           String keeper) {
        this.id = id;
        this.pigeonId = pigeonId;
        this.ringNumber = ringNumber;
        this.position = position;
        this.arrivalTime = arrivalTime;
        this.isPass = isPass;
        this.winPoints = winPoints;
        this.averageSpeed = averageSpeed;
        this.afterFlightCondition = afterFlightCondition.getTitle();
        this.keeper = keeper;
    }
}
