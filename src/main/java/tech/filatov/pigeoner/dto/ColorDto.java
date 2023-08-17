package tech.filatov.pigeoner.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@AllArgsConstructor
@Getter
public class ColorDto extends BaseDto {
    @NotNull(message = "У цвета должно быть название")
    @NotBlank(message = "Название цвета не может быть пустым")
    @Pattern(regexp = "^[a-zA-Zа-яА-Я -]+$", message = "Цвет может содержать только буквы, пробелы и знак дефиса \"-\"")
    private String name;

    public ColorDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
