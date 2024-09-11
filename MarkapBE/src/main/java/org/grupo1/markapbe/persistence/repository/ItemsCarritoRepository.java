package org.grupo1.markapbe.persistence.repository;

import org.grupo1.markapbe.persistence.entity.ItemsCarritoEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ItemsCarritoRepository extends JpaRepository<ItemsCarritoEntity, Long> {
    @Query("SELECT ic FROM ItemsCarritoEntity AS ic WHERE ic.carrito.id = :carritoId AND ic.product.id = :productId")
    Optional<ItemsCarritoEntity> findByCarritoIdAndProductId(@Param("carritoId") Long carritoId, @Param("productId") Long productId);

    @Query("SELECT ic FROM ItemsCarritoEntity AS ic WHERE ic.carrito.id = :carritoId")
    Optional<Page<ItemsCarritoEntity>> findAllItemsByCarritoId(@Param("carritoId") Long carritoId, Pageable pageable);

    boolean existsByCarritoId(@Param("carritoId") Long carritoId);


}
