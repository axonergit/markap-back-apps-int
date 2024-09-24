package org.grupo1.markapbe.persistence.repository;

import org.grupo1.markapbe.persistence.entity.CarritoEntity;
import org.grupo1.markapbe.persistence.entity.ItemsCarritoEntity;
import org.grupo1.markapbe.persistence.entity.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemsCarritoRepository extends JpaRepository<ItemsCarritoEntity, Long> {
    @Query("SELECT ic FROM ItemsCarritoEntity AS ic WHERE ic.carrito.id = :carritoId AND ic.product.id = :productId")
    Optional<ItemsCarritoEntity> findByCarritoIdAndProductId(@Param("carritoId") Long carritoId, @Param("productId") Long productId);

    @Query("SELECT ic FROM ItemsCarritoEntity AS ic WHERE ic.carrito.id = :carritoId")
    Optional<Page<ItemsCarritoEntity>> findAllByCarritoId(@Param("carritoId") Long carritoId, Pageable pageable);

    @Modifying
    @Query("DELETE ItemsCarritoEntity AS ic WHERE ic.carrito.id = :carritoId")
    void deleteAllByCarritoId(@Param("carritoId") Long carritoId);

    boolean existsByCarritoId(@Param("carritoId") Long carritoId);

    List<ItemsCarritoEntity> getItemsCarritoEntitiesByCarrito(CarritoEntity carrito);


}
