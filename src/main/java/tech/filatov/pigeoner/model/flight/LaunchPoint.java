package tech.filatov.pigeoner.model.flight;

import lombok.Data;
import lombok.EqualsAndHashCode;
import tech.filatov.pigeoner.model.AbstractOwnedEntity;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class LaunchPoint extends AbstractOwnedEntity {

    @NotNull
    private String name;

    private int distance;
}
