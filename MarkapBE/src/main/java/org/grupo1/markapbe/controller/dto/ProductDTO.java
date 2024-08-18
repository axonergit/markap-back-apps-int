package org.grupo1.markapbe.controller.dto;

import java.math.BigDecimal;

public record ProductDTO(String imagen, String descripcion, BigDecimal precio, String detalles, int stock) {
}



//Mete esto despues para la categoria
// public record ProductRequestDTO (String imagen, String descripcion, BigDecimal precio, String detalles, int stock, Categoria categoria) { }

