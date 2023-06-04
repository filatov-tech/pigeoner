package tech.filatov.pigeoner.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class FlightResultDto {
    private Long pigeonId;
    private String location;
    private int distance;
    private int position;
    private int totalParticipants;
    private LocalDateTime arrivalTime;
    private boolean isPass;

    public FlightResultDto(FlightResultDto fr) {
        this(fr.getPigeonId(),
                fr.getLocation(),
                fr.getDistance(),
                fr.getPosition(),
                fr.getTotalParticipants(),
                fr.getArrivalTime(),
                fr.isPass()
        );
    }
}
