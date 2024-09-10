package org.grupo1.markapbe.controller;

import org.grupo1.markapbe.controller.dto.CarritoDTO;
import org.grupo1.markapbe.controller.dto.ItemsCarritoDTO;
import org.grupo1.markapbe.service.CarritoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/carrito")
public class CarritoController {

    @Autowired
    private CarritoService carritoService;

    @GetMapping("/actual")
    public ResponseEntity<?> getCarritoActivo() {
        CarritoDTO carrito = carritoService.getActiveCarritoDTO()
                .orElseGet(() -> carritoService.createCarritoDTO());
        return ResponseEntity.ok(carrito);
    }

    @GetMapping("/actual/productos")
    public ResponseEntity<?> getAllCarritoItemsActivo() {
        CarritoDTO carrito = carritoService.getActiveCarritoDTO()
                .orElseGet(() -> carritoService.createCarritoDTO());
        Set<ItemsCarritoDTO> itemsCarrito = carritoService.getAllItemsByCarritoDTO(carrito)
                .orElse(new HashSet<>());
        return ResponseEntity.ok(itemsCarrito);
    }

    @GetMapping("/historial")
    public ResponseEntity<?> getAllsCarritoHistorial() {
        List<CarritoDTO> carrito = carritoService.getAllCarritos()
                .orElse(new ArrayList<>());
        return ResponseEntity.ok(carrito);
    }

    @GetMapping("/historial/items")
    public ResponseEntity<?> getCarritoItems(@RequestBody CarritoDTO carritoDTO) {
        Set<ItemsCarritoDTO> itemsCarrito = carritoService.getAllItemsByCarritoDTO(carritoDTO)
                .orElse(new HashSet<>());
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
        return ResponseEntity.ok(itemsCarrito);
    }
}
