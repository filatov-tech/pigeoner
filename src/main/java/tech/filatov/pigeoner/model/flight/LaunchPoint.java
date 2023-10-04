package tech.filatov.pigeoner.model.flight;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.validator.constraints.Range;
import tech.filatov.pigeoner.model.AbstractOwnedEntity;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Table(uniqueConstraints = {
        @UniqueConstraint(name = "UniqueNameWithDistance", columnNames = {"name", "distance"})
})
@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class LaunchPoint extends AbstractOwnedEntity {

    @NotNull
    private String name;

    @Range(min = 1, max = 2000)
    private int distance;

    @OneToMany(mappedBy = "launchPoint")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<PreciseDistance> preciseDistances = new HashSet<>();
}
