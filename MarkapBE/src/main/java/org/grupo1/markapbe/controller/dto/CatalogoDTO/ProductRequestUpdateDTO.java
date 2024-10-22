package org.grupo1.markapbe.controller.dto.CatalogoDTO;

import org.grupo1.markapbe.persistence.entity.CategoryEntity;

import java.math.BigDecimal;

public record ProductRequestUpdateDTO(String imagen, String descripcion, BigDecimal precio, String detalles, int stock, long categoria){
}
