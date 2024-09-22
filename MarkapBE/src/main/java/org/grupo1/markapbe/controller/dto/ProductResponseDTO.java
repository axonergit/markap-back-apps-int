package org.grupo1.markapbe.controller.dto;


import java.math.BigDecimal;
public record ProductResponseDTO(Long id,String imagen, String descripcion, BigDecimal precio, String detalles, int stock, String nombreCategoria, String nombreUserVendedor) {
}
