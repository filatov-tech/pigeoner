package tech.filatov.pigeoner.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import tech.filatov.pigeoner.model.flight.PreciseDistance;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Keeper extends AbstractOwnedEntity {

    @Column(unique = true)
    private String name;

    @OneToMany(mappedBy = "keeper")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<PreciseDistance> preciseDistances = new HashSet<>();

    public Keeper(long id) {
        this.setId(id);
    }
}
