package tech.filatov.pigeoner.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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

    private LocalDateTime departure;

    private LocalDateTime arrival;
}
