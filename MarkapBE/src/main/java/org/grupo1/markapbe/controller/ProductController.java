package org.grupo1.markapbe.controller;

import org.grupo1.markapbe.controller.dto.ProductDTO;
import org.grupo1.markapbe.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/productos")
public class ProductController {

    @Autowired
    private ProductService productoService;

    @GetMapping
    public List<ProductDTO> getAllProductos() {
        return productoService.getAllProductos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductoById(@PathVariable Long id) {
        Optional<ProductDTO> producto = productoService.getProductoById(id);
        return producto.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
   // Solo admin puede crear productos -->  @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductDTO> createProducto(@RequestBody ProductDTO productoRequestDTO) {
        ProductDTO producto = productoService.createProducto(productoRequestDTO);
        return ResponseEntity.ok(producto);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')") // Solo admin puede actualizar productos
    public ResponseEntity<ProductDTO> updateProducto(@PathVariable Long id, @RequestBody ProductDTO productoRequestDTO) {
        Optional<ProductDTO> updatedProducto = productoService.updateProducto(id, productoRequestDTO);
        return updatedProducto.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')") // Solo admin puede eliminar productos
    public ResponseEntity<Void> deleteProducto(@PathVariable Long id) {
        if (productoService.deleteProducto(id)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
