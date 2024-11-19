package org.grupo1.markapbe.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.grupo1.markapbe.controller.dto.FormularioDTO;
import org.grupo1.markapbe.service.FormularioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@PreAuthorize("permitAll()")
@RequestMapping("/formulario")
public class FormularioController {

    @Autowired
    private FormularioService formularioService;

    @Operation(summary = "Registrar un nuevo formulario",
                description = "Este endpoint permite crear un formulario de contacto para informar sobre alguna problematica del sitio web")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Contacto registrado correctamente"),
            @ApiResponse(responseCode = "404", description = "Solicitud incorrecta"),
    })
    @PostMapping
    public ResponseEntity<?> registrarFormulario(@Valid @RequestBody FormularioDTO formularioDTO) {

        Map<String, Object> resultado = new HashMap<>();
        resultado.put("message", "Se registro correctamente el formulario");
        resultado.put("formulario", formularioService.registrarFormulario(formularioDTO));
        return ResponseEntity.ok(resultado);

    }

    @Operation(summary = "Obtener los formularios paginados",
                description = "Este endpoint entrega los formularios de contacto paginados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pagina de formularios obtenida"),
    })
    @GetMapping("/paginado")
    public Page<FormularioDTO> obtenerTodosLosFormularios(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        return formularioService.obtenerTodosLosFormularios(page, size);

    }

}
