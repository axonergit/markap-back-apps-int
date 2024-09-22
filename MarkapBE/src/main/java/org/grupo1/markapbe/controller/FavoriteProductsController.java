package org.grupo1.markapbe.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.grupo1.markapbe.controller.dto.CatalogoDTO.ProductDTO;
import org.grupo1.markapbe.persistence.repository.UserRepository;
import org.grupo1.markapbe.service.FavoriteProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/productos/liked")
public class FavoriteProductsController {

    @Autowired
    private FavoriteProductService productosFavoritoService;

    @Autowired
    private UserRepository userRepository;

    @Operation(summary = "Añadir un producto a favoritos",
            description = "Permite al usuario añadir un producto a su lista de favoritos.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto añadido a favoritos correctamente."),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado.")
    })
    @PostMapping("/{productId}")
    public ResponseEntity<?> createProductoFavorito(@PathVariable Long productId) {
        productosFavoritoService.createFavoriteProduct(productId);
        return ResponseEntity.ok("El producto se ha likeado correctamente");
    }

    @Operation(summary = "Obtener productos favoritos",
            description = "Devuelve la lista de productos que el usuario ha marcado como favoritos.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de productos favoritos devuelta correctamente."),
            @ApiResponse(responseCode = "404", description = "No existen favoritos para el usuario.")
    })
    @GetMapping("/")
    public ResponseEntity<?> conseguirFavoritos(Principal principal) {
        List<ProductDTO> productos = productosFavoritoService.getLikes();
        return new ResponseEntity<>(productos, HttpStatus.OK);
    }

    @Operation(summary = "Eliminar producto de favoritos",
            description = "Elimina un producto de la lista de favoritos del usuario.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "El producto ha sido eliminado de la lista de favoritos."),
            @ApiResponse(responseCode = "404", description = "El producto no está en la lista de favoritos.")
    })
    @DeleteMapping("/{productId}")
    public ResponseEntity<?> eliminarFavorito(@PathVariable Long productId) {
        productosFavoritoService.eliminarProductoFavorito(productId);
        return ResponseEntity.ok("El producto ha sido eliminado de la lista de favoritos");
    }
}