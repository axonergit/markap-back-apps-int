package org.grupo1.markapbe.persistence.repository;

import org.grupo1.markapbe.persistence.entity.ItemsCarritoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;
import java.util.Optional;

@Repository
public interface ItemsCarritoRepository extends JpaRepository<ItemsCarritoEntity, Long> {
    /**
     * Finds a specific item in a cart based on cart ID and product ID.
     *
     * @param carritoId the ID of the cart
     * @param productoId the ID of the product
     * @return an Optional containing the found ItemsCarritoEntity, or empty if not found
     */
    Optional<ItemsCarritoEntity> findByCarritoIdAndProductId(@Param("carrito_id") Long carritoId, @Param("product_id") Long productId);
}
