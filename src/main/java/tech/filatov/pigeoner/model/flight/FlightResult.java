package tech.filatov.pigeoner.model.flight;

import lombok.Data;
import lombok.EqualsAndHashCode;
import tech.filatov.pigeoner.model.AbstractOwnedEntity;
import tech.filatov.pigeoner.model.pigeon.Pigeon;

import javax.persistence.*;
import java.math.BigInteger;
import java.time.LocalDateTime;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class FlightResult extends AbstractOwnedEntity {

    @ManyToOne
    @JoinColumn(name = "PIGEON_ID")
    private Pigeon pigeon;

    @ManyToOne
    @JoinColumn(name = "FLIGHT_ID")
    private Flight flight;

    private LocalDateTime arrivalTime;

    private boolean isPass;

    private int position;

    private BigInteger winPoints;

    @Enumerated(EnumType.STRING)
    private AfterFlightCondition condition;
}
