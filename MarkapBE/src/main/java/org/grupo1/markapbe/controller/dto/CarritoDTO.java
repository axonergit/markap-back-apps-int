package org.grupo1.markapbe.controller.dto;

import org.grupo1.markapbe.persistence.entity.UserEntity;

public record CarritoDTO(UserEntity User, boolean paymentStatus) {
}
