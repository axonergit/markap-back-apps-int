package org.grupo1.markapbe.service;

import org.grupo1.markapbe.persistence.entity.CarritoEntity;
import org.grupo1.markapbe.persistence.entity.ItemsCarritoEntity;
import org.grupo1.markapbe.persistence.entity.UserEntity;
import org.grupo1.markapbe.persistence.repository.CarritoRepository;
import org.grupo1.markapbe.persistence.repository.ItemsCarritoRepository;
import org.grupo1.markapbe.persistence.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class CarritoService {

    @Autowired
    private CarritoRepository carritoRepository;

    @Autowired
    private ItemsCarritoRepository itemsCarritoRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Creates a new cart for the user. Throws an exception if an active cart already exists for the user.
     *
     * @param userId The ID of the user for whom the cart is being created.
     */
    public void createCarrito(Long userId) {
        //Busca que no exista ya un carrito de usuario con paymentStatus = false
        CarritoEntity actualCarrito = carritoRepository.findActiveCarritoByUser(userId)
                .orElse(null);

        if(actualCarrito != null) {
            //Existe por ende no se puede crear uno nuevo.
            throw new IllegalStateException("Ya existe un Carrito activo para este Usuario.");
        }

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("No existe Usuario con ese Id."));

        CarritoEntity newCarrito = CarritoEntity.builder()
                .User(user)
                .build();

        carritoRepository.save(newCarrito);
    }

    /**
     * Changes the status of a cart to 'paid' (paymentStatus = true).
     *
     * @param carritoId The ID of the cart to be updated to paid status.
     */
    public void changeStatusCarritoToPaid(Long carritoId) {
        CarritoEntity carrito = carritoRepository.findById(carritoId)
                .orElseThrow(() -> new RuntimeException("No existe Carrito con ese Id."));
        carrito.setPaymentStatus(true);
        carritoRepository.save(carrito);
    }

    /**
     * Adds an item to the cart. If the item exists, it increments the amount.
     * If the item does not exist, it creates a new item in the cart.
     *
     * @param carritoId The ID of the cart where the item is being added.
     * @param productoId The ID of the product to add to the cart.
     */
    public void addItemToCarrito(Long carritoId, Long productoId) {
        ItemsCarritoEntity itemCarrito = itemsCarritoRepository.findByCarritoIdAndProductId(carritoId,productoId)
                .orElse(null);
        if (itemCarrito == null) {
            // Si el ítem no existe, crear un nuevo ítem
            CarritoEntity carrito = carritoRepository.findById(carritoId)
                    .orElseThrow(() -> new RuntimeException("No existe Carrito con ese Id."));

            ItemsCarritoEntity newItemCarrito = ItemsCarritoEntity.builder()
                    .carrito(carrito)
                    .build();
            itemsCarritoRepository.save(newItemCarrito);
        } else {
            // Si el ítem ya existe, aumentar la cantidad
            itemCarrito.setAmount(itemCarrito.getAmount() + 1);
            itemsCarritoRepository.save(itemCarrito);
        }
    }

    /**
     * Removes an item from the cart. If the item amount is 1, it removes the item entirely.
     * Otherwise, it decreases the amount by 1.
     *
     * @param carritoId The ID of the cart where the item is being removed.
     * @param productoId The ID of the product to remove from the cart.
     */
    public void removeItemFromCarrito(Long carritoId, Long productoId) {
        ItemsCarritoEntity itemCarrito = itemsCarritoRepository.findByCarritoIdAndProductId(carritoId,productoId)
                .orElse(null);
        if (itemCarrito == null) {
            throw new IllegalStateException("No existe Item Carrito con ese Carrito Id y Producto Id");
        }

        if (itemCarrito.getAmount() == 1) {
            //Existe solo una unidad del item, Se elimina
            itemsCarritoRepository.delete(itemCarrito);
        } else {
            //Existe mas de una unidad del item, Se desincrementa
            itemCarrito.setAmount(itemCarrito.getAmount() - 1);
            itemsCarritoRepository.save(itemCarrito);
        }
    }
}
