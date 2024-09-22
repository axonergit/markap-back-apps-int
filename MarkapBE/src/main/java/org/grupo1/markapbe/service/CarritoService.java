package org.grupo1.markapbe.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.grupo1.markapbe.controller.dto.CarritoDTO.CarritoDTO;
import org.grupo1.markapbe.controller.dto.CarritoDTO.ItemsCarritoDTO;
import org.grupo1.markapbe.persistence.entity.CarritoEntity;
import org.grupo1.markapbe.persistence.entity.ItemsCarritoEntity;
import org.grupo1.markapbe.persistence.entity.ProductEntity;
import org.grupo1.markapbe.persistence.entity.UserEntity;
import org.grupo1.markapbe.persistence.repository.CarritoRepository;
import org.grupo1.markapbe.persistence.repository.ItemsCarritoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CarritoService {

    @Autowired
    private CarritoRepository carritoRepository;

    @Autowired
    private ItemsCarritoRepository itemsCarritoRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    //Funciones Publicas: Retornan DTO o boolean;

    public CarritoDTO getCarritoDTO(Long carritoId) {
        return convertToDTO(getCarrito(carritoId));
    }

    public CarritoDTO getActiveCarritoDTO() {
        UserEntity user = userService.obtenerUsuarioPeticion();
        CarritoEntity carrito = carritoRepository.findActiveCarritoByUser(user.getId())
                .orElseThrow(() -> new EntityNotFoundException("Carrito no encontrado."));
        return convertToDTO(carrito);
    }

    public List<CarritoDTO> getAllPaidCarritos() {
        UserEntity user = userService.obtenerUsuarioPeticion();
        Optional<List<CarritoEntity>> allCarritosOpt = carritoRepository.findPaidCarritos(user.getId());
        if (allCarritosOpt.isPresent()) {
            List<CarritoEntity> allCarritos = allCarritosOpt.get();
            return allCarritos.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    public Page<ItemsCarritoDTO> getAllItemsByCarritoDTO(CarritoDTO carritoDTO, int pagina, int size) {
        Optional<Page<ItemsCarritoEntity>> itemsCarritoEntity = getAllItemsByCarrito(carritoDTO.id(), pagina, size);
        return itemsCarritoEntity.map(itemsCarritoEntities -> itemsCarritoEntities
                .map(this::convertToDTO)).orElseGet(Page::empty);
    }

    public boolean deleteCarrito(CarritoDTO carritoDTO) {
        CarritoEntity carrito = getCarrito(carritoDTO.id());
        if (itemsCarritoRepository.existsById(carrito.getId()))
            itemsCarritoRepository.deleteAllByCarritoId(carrito.getId());
        carritoRepository.delete(carrito);
        return true;
    }

    public boolean addItemToCarrito(Long productId, int amount) {
        ProductEntity product = productService.getEntityById(productId);
        if (product.getStock() < amount)
            throw new IllegalArgumentException("No hay Stock Disponible.");
        CarritoEntity carrito = getActiveCarrito();
        Optional<ItemsCarritoEntity> itemCarrito = itemsCarritoRepository.findByCarritoIdAndProductId(
                carrito.getId(), productId);
        if (itemCarrito.isEmpty()) {
            ItemsCarritoEntity newItemCarrito = ItemsCarritoEntity.builder()
                    .carrito(carrito)
                    .product(product)
                    .amount(amount)
                    .build();
            itemsCarritoRepository.save(newItemCarrito);
        } else {
            ItemsCarritoEntity itemsCarritoEntity = itemCarrito.get();
            int sumaItems = itemsCarritoEntity.getAmount() + amount;
            if (sumaItems > product.getStock()) {
                throw new IllegalArgumentException("No hay Stock Disponible para el Total Requerido");
            }
            itemsCarritoEntity.setAmount(itemsCarritoEntity.getAmount() + amount);
            itemsCarritoRepository.save(itemsCarritoEntity);
        }
        return true;
    }

    public boolean removeItemFromCarrito(Long productId, int amount) {
        CarritoEntity carrito = getActiveCarrito();
        ProductEntity product = productService.getEntityById(productId);
        ItemsCarritoEntity itemCarrito = itemsCarritoRepository
                .findByCarritoIdAndProductId(carrito.getId(), product.getId())
                .orElseThrow(() -> new EntityNotFoundException("Producto en el Carrito no encontrado."));
        if (itemCarrito.getAmount() < amount) {
            throw new IllegalArgumentException("No existe tal cantidad de Cantidad en el Carrito para eliminar.");
        }
        if (itemCarrito.getAmount() == amount) {
            itemsCarritoRepository.delete(itemCarrito);
        } else {
            itemCarrito.setAmount(itemCarrito.getAmount() - amount);
            itemsCarritoRepository.save(itemCarrito);
        }
        return true;
    }

    public boolean changeStatusCarritoToPaid() {
        CarritoEntity carrito = getActiveCarrito();
        carrito.setPaymentStatus(true);
        carritoRepository.save(carrito);
        return true;
    }

    public boolean existItemsIntoCarrito(Long carritoId){
        return itemsCarritoRepository.existsByCarritoId(carritoId);
    }

    //Funciones Privadas: Trabaja con la Entidades, Manteniendo el Encapsulamiento.

    private CarritoEntity createCarrito() {
        UserEntity user = userService.obtenerUsuarioPeticion();
        Optional<CarritoEntity> actualCarrito = carritoRepository.findActiveCarritoByUser(user.getId());

        if (actualCarrito.isPresent())
            return actualCarrito.get();

        CarritoEntity newCarrito = CarritoEntity.builder()
                .User(user)
                .build();
        return carritoRepository.save(newCarrito);
    }

    private CarritoEntity getActiveCarrito() {
        UserEntity user = userService.obtenerUsuarioPeticion();
        return carritoRepository.findActiveCarritoByUser(user.getId())
                .orElseGet(this::createCarrito);
    }

    private CarritoEntity getCarrito(Long carritoId) {
        return carritoRepository.findById(carritoId)
                .orElseThrow(() -> new EntityNotFoundException("Carrito no encontrado."));
    }

    private Optional<Page<ItemsCarritoEntity>> getAllItemsByCarrito(Long carritoId, int pagina, int size) {
        Pageable pageable = PageRequest.of(pagina, size);
        return itemsCarritoRepository.findAllByCarritoId(carritoId, pageable);
    }

    private CarritoDTO convertToDTO(CarritoEntity carritoEntity) {
        return objectMapper.convertValue(carritoEntity, CarritoDTO.class);
    }

    private ItemsCarritoDTO convertToDTO(ItemsCarritoEntity itemsCarritoEntity) {
        return objectMapper.convertValue(itemsCarritoEntity, ItemsCarritoDTO.class);
    }
}