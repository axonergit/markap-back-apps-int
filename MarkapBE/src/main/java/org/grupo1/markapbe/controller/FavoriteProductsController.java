package org.grupo1.markapbe.controller;


import org.grupo1.markapbe.controller.dto.FavoriteProductRequestDTO;
import org.grupo1.markapbe.controller.dto.ProductDTO;
import org.grupo1.markapbe.service.FavoriteProductService;
import org.grupo1.markapbe.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/productos/Liked")
public class FavoriteProductsController {

    @Autowired
    private FavoriteProductService productosFavoritoService;

    @GetMapping("/{id}")
    public ResponseEntity<List<ProductDTO>> getFavoriteProductsByUserId(@PathVariable Long id) {
        List<ProductDTO> productos = productosFavoritoService.getFavoriteProductsByUserId(id);

        if (productos.isEmpty()) {
            return ResponseEntity.notFound().build(); // Devuelve 404 si no hay productos
        } else {
            return ResponseEntity.ok(productos); // Devuelve 200 con la lista de productos
        }

    }


    @PostMapping()
    public ResponseEntity<FavoriteProductRequestDTO> createProductoFavorito(@RequestBody FavoriteProductRequestDTO productoRequestDTO) {
        FavoriteProductRequestDTO producto = productosFavoritoService.createFavoriteProduct(productoRequestDTO);
        return ResponseEntity.ok(producto);
    }


    //borrar favorito
}
