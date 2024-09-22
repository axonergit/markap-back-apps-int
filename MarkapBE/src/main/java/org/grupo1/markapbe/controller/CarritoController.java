package org.grupo1.markapbe.controller;

import org.grupo1.markapbe.controller.dto.CarritoDTO;
import org.grupo1.markapbe.controller.dto.ItemsCarritoDTO;
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

    @GetMapping("/actual")
    public ResponseEntity<?> getCarritoActivo() {
        CarritoDTO carritoDTO = carritoService.getActiveCarritoDTO();
        return ResponseEntity.ok(carritoDTO);
    }

    @GetMapping("/actual/items")
    public ResponseEntity<?> getAllItemsFromCarritoActivo(
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "5") int size) {
        CarritoDTO carritoDTO = carritoService.getActiveCarritoDTO();
        Page<ItemsCarritoDTO> itemsCarrito = carritoService.getAllItemsByCarritoDTO(carritoDTO, pagina, size);
        return ResponseEntity.ok(itemsCarrito);
    }

    @PutMapping("/actual/paid")
    public ResponseEntity<?> updateCarritoStatus() {
        Map<String, Object> response = new HashMap<>();
        Long carritoId = carritoService.getActiveCarritoDTO().id();
        if (carritoService.changeStatusCarritoToPaid()) {
            response.put("message", "Estado de Carrito Actualizado");
            response.put("carrito", carritoService.getCarritoDTO(carritoId));
        } else {
            response.put("message", "Error al Actualizar");
            response.put("carrito", carritoService.getCarritoDTO(carritoId));
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/historial")
    public ResponseEntity<?> getAllsCarritoHistorial() {
        List<CarritoDTO> carrito = carritoService.getAllPaidCarritos();
        return ResponseEntity.ok(carrito);
    }

    @GetMapping("/historial/{carritoId}")
    public ResponseEntity<?> getCarritoItems(@PathVariable Long carritoId,
                                             @RequestParam(defaultValue = "0") int pagina,
                                             @RequestParam(defaultValue = "5") int size) {
        CarritoDTO carritoDTO = carritoService.getCarritoDTO(carritoId);
        Page<ItemsCarritoDTO> itemsCarrito = carritoService.getAllItemsByCarritoDTO(carritoDTO, pagina, size);
        return ResponseEntity.ok(itemsCarrito);
    }

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
