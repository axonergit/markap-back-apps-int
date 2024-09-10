package org.grupo1.markapbe.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.grupo1.markapbe.controller.dto.CarritoDTO;
import org.grupo1.markapbe.controller.dto.ItemsCarritoDTO;
import org.grupo1.markapbe.persistence.entity.CarritoEntity;
import org.grupo1.markapbe.persistence.entity.ItemsCarritoEntity;
import org.grupo1.markapbe.persistence.entity.ProductEntity;
import org.grupo1.markapbe.persistence.entity.UserEntity;
import org.grupo1.markapbe.persistence.repository.CarritoRepository;
import org.grupo1.markapbe.persistence.repository.ItemsCarritoRepository;
import org.grupo1.markapbe.persistence.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


@Service
public class CarritoService {

    @Autowired
    private CarritoRepository carritoRepository;

    @Autowired
    private ItemsCarritoRepository itemsCarritoRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Creates a new cart for the current user.
     *
     * @return CarritoDTO The DTO representation of the newly created cart.
     */
    public CarritoDTO createCarrito() {
        // Chequea si existe un Carrito activo (paymentStatus = false)
        UserEntity user = userService.obtenerUsuarioPeticion();
        Optional<CarritoEntity> actualCarrito = carritoRepository.findActiveCarritoByUser(user.getId());

        if(actualCarrito.isPresent()) {
            // Si existe manda error (Solo debe existir un Carrito Activo por Usuario)
            throw new IllegalStateException("El usuario ya tiene un Carrito Activo");
        }

        // Crea nuevo carrito
        CarritoEntity newCarrito = CarritoEntity.builder()
                .User(user)
                .build();
        return convertToDTO(carritoRepository.save(newCarrito));
    }

    /**
     * Retrieves all items from a specific cart.
     *
     * @param carritoId The ID of the cart.
     * @param carritoDTO The DTO representation of the cart.
     * @return Set<ItemsCarritoDTO> A set of DTOs representing the items in the cart.
     */
    public Set<ItemsCarritoDTO> getAllItemsByCarritoId(Long carritoId, CarritoDTO carritoDTO) {
        Set<ItemsCarritoEntity> itemsCarrito = itemsCarritoRepository.findAllItemsByCarritoId(carritoId)
                .orElseThrow(() -> new EntityNotFoundException("Item carrito no encontrado."));
        return itemsCarrito.stream() // stream() Permite realizar operaciones en colecciones de Datos.
                .map(this::convertToDTO) // .map() Convertir cada ItemsCarritoEntity a ItemsCarritoDTO.
                .collect(Collectors.toSet()); // .collect() Recoge los resultados en el Set.
    }

    /**
     * Retrieves a specific item from the cart.
     *
     * @param carritoId The ID of the cart.
     * @param productId The ID of the product in the cart.
     * @return ItemsCarritoDTO The DTO representation of the item in the cart.
     */
    public ItemsCarritoDTO getItemFromCarrito(Long carritoId, Long productId) {
        ItemsCarritoEntity itemCarrito = itemsCarritoRepository.findByCarritoIdAndProductId(carritoId, productId)
                .orElseThrow(() -> new EntityNotFoundException("Item carrito no encontrado."));
        return convertToDTO(itemCarrito);
    }

    /**
     * Retrieves a cart entity by its ID.
     *
     * @param carritoId The ID of the cart.
     * @return CarritoEntity The entity representation of the cart.
     */
    public CarritoEntity getCarritoEntity(Long carritoId) {
        return carritoRepository.findById(carritoId)
                .orElseThrow(() -> new EntityNotFoundException("Carrito no encontrado."));
    }

    /**
     * Changes the status of a cart to 'paid' (paymentStatus = true).
     *
     * @param carritoId The ID of the cart to be updated.
     * @param carritoDTO The DTO representation of the cart to be updated.
     * @return CarritoDTO The updated DTO representation of the cart.
     */
    public CarritoDTO changeStatusCarritoToPaid(Long carritoId, CarritoDTO carritoDTO) {
        CarritoEntity carrito = carritoRepository.findById(carritoId)
                .orElseThrow(() -> new EntityNotFoundException("Carrito no encontrado."));
        carrito.setPaymentStatus(true);
        return convertToDTO(carritoRepository.save(carrito));
    }

