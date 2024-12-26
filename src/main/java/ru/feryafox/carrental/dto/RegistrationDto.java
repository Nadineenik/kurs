package ru.feryafox.carrental.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegistrationDto {

    @NotEmpty(message = "Имя не может быть пустым")
    private String firstName;

    @NotEmpty(message = "Email не может быть пустым")
    @Email(message = "Некорректный email")
    private String email;

    @NotEmpty(message = "Телефон не может быть пустым")
    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Некорректный номер телефона")
    private String phone;

    @NotEmpty(message = "Пароль не может быть пустым")
    @Size(min = 8, message = "Пароль должен содержать минимум 8 символов")
    private String password;

}
