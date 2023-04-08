package tech.filatov.pigeoner.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.util.Set;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class FeedingMode extends AbstractOwnedEntity{

    private String name;

    @OneToMany
    @JoinColumn(name = "FEEDING_ELEMENT_ID")
    private Set<FeedingElement> elements;
}
