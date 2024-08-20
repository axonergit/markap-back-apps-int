package org.grupo1.markapbe.persistence.repository;

import org.grupo1.markapbe.persistence.entity.ProductEntity;
import org.grupo1.markapbe.persistence.entity.UserEntity;
import org.grupo1.markapbe.persistence.entity.UserProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
/* no se que carajo hago aca
    Optional<ProductEntity> findProductEntityById(Long id);

    Optional<ProductEntity> findProductEntityByNombreContaining(String nombre);
*/
}