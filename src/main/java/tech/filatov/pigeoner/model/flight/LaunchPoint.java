package tech.filatov.pigeoner.model.flight;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Range;
import tech.filatov.pigeoner.model.AbstractOwnedEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

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
}
