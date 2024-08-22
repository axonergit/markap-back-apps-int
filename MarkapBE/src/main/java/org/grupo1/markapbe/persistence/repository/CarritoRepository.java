package org.grupo1.markapbe.persistence.repository;

import jakarta.transaction.Transactional;
import org.grupo1.markapbe.persistence.entity.CarritoEntity;
import org.grupo1.markapbe.persistence.entity.ItemsCarritoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
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
    Optional<List<CarritoEntity>> findByUser(@Param("user_id") Long userId);

    /**
     * Finds the active cart (not paid) associated with the given user ID.
     *
     * @param userId the ID of the user to find the active cart for
     * @return an `Optional` containing the active `CarritoEntity` for the given user ID, or an empty `Optional` if no active cart exists.
     */
    Optional<CarritoEntity> findActiveCartByUser(@Param("user_id") Long userId);

    /**
     * Finds all carts that have been paid for the given user ID.
     *
     * @param userId the ID of the user to find paid carts for
     * @return an `Optional` containing a list of `CarritoEntity` that are in the paid state for the given user ID;
     *         otherwise, an empty `Optional` if no paid carts are found.
     */
    Optional<List<CarritoEntity>> findPaidCarts(@Param("user_id") Long userId);

    /**
     * Finds all carts that contain a specific product, regardless of the user.
     *
     * @param productId the ID of the product to search for in carts
     * @return an `Optional` containing a list of `CarritoEntity` that contain the specified product ID;
     *         otherwise, an empty `Optional` if no carts are found with the specified product.
     */
    Optional<List<CarritoEntity>> findCartsByProduct(@Param("product_id") Long productId);

    /**
     * Finds the list of items associated with the specified cart.
     *
     * This method returns an `Optional` that:
     * - Contains a list of `ItemsCarritoEntity` if there are items associated with the given cart ID.
     * - Is empty if no items are associated with the given cart ID.
     *
     * @param carritoId the ID of the CarritoEntity to find items for
     * @return an `Optional` containing a list of `ItemsCarritoEntity` if items exist;
     *         otherwise, an empty `Optional`.
     */
    Optional<List<ItemsCarritoEntity>> findItemsByCart(@Param("carrito_id") Long carritoId);

    /**
     * Updates the payment status of the specified cart to true (paid).
     *
     * This method will return:
     * - `true` if the payment status was successfully updated from `false` to `true`.
     * - `false` if the payment status was already `true`, indicating no change was made.
     *
     * @param carritoId the ID of the CarritoEntity to update
     * @return `true` if the payment status was updated from `false` to `true`;
     *         `false` if the payment status was already `true`.
     */
    @Transactional
    boolean updateCartPaymentStatus(@Param("carrito_id") Long carritoId);

    /**
     * Note: No se definio aun si la responsabiliad de sacar un producto es de
     * ItemsCarritoRepository o de CarritoRepository;
     */
}



