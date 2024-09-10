package org.grupo1.markapbe.persistence.repository;

import org.grupo1.markapbe.persistence.entity.ItemsCarritoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface ItemsCarritoRepository extends JpaRepository<ItemsCarritoEntity, Long> {
    /**
     * Finds a specific item in a cart based on cart ID and product ID.
     *
     * @param carritoId the ID of the cart
     * @param productId the ID of the product
     * @return an Optional containing the found ItemsCarritoEntity, or empty if not found
     */
    @Query("SELECT ic FROM ItemsCarritoEntity AS ic WHERE ic.carrito.id = :carritoId AND ic.product.id = :productId")
    Optional<ItemsCarritoEntity> findByCarritoIdAndProductId(@Param("carritoId") Long carritoId, @Param("productId") Long productId);

    @Query("SELECT ic FROM ItemsCarritoEntity AS ic WHERE ic.carrito.id = :carritoId")
    Optional<Set<ItemsCarritoEntity>> findAllItemsByCarritoId(@Param("carritoId") Long carritoId);

}
