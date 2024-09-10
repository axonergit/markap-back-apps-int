package org.grupo1.markapbe.controller.dto;

import org.grupo1.markapbe.persistence.entity.CarritoEntity;
import org.grupo1.markapbe.persistence.entity.ProductEntity;

public record ItemsCarritoDTO(
        CarritoEntity carrito,
        ProductEntity product,
        int amount
){}


