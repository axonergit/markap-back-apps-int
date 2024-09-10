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

    /**
     * Finds all carts associated with the given user ID.
     *
     * @param userId the ID of the user to find carts for
     * @return an `Optional` containing a list of `CarritoEntity` associated with the given user ID;
     *         otherwise, an empty `Optional` if no carts are found.
     */
    @Query("SELECT c FROM CarritoEntity AS c WHERE c.User.id = :userId")
    Optional<List<CarritoEntity>> findAllByUser(@Param("userId") Long userId);

    /**
     * Finds the active cart (not paid) associated with the given user ID.
     *
     * @param userId the ID of the user to find the active cart for
     * @return an `Optional` containing the active `CarritoEntity` for the given user ID,
     *          or an empty `Optional` if no active cart exists.
     */
    @Query("SELECT c FROM CarritoEntity AS c WHERE c.User.id = :userId AND c.paymentStatus = false")
    Optional<CarritoEntity> findActiveCarritoByUser(@Param("userId") Long userId);

    /**
     * Finds all carts that have been paid for the given user ID.
     *
     * @param userId the ID of the user to find paid carts for
     * @return an `Optional` containing a list of `CarritoEntity` that are in the paid state for the given user ID;
     *         otherwise, an empty `Optional` if no paid carts are found.
     */
    @Query("SELECT c FROM CarritoEntity AS c WHERE c.User.id = :userId AND c.paymentStatus = true")
    Optional<List<CarritoEntity>> findPaidCarritos(@Param("userId") Long userId);
}



