package tech.filatov.pigeoner.model.flight;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PreciseDistanceId implements Serializable {

    private Long keeperId;
    private Long launchPointId;
}
