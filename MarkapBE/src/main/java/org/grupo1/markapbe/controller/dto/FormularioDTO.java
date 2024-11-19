package org.grupo1.markapbe.controller.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import java.util.List;

public record FormularioDTO (

    Long id,

    @NotEmpty(message = "Nombre y Apellido es obligatorio")
    @Size(max = 100, message = "Maximo 100 caracteres")
    String nombreCompleto,

    @NotEmpty(message = "La problematica es obligatoria")
    @Size(max = 250, message = "Maximo 100 caracteres")
    String problematica,

    @Size(max = 5, message = "Maximo 5 fotos")
    List<String> fotoBase64,

    @NotEmpty(message = "La descripcion es obligatoria")
    @Size(max = 5000, message = "Maximo 5000 caracteres")
    String descripcion

) {}
