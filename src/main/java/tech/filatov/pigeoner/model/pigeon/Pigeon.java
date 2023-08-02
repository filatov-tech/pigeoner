package tech.filatov.pigeoner.model.pigeon;

import lombok.*;
import tech.filatov.pigeoner.dto.PigeonDto;
import tech.filatov.pigeoner.model.AbstractOwnedEntity;
import tech.filatov.pigeoner.model.Keeper;
import tech.filatov.pigeoner.model.flight.FlightResult;
import tech.filatov.pigeoner.model.dovecote.Section;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;

import static tech.filatov.pigeoner.constant.Constants.*;

@NamedNativeQuery(
        name = "Pigeon.getWithAncestorsById",
        query = PIGEON_WITH_3_LEVEL_ANCESTORS,
        resultSetMapping = "Mapping.pigeonDto"
)

@SqlResultSetMapping(
        name = "Mapping.pigeonDto",
        classes = @ConstructorResult(targetClass = PigeonDto.class,
                columns = {@ColumnResult(name = "id", type = Long.class),
                        @ColumnResult(name = "ring_number", type = String.class),
                        @ColumnResult(name = "name", type = String.class),
                        @ColumnResult(name = "sex", type = String.class),
                        @ColumnResult(name = "birthdate", type = LocalDate.class),
                        @ColumnResult(name = "condition_status", type = String.class),
                        @ColumnResult(name = "is_own", type = Boolean.class),
                        @ColumnResult(name = "father_id", type = Long.class),
                        @ColumnResult(name = "mother_id", type = Long.class),
                        @ColumnResult(name = "depth", type = Integer.class),
                        @ColumnResult(name = "keeper", type = String.class),
                }
        )
)

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Pigeon extends AbstractOwnedEntity {

    private String ringNumber;

    private String name;

    @Enumerated(EnumType.STRING)
    private Sex sex;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MOTHER_ID")
    private Pigeon mother;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FATHER_ID")
    private Pigeon father;

    private boolean isOwn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "COLOR_ID")
    private Color color;

    @OneToMany(mappedBy = "flight")
    private Set<FlightResult> flights;

    private LocalDate birthdate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SECTION_ID")
    private Section section;

    @OneToOne(fetch = FetchType.LAZY)
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "KEEPER_ID")
    private Keeper keeper;

    public Pigeon(long id) {
        this.setId(id);
    }
}
