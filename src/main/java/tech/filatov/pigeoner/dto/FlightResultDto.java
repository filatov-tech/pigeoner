package tech.filatov.pigeoner.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import tech.filatov.pigeoner.model.flight.AfterFlightCondition;
import tech.filatov.pigeoner.util.CustomDateTimeDeserializer;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class FlightResultDto extends BaseDto {
    @NotNull
    private Long pigeonId;
    private String ringNumber;
    private String pigeonName;
    private LaunchPointDto launchPoint;
    private Integer position;
    private Integer totalParticipants;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    @JsonDeserialize(using = CustomDateTimeDeserializer.class)
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
                           String pigeonName,
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
        this.pigeonName = pigeonName;
        this.position = position;
        this.arrivalTime = arrivalTime;
        this.isPass = isPass;
        this.winPoints = winPoints;
        this.averageSpeed = averageSpeed;
        this.afterFlightCondition = afterFlightCondition.getTitle();
        this.keeper = keeper;
    }
}
