package org.grupo1.markapbe.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.grupo1.markapbe.controller.dto.VisitedProductDTO;
import org.grupo1.markapbe.persistence.entity.UserEntity;
import org.grupo1.markapbe.persistence.repository.UserRepository;
import org.grupo1.markapbe.service.UserService;
import org.grupo1.markapbe.service.VisitedProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/productos/visited")
public class VisitedProductsController {

    @Autowired
    private UserService userService;

    @Autowired
    private VisitedProductService visitedProductService;

    @Operation(summary = "Obtener productos visitados",
            description = "Este endpoint devuelve una lista de productos visitados por el usuario, paginada por página.",security = @SecurityRequirement(name = "BearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de productos visitados obtenida exitosamente."),
            @ApiResponse(responseCode = "404", description = "No se encontraron productos visitados para el usuario.")
    })
    @GetMapping("/{page}")
    public ResponseEntity<List<VisitedProductDTO>> getVisitedProducts(@PathVariable int page) {
        return new ResponseEntity<>(visitedProductService.getVisited(page), HttpStatus.OK);
    }

    @Operation(summary = "Obtener todos los productos visitados",
            description = "Este endpoint devuelve una lista de todos los productos visitados por el usuario.",security = @SecurityRequirement(name = "BearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de productos visitados obtenida exitosamente."),
            @ApiResponse(responseCode = "404", description = "No se encontraron productos visitados para el usuario.")
    })
    @GetMapping("/")
    public ResponseEntity<List<VisitedProductDTO>> getAllVisitedProducts() {
        return new ResponseEntity<>(visitedProductService.getVisitedAll(), HttpStatus.OK);
    }
}
