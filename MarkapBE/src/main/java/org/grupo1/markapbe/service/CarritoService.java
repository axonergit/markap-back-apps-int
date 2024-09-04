package org.grupo1.markapbe.service;

import org.grupo1.markapbe.persistence.entity.CarritoEntity;
import org.grupo1.markapbe.persistence.entity.ItemsCarritoEntity;
import org.grupo1.markapbe.persistence.repository.CarritoRepository;
import org.grupo1.markapbe.persistence.repository.ItemsCarritoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
public class CarritoService {

    @Autowired
    private CarritoRepository carritoRepository;

    @Autowired
    private ItemsCarritoRepository itemsCarritoRepository;

    // Obtener un carrito por su ID
    public CarritoEntity getCarritoById(Long id) {
        // Buscar el carrito por ID, si no se encuentra, devolver null
        Optional<CarritoEntity> carritoEntity = carritoRepository.findById(id);
        return carritoEntity.orElse(null);
    }

    // Obtener todos los items del carrito por el ID del carrito
    public Set<ItemsCarritoEntity> getItemsCarritoByCarritoId(Long id) {
        // Buscar el carrito por ID y devolver el conjunto de items, si no se encuentra, devolver null
        Optional<CarritoEntity> carritoEntity = carritoRepository.findById(id);
        return carritoEntity.map(CarritoEntity::getItemsCarrito).orElse(null);
    }

    // Obtener un ítem del carrito por el ID del carrito y el ID del producto
    public ItemsCarritoEntity getItemCarritoByCarritoAndProduct(Long carritoId, Long productId) {
        // Buscar el ítem del carrito por el ID del carrito y producto, si no se encuentra, devolver null
        Optional<ItemsCarritoEntity> itemsCarrito = itemsCarritoRepository.findByCarritoIdAndProductId(carritoId, productId);
        return itemsCarrito.orElse(null);
    }

    /**
     * Add an item to the cart. If the item already exists, the quantity is incremented.
     * If it doesn't exist, a new item is created in the cart with quantity 1.
     *
     * @param carritoId The ID of the cart
     * @param productoId The ID of the product to add
     * @return The updated or newly created item
     */
    public ItemsCarritoEntity addItemToCart(Long carritoId, Long productoId) {
        ItemsCarritoEntity itemCarrito = this.getItemCarritoByCarritoAndProduct(carritoId, productoId);
        if (itemCarrito != null) {
            // Si el ítem ya existe, aumentar la cantidad
            itemCarrito.setAmount(itemCarrito.getAmount() + 1);
            return itemsCarritoRepository.save(itemCarrito);
        } else {
            // Si el ítem no existe, crear un nuevo ítem
            CarritoEntity carrito = this.getCarritoById(carritoId);
            //ProductoEntity producto = this.getProductoById(productoId); Asumimos que este metodo existe
            ItemsCarritoEntity newItemCarrito = ItemsCarritoEntity.builder()
                    .carrito(carrito)
                    //Falta Producto, agregar cuando esté implementado
                    .amount(1)
                    .build();
            return itemsCarritoRepository.save(newItemCarrito);
        }
    }

    /**
     * Remove an item from the cart. If the quantity is 1, the item is completely removed.
     * If the quantity is greater than 1, the quantity is decremented.
     *
     * @param carritoId The ID of the cart
     * @param productoId The ID of the product to remove
     * @return The updated item or null if it was removed
     */
    public ItemsCarritoEntity removeItemToCart(Long carritoId, Long productoId) {
        ItemsCarritoEntity itemCarrito = this.getItemCarritoByCarritoAndProduct(carritoId, productoId);

        // Verificar si la cantidad es 1
        if (itemCarrito.getAmount() == 1) {
            // Si la cantidad es 1, remover el ítem completo del carrito
            itemsCarritoRepository.delete(itemCarrito);
            return null;  // Devolver null si el ítem fue eliminado
        } else {
            // Si la cantidad es mayor que 1, disminuir la cantidad en 1
            itemCarrito.setAmount(itemCarrito.getAmount() - 1);
            return itemsCarritoRepository.save(itemCarrito);
        }
    }
}

