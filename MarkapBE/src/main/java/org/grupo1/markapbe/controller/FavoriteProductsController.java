package org.grupo1.markapbe.controller;


import org.grupo1.markapbe.controller.dto.FavoriteProductRequestDTO;
import org.grupo1.markapbe.controller.dto.ProductDTO;
import org.grupo1.markapbe.persistence.entity.UserEntity;
import org.grupo1.markapbe.persistence.repository.UserRepository;
import org.grupo1.markapbe.service.FavoriteProductService;
import org.grupo1.markapbe.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/productos/liked")
public class FavoriteProductsController {

    @Autowired
    private FavoriteProductService productosFavoritoService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/{productId}")
    public ResponseEntity<?> createProductoFavorito(@PathVariable Long productId) {
        productosFavoritoService.createFavoriteProduct(productId);
        return ResponseEntity.ok("El producto se ha likeado correctamente");
    }

    @GetMapping("/")
    public ResponseEntity<?> conseguirFavoritos(Principal principal) {
        List<ProductDTO> productos = productosFavoritoService.getLikes();
        return new ResponseEntity<>(productos, HttpStatus.OK);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<?> eliminarFavorito(@PathVariable Long productId) {
        productosFavoritoService.eliminarProductoFavorito(productId);
        return ResponseEntity.ok("El producto ha sido eliminado de la lista de favoritos");
    }
}