package tech.filatov.pigeoner.model.pigeon;

import lombok.*;
import tech.filatov.pigeoner.model.AbstractOwnedEntity;

import javax.persistence.*;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Image {

    @org.hibernate.annotations.Parent
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Pigeon pigeon;

    private String fileName;

    private boolean isMainImage;
}
