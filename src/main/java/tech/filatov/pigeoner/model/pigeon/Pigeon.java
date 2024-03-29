package tech.filatov.pigeoner.model.pigeon;

import lombok.*;
import tech.filatov.pigeoner.dto.PigeonDto;
import tech.filatov.pigeoner.model.AbstractOwnedEntity;
import tech.filatov.pigeoner.model.Keeper;
import tech.filatov.pigeoner.model.flight.FlightResult;
import tech.filatov.pigeoner.model.dovecote.Section;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static tech.filatov.pigeoner.constant.Constants.*;

@NamedNativeQuery(
        name = "Pigeon.getWithAncestorsById",
        query = PIGEON_WITH_3_LEVEL_ANCESTORS,
        resultSetMapping = "Mapping.pigeonDto"
)

@NamedNativeQuery(
        name = "Pigeon.getWithAllAncestorsById",
        query = PIGEON_WITH_ALL_ANCESTORS,
        resultSetMapping = "Mapping.pigeonDto"
)

@NamedNativeQuery(
        name = "Pigeon.getWithAllDescendantsById",
        query = PIGEON_WITH_ALL_DESCENDANTS,
        resultSetMapping = "Mapping.pigeonDto"
)

@SqlResultSetMapping(
        name = "Mapping.pigeonDto",
        classes = @ConstructorResult(targetClass = PigeonDto.class,
                columns = {@ColumnResult(name = "id", type = Long.class),
                        @ColumnResult(name = "name", type = String.class),
                        @ColumnResult(name = "ring_number", type = String.class),
                        @ColumnResult(name = "birthdate", type = LocalDate.class),
                        @ColumnResult(name = "condition_status", type = String.class),
                        @ColumnResult(name = "sex", type = String.class),
                        @ColumnResult(name = "is_own", type = Boolean.class),
                        @ColumnResult(name = "country_code", type = String.class),
                        @ColumnResult(name = "mate_id", type = Long.class),
                        @ColumnResult(name = "father_id", type = Long.class),
                        @ColumnResult(name = "mother_id", type = Long.class),
                        @ColumnResult(name = "keeper_id", type = Long.class),
                        @ColumnResult(name = "keeper_name", type = String.class),
                        @ColumnResult(name = "section_id", type = Long.class),
                }
        )
)
@Table(uniqueConstraints = {
        @UniqueConstraint(name = "UniqueNameWithinUser", columnNames = {"name", "user_id"}),
        @UniqueConstraint(name = "UniqueRingNumberWithinUser", columnNames = {"ringNumber", "user_id"})
})
@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class Pigeon extends AbstractOwnedEntity {

    @NotNull
    @EqualsAndHashCode.Include
    private String ringNumber;

    @EqualsAndHashCode.Include
    private String name;

    @Enumerated(EnumType.STRING)
    private Sex sex;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MOTHER_ID")
    @ToString.Exclude
    private Pigeon mother;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FATHER_ID")
    @ToString.Exclude
    private Pigeon father;

    private boolean isOwn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "COLOR_ID")
    private Color color;

    @OneToMany(mappedBy = "flight", cascade = CascadeType.REMOVE)
    @ToString.Exclude
    private Set<FlightResult> flights;

    private LocalDate birthdate;

    private String countryCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SECTION_ID")
    private Section section;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MATE_ID")
    @ToString.Exclude
    private Pigeon mate;

    @OneToMany(mappedBy = "mother")
    @ToString.Exclude
    private Set<Pigeon> mothersChildren;

    @OneToMany(mappedBy = "father")
    @ToString.Exclude
    private Set<Pigeon> fathersChildren;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Condition conditionStatus;

    @ToString.Exclude
    @ElementCollection
    @CollectionTable(name = "IMAGE")
    private Set<Image> images = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "KEEPER_ID")
    private Keeper keeper;

    public Pigeon(long id) {
        this.setId(id);
    }
}
