package org.grupo1.markapbe.controller.dto.AuthDTO;

import jakarta.validation.constraints.NotBlank;

public record AuthLoginRequest(@NotBlank(message = "El usuario no puede estar vacio.") String username,
                               @NotBlank(message = "La contraseña no puede estar vacia") String password) {
}