    /**
     * Adds an item to the cart. If the item exists, increments the amount.
     * If the item does not exist, creates a new item in the cart.
     *
     * @param carritoId The ID of the cart where the item is being added.
     * @param productId The ID of the product to be added to the cart.
     * @return ItemsCarritoDTO The DTO representation of the item added or updated.
     */
    public ItemsCarritoDTO addItemToCarrito(Long carritoId, Long productId) {
        Optional<ItemsCarritoEntity> itemCarrito = itemsCarritoRepository.findByCarritoIdAndProductId(carritoId, productId);

        if (itemCarrito.isEmpty()) {
            // If the item does not exist, create a new item in the cart
            CarritoEntity carrito = carritoRepository.findById(carritoId)
                    .orElseThrow(() -> new EntityNotFoundException("Carrito no encontrado."));

            ProductEntity product = productRepository.findById(productId)
                    .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado."));

            ItemsCarritoEntity newItemCarrito = ItemsCarritoEntity.builder()
                    .carrito(carrito)
                    .product(product)
                    .build();
            return convertToDTO(itemsCarritoRepository.save(newItemCarrito));

        }
        // If the item exists, increment the amount
        ItemsCarritoEntity itemCarritoEnt = itemCarrito.get();
        itemCarritoEnt.setAmount(itemCarritoEnt.getAmount() + 1);
        return convertToDTO(itemsCarritoRepository.save(itemCarritoEnt));
    }

    /**
     * Removes an item from the cart. If the amount is 1, the item is deleted.
     * Otherwise, the amount is decremented by 1.
     *
     * @param carritoId The ID of the cart from which the item is being removed.
     * @param productId The ID of the product to be removed from the cart.
     * @return ItemsCarritoDTO The DTO representation of the updated item, or null if deleted.
     */
    public ItemsCarritoDTO removeItemFromCarrito(Long carritoId, Long productId) {
        ItemsCarritoEntity itemCarrito = itemsCarritoRepository.findByCarritoIdAndProductId(carritoId, productId)
                .orElseThrow(() -> new EntityNotFoundException("Item del Carrito no encontrado."));

        if (itemCarrito.getAmount() == 1) {
            // If there is only one unit of the item, delete it
            itemsCarritoRepository.delete(itemCarrito);
            return null;
        }
        // If there are more than one unit, decrease the amount by 1
        itemCarrito.setAmount(itemCarrito.getAmount() - 1);
        return convertToDTO(itemsCarritoRepository.save(itemCarrito));
    }

    /**
     * Converts CarritoEntity to CarritoDTO using ObjectMapper.
     *
     * @param carritoEntity The cart entity to be converted.
     * @return CarritoDTO The DTO representation of the cart.
     */
    private CarritoDTO convertToDTO(CarritoEntity carritoEntity) {
        return objectMapper.convertValue(carritoEntity, CarritoDTO.class);
    }

    /**
     * Converts ItemsCarritoEntity to ItemsCarritoDTO using ObjectMapper.
     *
     * @param itemsCarritoEntity The cart item entity to be converted.
     * @return ItemsCarritoDTO The DTO representation of the cart item.
     */
    private ItemsCarritoDTO convertToDTO(ItemsCarritoEntity itemsCarritoEntity) {
        return objectMapper.convertValue(itemsCarritoEntity, ItemsCarritoDTO.class);
    }

//    private CarritoEntity convertToEntity(CarritoDTO carritoDTO) {
//        return objectMapper.convertValue(carritoDTO, CarritoEntity.class);
//    }
//
//    private ItemsCarritoEntity convertToEntity(ItemsCarritoDTO itemsCarritoDTO) {
//        return objectMapper.convertValue(itemsCarritoDTO, ItemsCarritoEntity.class);
//    }

}

