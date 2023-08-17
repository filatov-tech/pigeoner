package tech.filatov.pigeoner.model.pigeon;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import tech.filatov.pigeoner.model.AbstractOwnedEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "COLOR")
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class Color extends AbstractOwnedEntity {

    @NotNull
    @NotBlank
    @Column(unique = true)
    private String name;
}
