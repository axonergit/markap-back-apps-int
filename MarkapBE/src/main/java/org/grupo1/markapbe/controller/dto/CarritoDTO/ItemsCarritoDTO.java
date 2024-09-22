package org.grupo1.markapbe.controller.dto.CarritoDTO;

import jakarta.validation.constraints.NotBlank;
import org.grupo1.markapbe.persistence.entity.ProductEntity;

public record ItemsCarritoDTO(
        @NotBlank(message = "Debe tener ID") Long id,
        ProductEntity product,
        int amount
){}


