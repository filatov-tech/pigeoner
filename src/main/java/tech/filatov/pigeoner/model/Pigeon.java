package tech.filatov.pigeoner.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class Pigeon extends AbstractOwnedEntity {

    private String ringNumber;

    private String name;

    private boolean isMale;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MOTHER_ID")
    private Pigeon mother;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FATHER_ID")
    private Pigeon father;

    private boolean isNative;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "COLOR_ID")
    private Color color;

    @OneToMany(mappedBy = "flight")
    private Set<FlightResult> flights;

    private LocalDate birthdate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SECTION_ID")
    private Section location;

    @OneToOne
    @JoinColumn(name = "MATE_ID")
    private Pigeon mate;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "mother")
    private Set<Pigeon> mothersChildren;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "father")
    private Set<Pigeon> fathersChildren;

    @Enumerated(EnumType.STRING)
    private Condition conditionStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PHOTO_ID")
    private Image mainPhoto;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "pigeon")
    private Set<Image> images;

}
