package org.grupo1.markapbe.controller.dto.CatalogoDTO;

import org.grupo1.markapbe.persistence.entity.CategoryEntity;
import org.grupo1.markapbe.persistence.entity.UserEntity;

import java.math.BigDecimal;

public record ProductDTO(String imagen, String descripcion, BigDecimal precio, String detalles, int stock, CategoryEntity categoria) {

}
