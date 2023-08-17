package tech.filatov.pigeoner.model.flight;

import lombok.Data;
import lombok.EqualsAndHashCode;
import tech.filatov.pigeoner.model.AbstractOwnedEntity;
import tech.filatov.pigeoner.model.pigeon.Pigeon;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class FlightResult extends AbstractOwnedEntity {

    @NotNull
    @EqualsAndHashCode.Exclude
    @ManyToOne
    @JoinColumn(name = "PIGEON_ID")
    private Pigeon pigeon;

    @NotNull
    @EqualsAndHashCode.Exclude
    @ManyToOne
    @JoinColumn(name = "FLIGHT_ID")
    private Flight flight;

    private LocalDateTime arrivalTime;

    private Boolean isPass ;

    private int position = 0;

    private Double winPoints;

    private Double averageSpeed;

    @Enumerated(EnumType.STRING)
    private AfterFlightCondition condition = AfterFlightCondition.NORMAL;
}
