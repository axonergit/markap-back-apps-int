package org.grupo1.markapbe.controller.dto.UserProfileDTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

import java.time.LocalDate;

public record UserProfileUpdateDTO(@NotBlank(message = "El nombre no puede estar vacio") String name,
                                   @NotBlank(message = "El apellido no puede estar vacio") String lastName,
                                   @Past(message = "La fecha de nacimiento tiene que ser en el pasado") @NotNull LocalDate birthDate) {

}
