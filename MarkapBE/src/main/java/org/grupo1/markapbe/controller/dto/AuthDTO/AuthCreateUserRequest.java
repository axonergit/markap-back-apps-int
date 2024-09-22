package org.grupo1.markapbe.controller.dto.AuthDTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;

public record AuthCreateUserRequest(@NotBlank(message = "El nombre de usuario no puede estar vacio.") String username,
                                    @NotBlank(message = "La contraseña no puede estar vacía.") @Length(min = 3, message = "La contraseña debe tener minimo 3 caracteres.") String password,
                                    @NotBlank(message = "El nombre no puede estar vacio") String name,
                                    @NotBlank(message = "El apellido no puede estar vacio") String lastName,
                                    @Email(message = "Debes ingresar un email valido.") @NotBlank(message = "El email no puede estar vacío.") String email,
                                    @Past(message = "La fecha de nacimiento tiene que ser en el pasado") @NotNull LocalDate birthDate) {
}


