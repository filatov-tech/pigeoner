package tech.filatov.pigeoner.model.pigeon;

import lombok.Data;
import lombok.EqualsAndHashCode;
import tech.filatov.pigeoner.model.AbstractOwnedEntity;

import javax.persistence.*;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class Image extends AbstractOwnedEntity {

    @EqualsAndHashCode.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PIGEON_ID")
    private Pigeon pigeon;

    private String name;

    @EqualsAndHashCode.Exclude
    @Lob
    private byte[] content;

}
