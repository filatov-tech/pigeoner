package tech.filatov.pigeoner.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Persistable;

import javax.persistence.*;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
public abstract class AbstractBaseEntity implements Persistable<Long> {

    private static final int INITIAL_SEQUENCE_VALUE = 100000;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "global_seq")
    @SequenceGenerator(
            name = "global_seq",
            sequenceName = "global_seq",
            allocationSize = 1,
            initialValue = INITIAL_SEQUENCE_VALUE)
    Long id;

    Date created;

    Date updated;

    @Override
    public boolean isNew() {
        return getId() == null;
    }

}
