package tech.filatov.pigeoner.model.pigeon;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import tech.filatov.pigeoner.model.AbstractOwnedEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "COLOR")
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class Color extends AbstractOwnedEntity {

    @Column(unique = true)
    private String name;
}
