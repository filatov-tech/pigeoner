package tech.filatov.pigeoner.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class DepartureLocation extends AbstractOwnedEntity {

    private String name;
}
