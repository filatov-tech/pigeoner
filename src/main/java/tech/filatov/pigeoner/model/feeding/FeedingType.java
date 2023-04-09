package tech.filatov.pigeoner.model.feeding;

import lombok.Data;
import lombok.EqualsAndHashCode;
import tech.filatov.pigeoner.model.AbstractOwnedEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "FEEDING_TYPE")
@Data
@EqualsAndHashCode(callSuper = true)
public class FeedingType extends AbstractOwnedEntity {
    private String name;
}
