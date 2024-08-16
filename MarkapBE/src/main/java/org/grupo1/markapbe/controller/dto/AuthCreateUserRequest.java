package org.grupo1.markapbe.controller.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

//public record AuthCreateUserRequest(@NotBlank String username, String password, @Valid AuthCreateRoleRequest roleRequest) { }


public record AuthCreateUserRequest(@NotBlank String username, String password) {

}
