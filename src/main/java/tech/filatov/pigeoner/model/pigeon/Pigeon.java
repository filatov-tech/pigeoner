package tech.filatov.pigeoner.model.pigeon;

import lombok.*;
import tech.filatov.pigeoner.model.AbstractOwnedEntity;
import tech.filatov.pigeoner.model.flight.FlightResult;
import tech.filatov.pigeoner.model.dovecote.Section;

import javax.persistence.*;
import java.time.LocalDate;
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

    @OneToMany(mappedBy = "mother")
    private Set<Pigeon> mothersChildren;

    @OneToMany(mappedBy = "father")
    private Set<Pigeon> fathersChildren;

    @Enumerated(EnumType.STRING)
    private Condition conditionStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PHOTO_ID")
    private Image mainPhoto;

    @OneToMany(mappedBy = "pigeon")
    private Set<Image> images;

}
