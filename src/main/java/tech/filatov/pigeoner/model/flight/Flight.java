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

    private int distance;

    private LocalDateTime departure;

    private int totalParticipants;

    @OneToMany(mappedBy = "flight")
    private List<FlightResult> participants;

    private String locationName;
}
