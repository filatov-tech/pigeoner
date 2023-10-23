package tech.filatov.pigeoner.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    @Email
    @NotNull(message = "Необходимо ввести адрес эл. почты")
    @NotBlank(message = "Адрес эл. почты не может быть пустым")
    private String email;

    @NotNull(message = "Необходимо ввести пароль")
    @NotBlank(message = "Пароль не может быть пустым")
    @Size(min = 6, max = 30, message = "От 6 до 30 символов")
    private String password;

    @NotNull(message = "У владельца должно быть имя")
    @NotBlank(message = "Имя владельца не может быть пустым")
    @Pattern(regexp = "^[a-zA-Zа-яА-Я\\d. -]+$", message = "Имя может содержать только буквы, цифры, точку и дефис")
    private String keeperName;
}
