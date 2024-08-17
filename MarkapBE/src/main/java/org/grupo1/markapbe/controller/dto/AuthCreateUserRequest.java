package org.grupo1.markapbe.controller.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

//public record AuthCreateUserRequest(@NotBlank String username, String password, @Valid AuthCreateRoleRequest roleRequest) { }


public record AuthCreateUserRequest(@NotBlank(message = "El nombre de usuario no puede estar vacio.") String username,
                                    @Length(min = 3, message = "La contraseña debe tener minimo 3 caracteres.") String password,
                                    String fullName, @Email(message = "Debes ingresar un email valido.") String email) {

}
