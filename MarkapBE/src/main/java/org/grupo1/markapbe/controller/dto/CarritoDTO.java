package org.grupo1.markapbe.controller.dto;

import jakarta.validation.constraints.NotBlank;
import org.grupo1.markapbe.persistence.entity.ItemsCarritoEntity;

import java.util.Set;

public record CarritoDTO(
        Set<ItemsCarritoEntity> items,
        boolean paymentStatus) {}
