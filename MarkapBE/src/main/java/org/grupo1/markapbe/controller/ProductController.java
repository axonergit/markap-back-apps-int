package org.grupo1.markapbe.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.grupo1.markapbe.controller.dto.CatalogoDTO.ProductDTO;
import org.grupo1.markapbe.controller.dto.CatalogoDTO.ProductRequestUpdateDTO;
import org.grupo1.markapbe.controller.dto.CatalogoDTO.ProductResponseDTO;
import org.grupo1.markapbe.persistence.entity.ProductEntity;
import org.grupo1.markapbe.persistence.entity.UserEntity;
import org.grupo1.markapbe.persistence.repository.ProductRepository;
import org.grupo1.markapbe.persistence.repository.UserRepository;
import org.grupo1.markapbe.service.ProductService;
import org.grupo1.markapbe.service.UserService;
import org.grupo1.markapbe.service.VisitedProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;


import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/productos")
public class ProductController {

    @Autowired
    private ProductService productoService;

    @Autowired
    private ProductRepository productoRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VisitedProductService visitedProductService;

    @Operation(summary = "Obtener todos los productos",
            description = "Este endpoint devuelve una lista de todos los productos disponibles.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de productos obtenida con éxito."),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor.")
    })
    @GetMapping
    public List<ProductResponseDTO> getAllProductos() {
        return productoService.getAllProductos();
    }

    @Operation(summary = "Obtener un producto por ID",
            description = "Este endpoint devuelve los detalles de un producto específico dado su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto encontrado y devuelto con éxito."),
            @ApiResponse(responseCode = "404", description = "No se encontró el producto con el ID especificado."),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor.")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> getProductoById(@PathVariable Long id) {
        Optional<ProductResponseDTO> producto = productoService.getProductoById(id);
        return producto.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Obtener productos destacados",
            description = "Este endpoint devuelve una lista de productos que están marcados como destacados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de productos destacados devuelta con éxito."),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor.")
    })
    @GetMapping("/destacados")
    public List<ProductResponseDTO> getFeaturedProducts() {return productoService.getFeaturedproducts();}


    @Operation(summary = "Obtener productos por categoría",
            description = "Este endpoint devuelve una lista de productos que pertenecen a una categoría específica.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de productos devuelta con éxito."),
            @ApiResponse(responseCode = "404", description = "No se encontraron productos para la categoría proporcionada."),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor.")
    })
    @GetMapping("/categoria/{id}")
    public ResponseEntity<Page<ProductResponseDTO>> getProductoByIdCategoria(
            @PathVariable Long id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<ProductResponseDTO> productos = productoService.getProductosByIdCategoria(id, pageable);

        if (productos.isEmpty()) {
            return ResponseEntity.notFound().build(); // Devuelve 404 si no hay productos
        } else {
            return ResponseEntity.ok(productos); // Devuelve 200 con la página de productos
        }
    }


    @Operation(summary = "Crear un nuevo producto",
            description = "Este endpoint permite a un usuario con rol ADMIN crear un nuevo producto.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto creado exitosamente."),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida. Verifique los datos ingresados."),
            @ApiResponse(responseCode = "401", description = "No autorizado. El usuario no tiene el rol adecuado."),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor.")
    })
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductResponseDTO> createProducto(@RequestBody ProductDTO productoRequestDTO, Principal principal) {
        ProductResponseDTO producto = productoService.createProducto(productoRequestDTO);
        return ResponseEntity.ok(producto);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')") // Solo admin puede actualizar productos
    public ResponseEntity<?> updateProducto(@PathVariable Long id, @RequestBody ProductRequestUpdateDTO productoRequestUpdateDTO) {
        ProductResponseDTO updatedProducto = productoService.updateProducto(id, productoRequestUpdateDTO);
        return new ResponseEntity<>(updatedProducto, HttpStatus.OK);

    }


    //revisar
    @Operation(summary = "Eliminar un producto",
            description = "Este endpoint permite a un usuario con rol ADMIN eliminar un producto existente mediante su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto eliminado exitosamente."),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado con el ID especificado."),
            @ApiResponse(responseCode = "401", description = "No autorizado. El usuario no tiene el rol adecuado.")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')") // Solo admin puede eliminar productos
    public ResponseEntity<String> deleteProducto(@PathVariable Long id) {
        if (productoService.deleteProducto(id)) {
            return ResponseEntity.ok("Producto eliminado exitosamente");
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Error, este producto no te pertenece");
        }
    }


    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> destacarProducto(@PathVariable Long id) {
        boolean valor = productoService.featureProduct(id);

        if (valor) {
            return ResponseEntity.ok("Producto destacado exitosamente");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Producto no encontrado");
   }

}

}
