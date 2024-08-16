package org.grupo1.markapbe.controller.dto;


import com.fasterxml.jackson.annotation.JsonPropertyOrder;

public record UserDetailsResponse(String nombreCompleto, String email) {

}
