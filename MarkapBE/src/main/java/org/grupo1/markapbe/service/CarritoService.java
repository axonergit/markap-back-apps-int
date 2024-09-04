package org.grupo1.markapbe.service;

import org.grupo1.markapbe.persistence.entity.CarritoEntity;
import org.grupo1.markapbe.persistence.entity.ItemsCarritoEntity;
import org.grupo1.markapbe.persistence.repository.CarritoRepository;
import org.grupo1.markapbe.persistence.repository.ItemsCarritoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CarritoService {

    @Autowired
    private CarritoRepository carritoRepository;

    @Autowired
    private ItemsCarritoRepository itemsCarritoRepository;

    public CarritoEntity getCarritoById(Long id) {
        Optional<CarritoEntity> carritoEntity = carritoRepository.findById(id);
        return carritoEntity.orElse(null);
    }

    /**
     * I
     *
     */
    public ItemsCarritoEntity addItemToCart(Long carritoId, Long productoId, int cantidad) {
        // Verificar si el item ya existe en el carrito
        Optional<ItemsCarritoEntity> existingItem = itemsCarritoRepository.findByCarritoIdAndProductId(carritoId, productoId);
        if (existingItem.isPresent()) {
            //Existe, solo se modifica Cantidad.
            ItemsCarritoEntity item = existingItem.get();
            item.setAmount(item.getAmount() + 1);
            return itemsCarritoRepository.save(item);
        } else {
            //Si no existe lo crea
            CarritoEntity carrito = this.getCarritoById(carritoId);
            //Falta agregar PorductoEntity

            ItemsCarritoEntity newItem = ItemsCarritoEntity.builder()
                    .carrito(carrito)
                    //Falta agregar Producto
                    .amount(1)
                    .build();

            return itemsCarritoRepository.save(newItem);
        }

    }

    public ItemsCarritoEntity removeItemToCart(Long carritoId, Long productoId) {
        Optional<ItemsCarritoEntity> existingItemOpt = itemsCarritoRepository.findByCarritoIdAndProductId(carritoId, productoId);

        if (existingItemOpt.isPresent()) {
            ItemsCarritoEntity existingItem = existingItemOpt.get();
            existingItem.setAmount(existingItem.getAmount() - 1);
            return itemsCarritoRepository.save(existingItem);
        }
        return null;
    }


}
