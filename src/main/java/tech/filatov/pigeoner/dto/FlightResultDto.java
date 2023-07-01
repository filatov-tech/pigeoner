package tech.filatov.pigeoner.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import tech.filatov.pigeoner.model.flight.AfterFlightCondition;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class FlightResultDto {
    private Long id;
    private Long pigeonId;
    private String ringNumber;
    private String location;
    private int distance;
    private Integer position;
    private Integer totalParticipants;
    private LocalDateTime arrivalTime;
    private Boolean isPass;
    private Double winPoints;
    private Double averageSpeed;
    private String afterFlightCondition;
    private String keeper;

    public FlightResultDto(FlightResultDto fr) {
        this(fr.getPigeonId(),
                fr.getLocation(),
                fr.getDistance(),
                fr.getPosition(),
                fr.getTotalParticipants(),
                fr.getArrivalTime(),
                fr.getIsPass()
        );
    }

    public FlightResultDto(Long pigeonId, String location, int distance, int position, int totalParticipants, LocalDateTime arrivalTime, boolean isPass) {
        this.pigeonId = pigeonId;
        this.location = location;
        this.distance = distance;
        this.position = position;
        this.totalParticipants = totalParticipants;
        this.arrivalTime = arrivalTime;
        this.isPass = isPass;
    }

    public FlightResultDto(Long id, Long pigeonId, String ringNumber, Integer position, LocalDateTime arrivalTime, Boolean isPass, Double winPoints, Double averageSpeed, AfterFlightCondition afterFlightCondition, String keeper) {
        this.id = id;
        this.pigeonId = pigeonId;
        this.ringNumber = ringNumber;
        this.position = position;
        this.arrivalTime = arrivalTime;
        this.isPass = isPass;
        this.winPoints = winPoints;
        this.averageSpeed = averageSpeed;
        this.afterFlightCondition = afterFlightCondition.label;
        this.keeper = keeper;
    }
}
