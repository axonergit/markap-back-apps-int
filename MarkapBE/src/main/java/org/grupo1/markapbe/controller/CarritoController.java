package org.grupo1.markapbe.controller;

import org.grupo1.markapbe.controller.dto.CarritoDTO;
import org.grupo1.markapbe.controller.dto.ItemsCarritoDTO;
import org.grupo1.markapbe.service.CarritoService;
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

    @GetMapping("/actual")
    public ResponseEntity<?> getCarritoActivo() {
        CarritoDTO carritoDTO = carritoService.getActiveCarritoDTO();
        if (carritoService.existItemsIntoCarrito(carritoDTO.id()))
            return ResponseEntity.ok(carritoDTO);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/actual/productos")
    public ResponseEntity<?> getAllItemsFromCarritoActivo(
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "5") int size) {
        CarritoDTO carritoDTO = carritoService.getActiveCarritoDTO();
        if (carritoService.existItemsIntoCarrito(carritoDTO.id())){
            Page<ItemsCarritoDTO> itemsCarrito = carritoService.getAllItemsByCarritoDTO(carritoDTO, pagina, size)
                    .orElse(null);
            return ResponseEntity.ok(itemsCarrito);
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/historial")
    public ResponseEntity<?> getAllsCarritoHistorial() {
        Optional<List<CarritoDTO>> carrito = carritoService.getAllPaidCarritos();
        if (carrito.isPresent())
            return ResponseEntity.ok(carrito);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/historial/items")
    public ResponseEntity<?> getCarritoItems(@RequestBody CarritoDTO carritoDTO,
                                             @RequestParam(defaultValue = "0") int pagina,
                                             @RequestParam(defaultValue = "5") int size) {

        Page<ItemsCarritoDTO> itemsCarrito = carritoService.getAllItemsByCarritoDTO(carritoDTO, pagina, size)
                .orElse(null);
        return ResponseEntity.ok(itemsCarrito);
    }

    @PutMapping("/paid")
    public ResponseEntity<?> updateCarritoStatus() {
        CarritoDTO carrito = carritoService.changeStatusCarritoToPaid();
        carritoService.createCarritoDTO();
        return ResponseEntity.ok(carrito);
    }

    @PutMapping("/add/{productId}")
    public ResponseEntity<?> addItem(@PathVariable Long productId, @RequestParam(defaultValue = "1") int amount) {
        ItemsCarritoDTO itemsCarrito = carritoService.addItemToCarrito(productId, amount);
        return ResponseEntity.ok(itemsCarrito);
    }

    @PutMapping("/remove/{productId}")
    public ResponseEntity<?> removeItem(@PathVariable Long productId, @RequestParam(defaultValue = "1") int amount) {
        ItemsCarritoDTO itemsCarrito = carritoService.removeItemFromCarrito(productId, amount);
        CarritoDTO carritoDTO = carritoService.getActiveCarritoDTO();
        if (itemsCarrito == null && !carritoService.existItemsIntoCarrito(carritoDTO.id())) {
            carritoService.deleteActiveCarrito();
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(itemsCarrito);
    }
}
