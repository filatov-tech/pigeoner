package tech.filatov.pigeoner.model.feeding;

import lombok.Data;
import lombok.EqualsAndHashCode;
import tech.filatov.pigeoner.model.AbstractOwnedEntity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.util.Set;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class FeedingMode extends AbstractOwnedEntity {

    private String name;

    @EqualsAndHashCode.Exclude
    @OneToMany
    @JoinColumn(name = "FEEDING_ELEMENT_ID")
    private Set<FeedingElement> elements;
}
