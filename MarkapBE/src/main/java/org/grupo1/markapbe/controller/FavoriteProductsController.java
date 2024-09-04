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

    @PostMapping("/")
    public ResponseEntity<?> createProductoFavorito(@RequestBody FavoriteProductRequestDTO productoRequestDTO, Principal principal) {
        UserEntity userEntity = userRepository.findUserEntityByUsername(principal.getName()).orElseThrow(() -> new UsernameNotFoundException("El usuario no fue encontrado"));
        productosFavoritoService.createFavoriteProduct(productoRequestDTO,userEntity);
        return ResponseEntity.ok("El producto se ha likeado correctamente");
    }

    @GetMapping("/")
    public ResponseEntity<?> conseguirFavoritos(Principal principal) {
        UserEntity userEntity = userRepository.findUserEntityByUsername(principal.getName()).orElseThrow(() -> new UsernameNotFoundException("El usuario no fue encontrado"));
        List<ProductDTO> productos = productosFavoritoService.getLikes(userEntity);
        return new ResponseEntity<>(productos, HttpStatus.OK);
    }


    //borrar favorito
}