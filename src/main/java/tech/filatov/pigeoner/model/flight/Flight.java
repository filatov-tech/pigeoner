package tech.filatov.pigeoner.model.flight;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import tech.filatov.pigeoner.model.AbstractOwnedEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class Flight extends AbstractOwnedEntity {

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private LaunchPoint launchPoint;

    @NotNull
    private LocalDateTime departure;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToMany(mappedBy = "flight")
    private Set<FlightResult> myParticipants = new HashSet<>();

    private Integer totalParticipants;

    private Integer passingThreshold;

    private Integer myPassed;

    @NotNull
    @EqualsAndHashCode.Exclude
    @Enumerated(EnumType.STRING)
    private FlightType flightType;
}
