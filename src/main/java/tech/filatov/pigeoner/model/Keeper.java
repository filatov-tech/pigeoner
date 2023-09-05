package tech.filatov.pigeoner.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Keeper extends AbstractOwnedEntity {

    @Column(unique = true)
    private String name;

    public Keeper(long id) {
        this.setId(id);
    }
}
