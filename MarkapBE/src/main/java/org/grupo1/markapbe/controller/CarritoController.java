package org.grupo1.markapbe.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.grupo1.markapbe.controller.dto.CarritoDTO.CarritoDTO;
import org.grupo1.markapbe.controller.dto.CarritoDTO.ItemsCarritoDTO;
import org.grupo1.markapbe.controller.dto.ErrorResponseDTO;
import org.grupo1.markapbe.service.CarritoService;
import org.grupo1.markapbe.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/carrito")
public class CarritoController {

    @Autowired
    private CarritoService carritoService;

    @Autowired
    private ProductService productService;

    @Operation(
            summary = "Obtener carrito activo",
            description = "Este endpoint devuelve el carrito activo del usuario autenticado. Si no existe un carrito activo, lanza una excepción."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Carrito activo encontrado",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = CarritoDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Carrito no encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "No autorizado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @GetMapping("/actual")
    public ResponseEntity<?> getCarritoActivo() {
        CarritoDTO carritoDTO = carritoService.getActiveCarritoDTO();
        return ResponseEntity.ok(carritoDTO);
    }

    @Operation(summary = "Obtener todos los items del carrito activo",
            description = "Este endpoint retorna los items del carrito de compras activo del usuario logueado, paginados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Items obtenidos con éxito",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ItemsCarritoDTO.class))),
            @ApiResponse(responseCode = "404", description = "Carrito no encontrado",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content)
    })
    @GetMapping("/actual/items")
    public ResponseEntity<?> getAllItemsFromCarritoActivo(
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "5") int size) {
        CarritoDTO carritoDTO = carritoService.getActiveCarritoDTO();
        Page<ItemsCarritoDTO> itemsCarrito = carritoService.getAllItemsByCarritoDTO(carritoDTO, pagina, size);
        return ResponseEntity.ok(itemsCarrito);
    }

    @Operation(summary = "Actualizar el estado del carrito a 'pagado'",
            description = "Este endpoint permite actualizar el estado del carrito activo del usuario a 'pagado'.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Estado de carrito actualizado correctamente",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CarritoDTO.class))}),
            @ApiResponse(responseCode = "500", description = "Error al actualizar el estado del carrito",
                    content = @Content)
    })
    @PutMapping("/actual/paid")
    public ResponseEntity<?> updateCarritoStatus() {
        Map<String, Object> response = new HashMap<>();
        Long carritoId = carritoService.getActiveCarritoDTO().id();
        try {
            if (carritoService.changeStatusCarritoToPaid()) {
                response.put("message", "Estado de Carrito Actualizado");
                response.put("carrito", carritoService.getCarritoDTO(carritoId));
            }
        }
        catch (Exception e) {
            response.put("message", "Error al Actualizar");
            response.put("carrito", carritoService.getCarritoDTO(carritoId));
            carritoService.updateExistingStockItems();
        }
        finally {
            return ResponseEntity.ok(response);
        }
    }

    @Operation(summary = "Obtener historial de carritos pagados",
            description = "Este endpoint permite obtener una lista de carritos que han sido pagados por el usuario.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de carritos pagados devuelta correctamente",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CarritoDTO.class))}),
            @ApiResponse(responseCode = "404", description = "No se encontraron carritos pagados para el usuario",
                    content = @Content)
    })
    @GetMapping("/historial")
    public ResponseEntity<?> getAllsCarritoHistorial() {
        List<CarritoDTO> carrito = carritoService.getAllPaidCarritos();
        return ResponseEntity.ok(carrito);
    }

    @Operation(summary = "Obtener items de un carrito específico",
            description = "Este endpoint permite obtener todos los items de un carrito dado su ID, paginando los resultados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Items del carrito devueltos correctamente",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ItemsCarritoDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Carrito no encontrado",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Parámetros de paginación no válidos",
                    content = @Content)
    })
    @GetMapping("/historial/{carritoId}")
    public ResponseEntity<?> getCarritoItems(@PathVariable Long carritoId,
                                             @RequestParam(defaultValue = "0") int pagina,
                                             @RequestParam(defaultValue = "5") int size) {
        CarritoDTO carritoDTO = carritoService.getCarritoDTO(carritoId);
        Page<ItemsCarritoDTO> itemsCarrito = carritoService.getAllItemsByCarritoDTO(carritoDTO, pagina, size);
        return ResponseEntity.ok(itemsCarrito);
    }

    @Operation(summary = "Añadir un producto al carrito",
            description = "Añade una cantidad especificada de un producto al carrito activo del usuario.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto añadido al carrito exitosamente."),
            @ApiResponse(responseCode = "400", description = "Error de entrada, como stock insuficiente o producto no encontrado."),
            @ApiResponse(responseCode = "404", description = "Carrito no encontrado.")
    })
    @PutMapping("/add/{productId}")
    public ResponseEntity<?> addItem(@PathVariable Long productId,
                                     @RequestParam(defaultValue = "1") int amount) {
        Map<String, Object> response = new HashMap<>();
        if (carritoService.addItemToCarrito(productId, amount))
            response.put("message", "Se añadio "+amount+" productos del Carrito.");
        CarritoDTO carritoDTO = carritoService.getActiveCarritoDTO();
        response.put("carrito", carritoDTO);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Eliminar un producto del carrito",
            description = "Elimina una cantidad especificada de un producto del carrito activo del usuario. Si el carrito queda vacío, se elimina el carrito.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto eliminado del carrito exitosamente."),
            @ApiResponse(responseCode = "400", description = "Error de entrada, como cantidad insuficiente para eliminar o producto no encontrado."),
            @ApiResponse(responseCode = "404", description = "Carrito no encontrado.")
    })
    @PutMapping("/remove/{productId}")
    public ResponseEntity<?> removeItem(@PathVariable Long productId,
                                        @RequestParam(defaultValue = "1") int amount) {
        Map<String, Object> response = new HashMap<>();
        if (carritoService.removeItemFromCarrito(productId, amount))
            response.put("message", "Se quito "+amount+" productos del Carrito.");
        CarritoDTO carritoDTO = carritoService.getActiveCarritoDTO();
        if (!carritoService.existItemsIntoCarrito(carritoDTO.id())
                && carritoService.deleteCarrito(carritoDTO)) //Esta vacio;
            response.put("carrito", "null");
        else
            response.put("carrito", carritoDTO);
        return ResponseEntity.ok(response);
    }
}
