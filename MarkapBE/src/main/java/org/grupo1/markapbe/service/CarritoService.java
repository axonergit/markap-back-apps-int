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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
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
    private ProductRepository productRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Creates a new cart for the current user.
     *
     * @return CarritoEntity The entity representation of the newly created cart.
     */
    public CarritoEntity createCarrito() {
        UserEntity user = userService.obtenerUsuarioPeticion();
        Optional<CarritoEntity> actualCarrito = carritoRepository.findActiveCarritoByUser(user.getId());

        if (actualCarrito.isPresent())
            return actualCarrito.get();

        CarritoEntity newCarrito = CarritoEntity.builder()
                .User(user)
                .build();
        return carritoRepository.save(newCarrito);
    }

    public CarritoDTO createCarritoDTO() {
        CarritoEntity carrito = createCarrito();
        return convertToDTO(carrito);
    }

    public CarritoEntity getActiveCarrito() {
        UserEntity user = userService.obtenerUsuarioPeticion();
        return carritoRepository.findActiveCarritoByUser(user.getId())
                .orElseGet(this::createCarrito);
    }

    public CarritoDTO getActiveCarritoDTO() {
        return convertToDTO(getActiveCarrito());
    }


    public boolean deleteActiveCarrito() {
        try{
            CarritoEntity carrito = getActiveCarrito();
            carritoRepository.delete(carrito);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Page<ItemsCarritoEntity> getAllItemsByCarrito(Long carritoId, int pagina, int size) {
        Pageable pageable = PageRequest.of(pagina, size);
        return itemsCarritoRepository.findAllItemsByCarritoId(carritoId, pageable)
                .orElseThrow(() -> new EntityNotFoundException("Item carrito no encontrado."));
    }

    public Optional<Page<ItemsCarritoDTO>> getAllItemsByCarritoDTO(CarritoDTO carritoDTO, int pagina, int size) {
        Page<ItemsCarritoEntity> itemsEntity = getAllItemsByCarrito(carritoDTO.id(), pagina, size);
        Page<ItemsCarritoDTO> itemsDTO = itemsEntity.map(this::convertToDTO);
        return Optional.of(itemsDTO);
    }

    public CarritoEntity getCarrito(Long carritoId) {
        return carritoRepository.findById(carritoId)
                .orElseThrow(() -> new EntityNotFoundException("Carrito no encontrado."));
    }

    public Optional<CarritoDTO> getCarritoDTO(Long carritoId) {
        return Optional.of(convertToDTO(getCarrito(carritoId)));
    }

    public Optional<List<CarritoDTO>> getAllPaidCarritos() {
        UserEntity user = userService.obtenerUsuarioPeticion();
        Optional<List<CarritoEntity>> allCarritosOpt = carritoRepository.findPaidCarritos(user.getId());
        if (allCarritosOpt.isPresent()) {
            List<CarritoEntity> allCarritos = allCarritosOpt.get();
            return Optional.of(allCarritos.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList()));
        }
        return Optional.empty();
    }

    public boolean existItemsIntoCarrito(Long carritoId){
        return itemsCarritoRepository.existsByCarritoId(carritoId);
    }

    // ACCIONES

    public CarritoDTO changeStatusCarritoToPaid() {
        CarritoEntity carrito = getActiveCarrito();
        carrito.setPaymentStatus(true);
        return convertToDTO(carritoRepository.save(carrito));
    }

    public ItemsCarritoDTO addItemToCarrito(Long productId, int amount) {
        CarritoEntity carrito = getActiveCarrito();
        Optional<ItemsCarritoEntity> itemCarrito = itemsCarritoRepository.findByCarritoIdAndProductId(
                carrito.getId(), productId);

        if (itemCarrito.isEmpty()) {
            ProductEntity product = productRepository.findById(productId)
                    .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado."));

            ItemsCarritoEntity newItemCarrito = ItemsCarritoEntity.builder()
                    .carrito(carrito)
                    .product(product)
                    .amount(amount)
                    .build();
            return convertToDTO(itemsCarritoRepository.save(newItemCarrito));
        }

        ItemsCarritoEntity itemCarritoEnt = itemCarrito.get();
        itemCarritoEnt.setAmount(itemCarritoEnt.getAmount() + amount);
        return convertToDTO(itemsCarritoRepository.save(itemCarritoEnt));
    }

    public ItemsCarritoDTO removeItemFromCarrito(Long productId, int amount) {
        CarritoEntity carrito = getActiveCarrito();
        ItemsCarritoEntity itemCarrito = itemsCarritoRepository.findByCarritoIdAndProductId(
                        carrito.getId(), productId)
                .orElseThrow(() -> new EntityNotFoundException("Item del Carrito no encontrado."));

        if (itemCarrito.getAmount() < amount) {
            throw new IllegalArgumentException("La cantidad a eliminar del Producto no puede ser mayor a lo ya existente en el Carrito");
        }
        if (itemCarrito.getAmount() == amount) {
            itemsCarritoRepository.delete(itemCarrito);
            return null;
        }

        itemCarrito.setAmount(itemCarrito.getAmount() - amount);
        return convertToDTO(itemsCarritoRepository.save(itemCarrito));
    }

    private CarritoDTO convertToDTO(CarritoEntity carritoEntity) {
        return objectMapper.convertValue(carritoEntity, CarritoDTO.class);
    }

    private ItemsCarritoDTO convertToDTO(ItemsCarritoEntity itemsCarritoEntity) {
        return objectMapper.convertValue(itemsCarritoEntity, ItemsCarritoDTO.class);
    }
}