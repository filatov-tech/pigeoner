package tech.filatov.pigeoner.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class KeeperDto extends BaseDto {
    @NotNull(message = "У владельца должно быть имя")
    @NotBlank(message = "Имя владельца не может быть пустым")
    @Pattern(regexp = "^[a-zA-Zа-яА-Я\\d. -]+$", message = "Имя может содержать только буквы, цифры, точку и дефис")
    private String name;

    public KeeperDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
