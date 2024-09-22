package org.grupo1.markapbe.controller.dto;


import java.time.LocalDate;

public record UserDetailsResponse(String nombreCompleto, String email, LocalDate birthDate) {

}
