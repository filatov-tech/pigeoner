package tech.filatov.pigeoner.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class FlightTableDto {
    private String locationName;
    private int distance;
    private Long numberParticipants;
    private Integer totalParticipants;
    private Integer myPassed;
    private LocalDateTime departure;
}
