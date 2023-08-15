package tech.filatov.pigeoner.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.Persistable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import tech.filatov.pigeoner.HasId;

import javax.persistence.*;
import java.util.Date;

@Data
@ToString(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class AbstractBaseEntity implements Persistable<Long>, HasId {

    private static final int INITIAL_SEQUENCE_VALUE = 100000;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "global_seq")
    @SequenceGenerator(
            name = "global_seq",
            sequenceName = "global_seq",
            allocationSize = 1,
            initialValue = INITIAL_SEQUENCE_VALUE)
    @ToString.Include
    Long id;

    @CreatedDate
    Date created;

    @LastModifiedDate
    Date updated;

    @Override
    public boolean isNew() {
        return getId() == null;
    }

}
