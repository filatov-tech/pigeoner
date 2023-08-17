package tech.filatov.pigeoner.model.flight;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import tech.filatov.pigeoner.model.AbstractOwnedEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class Flight extends AbstractOwnedEntity {

    @EqualsAndHashCode.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    private LaunchPoint launchPoint;

    private LocalDateTime departure;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToMany(mappedBy = "flight")
    private List<FlightResult> myParticipants;

    private Integer totalParticipants;

    private Integer passingThreshold;

    private Integer myPassed;

    @NotNull
    @EqualsAndHashCode.Exclude
    @Enumerated(EnumType.STRING)
    private FlightType flightType;
}
