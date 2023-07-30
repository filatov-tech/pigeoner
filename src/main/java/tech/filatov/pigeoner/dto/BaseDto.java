package tech.filatov.pigeoner.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tech.filatov.pigeoner.HasId;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public abstract class BaseDto implements HasId {
    protected Long id;
}
