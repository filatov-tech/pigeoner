package tech.filatov.pigeoner.model.flight;

import lombok.Data;
import lombok.EqualsAndHashCode;
import tech.filatov.pigeoner.model.AbstractOwnedEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Table(uniqueConstraints = {
        @UniqueConstraint(name = "UniqueFlight", columnNames = {"launch_point_id", "departure", "flightType"})
})
@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class Flight extends AbstractOwnedEntity {

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private LaunchPoint launchPoint;

    @NotNull
    private LocalDateTime departure;

    private Integer totalParticipants;

    private Integer passingThreshold;

    private Double passingAvgSpeed;

    @NotNull
    @EqualsAndHashCode.Exclude
    @Enumerated(EnumType.STRING)
    private FlightType flightType;
}
