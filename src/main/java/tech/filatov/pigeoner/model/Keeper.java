package tech.filatov.pigeoner.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import tech.filatov.pigeoner.model.flight.PreciseDistance;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Keeper extends AbstractOwnedEntity {

    private String name;

    @OneToMany(mappedBy = "keeper")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<PreciseDistance> preciseDistances = new HashSet<>();

    public Keeper(long id) {
        this.setId(id);
    }

    public Map<Long, Double> getLookupPreciseDistancesMap() {
        Map<Long, Double> preciseDistancesMap = new HashMap<>();
        for (PreciseDistance preciseDistance : this.getPreciseDistances()) {
            preciseDistancesMap.put(
                    preciseDistance.getLaunchPoint().getId(),
                    preciseDistance.getPreciseDistance()
            );
        }
        return preciseDistancesMap;
    }
}
