package org.grupo1.markapbe.controller.dto.CarritoDTO;

import jakarta.validation.constraints.NotBlank;
import org.grupo1.markapbe.persistence.entity.ItemsCarritoEntity;

import java.util.Set;

public record CarritoDTO(
        @NotBlank(message = "Debe tener ID.") Long id,
        boolean paymentStatus) {}
