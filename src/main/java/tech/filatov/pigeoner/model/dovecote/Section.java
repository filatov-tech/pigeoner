package tech.filatov.pigeoner.model.dovecote;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import tech.filatov.pigeoner.dto.SectionDto;
import tech.filatov.pigeoner.model.AbstractOwnedEntity;
import tech.filatov.pigeoner.model.feeding.FeedingMode;
import tech.filatov.pigeoner.model.pigeon.Pigeon;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

import static tech.filatov.pigeoner.constant.Constants.*;

@NamedNativeQueries({
        @NamedNativeQuery(
                name = "Section.getTopLevelCommonSectionInfo",
                query = SECTION_DTO_COMMON_NATIVE_QUERY + SECTION_DTO_ROOT,
                resultSetMapping = "Mapping.SectionDto"
        ),
        @NamedNativeQuery(
                name = "Section.getCommonSectionInfoById",
                query = SECTION_DTO_COMMON_NATIVE_QUERY + SECTION_DTO_BY_ID,
                resultSetMapping = "Mapping.SectionDto"
        ),
        @NamedNativeQuery(
                name = "Section.getAllWithInfo",
                query = SECTION_DTO_COMMON_NATIVE_QUERY,
                resultSetMapping = "Mapping.SectionDto"
        ),
        @NamedNativeQuery(
                name = "Section.getIdListOfAllDescendantsById",
                query = SECTION_WITH_DEEP_CHILDREN
        )}
)
@SqlResultSetMapping(
        name = "Mapping.SectionDto",
        classes = @ConstructorResult(targetClass = SectionDto.class,
                columns = {@ColumnResult(name = "sect_id", type = Long.class),
                        @ColumnResult(name = "sect_name", type = String.class),
                        @ColumnResult(name = "sect_type", type = String.class),
                        @ColumnResult(name = "parent_id", type = Long.class),
                        @ColumnResult(name = "pigeon_number", type = Integer.class)}))

@Entity
@Table(name = "SECTION")
@Data
@NoArgsConstructor
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

    @OneToMany(mappedBy = "section")
    private Set<Pigeon> pigeons;

    public Section(long id) {
        this.setId(id);
    }
}
