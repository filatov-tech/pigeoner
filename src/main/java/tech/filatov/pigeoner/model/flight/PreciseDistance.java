package tech.filatov.pigeoner.model.flight;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tech.filatov.pigeoner.model.Keeper;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PreciseDistance {

    @EmbeddedId
    private PreciseDistanceId id = new PreciseDistanceId();

    @ManyToOne
    @MapsId("keeperId")
    private Keeper keeper;

    @ManyToOne
    @MapsId("launchPointId")
    private LaunchPoint launchPoint;

    private Double preciseDistance;

    public PreciseDistance(Double preciseDistance, Keeper keeper, LaunchPoint launchPoint) {
        this.preciseDistance = preciseDistance;
        this.keeper = keeper;
        this.launchPoint = launchPoint;

        this.id.setKeeperId(keeper.getId());
        this.id.setLaunchPointId(launchPoint.getId());

        keeper.getPreciseDistances().add(this);
        launchPoint.getPreciseDistances().add(this);
    }
}
