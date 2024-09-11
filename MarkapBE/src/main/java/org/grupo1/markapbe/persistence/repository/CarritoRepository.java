package org.grupo1.markapbe.persistence.repository;

import jakarta.transaction.Transactional;
import org.grupo1.markapbe.persistence.entity.CarritoEntity;
import org.grupo1.markapbe.persistence.entity.ItemsCarritoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CarritoRepository extends JpaRepository<CarritoEntity, Long> {
    @Query("SELECT c FROM CarritoEntity AS c WHERE c.User.id = :userId")
    Optional<List<CarritoEntity>> findAllByUser(@Param("userId") Long userId);

    @Query("SELECT c FROM CarritoEntity AS c WHERE c.User.id = :userId AND c.paymentStatus = false")
    Optional<CarritoEntity> findActiveCarritoByUser(@Param("userId") Long userId);

    @Query("SELECT c FROM CarritoEntity AS c WHERE c.User.id = :userId AND c.paymentStatus = true")
    Optional<List<CarritoEntity>> findPaidCarritos(@Param("userId") Long userId);
}



