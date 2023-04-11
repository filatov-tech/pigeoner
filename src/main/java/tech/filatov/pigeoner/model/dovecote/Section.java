package tech.filatov.pigeoner.model.dovecote;

import lombok.Data;
import lombok.EqualsAndHashCode;
import tech.filatov.pigeoner.model.AbstractOwnedEntity;
import tech.filatov.pigeoner.model.feeding.FeedingMode;
import tech.filatov.pigeoner.model.pigeon.Pigeon;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "SECTION")
@Data
@EqualsAndHashCode(callSuper = true)
public class Section extends AbstractOwnedEntity {

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PARENT_ID")
    private Section parent;

    @OneToMany(mappedBy = "parent")
    private List<Section> children;

    @Enumerated(EnumType.STRING)
    private SectionType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FEEDING_MODE_ID")
    private FeedingMode feedingMode;

    @OneToMany(mappedBy = "location")
    private Set<Pigeon> pigeons;
}
