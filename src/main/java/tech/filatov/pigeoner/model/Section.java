package tech.filatov.pigeoner.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parent")
    private List<Section> children;

    @Enumerated(EnumType.STRING)
    private SectionType type;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "location")
    private Set<Pigeon> pigeons;
}
