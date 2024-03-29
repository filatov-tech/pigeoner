package tech.filatov.pigeoner.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Getter
@Setter
public class LaunchPointDto extends BaseDto {

    @NotNull(message = "У точки вылета должны быть название и дистанция")
    @NotEmpty(message = "У точки вылета должны быть название и дистанция")
    private String name;

    @NotNull(message = "Укажите дистанцию в км")
    @Range(min = 1, max = 2000, message = "Дистанция должна быть в диапозоне от 1 км до 2000 км")
    private Integer distance;

    private Double mainKeeperPreciseDistance;

    public LaunchPointDto(Long id, String name, Integer distance) {
        this.id = id;
        this.name = name;
        this.distance = distance;
    }

    public LaunchPointDto(String name, Integer distance) {
        this.name = name;
        this.distance = distance;
    }

}
