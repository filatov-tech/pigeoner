package tech.filatov.pigeoner.model.flight;

import lombok.Data;
import lombok.EqualsAndHashCode;
import tech.filatov.pigeoner.model.AbstractOwnedEntity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class Flight extends AbstractOwnedEntity {

    private String locationName;

    private int distance;

    private LocalDateTime departure;

    @OneToMany(mappedBy = "flight")
    private List<FlightResult> myParticipants;

    private Integer totalParticipants;

    private Integer passingThreshold;

    private Integer myPassed;

    @Enumerated(EnumType.STRING)
    private FlightType flightType;

}
