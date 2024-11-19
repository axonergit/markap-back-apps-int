package org.grupo1.markapbe.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.grupo1.markapbe.controller.dto.FormularioDTO;
import org.grupo1.markapbe.service.FormularioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/formulario")
@PreAuthorize("permitAll()")
public class FormularioController {

    @Autowired
    private FormularioService formularioService;

    @Operation(summary = "Registrar un nuevo formulario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Contacto registrado correctamente"),
            @ApiResponse(responseCode = "400", description = "Solicitud incorrecta"),
    })
    @PostMapping
    public ResponseEntity<FormularioDTO> registrarFormulario(@Valid @RequestBody FormularioDTO formularioDTO) {

        FormularioDTO resultado = formularioService.registrarFormulario(formularioDTO);
        return ResponseEntity.ok(resultado);

    }

}
