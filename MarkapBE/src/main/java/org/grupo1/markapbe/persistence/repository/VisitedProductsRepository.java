package org.grupo1.markapbe.persistence.repository;

import org.grupo1.markapbe.persistence.entity.*;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VisitedProductsRepository extends JpaRepository<VisitedProductsEntity, Long> {

    List<VisitedProductsEntity> findVisitedProductsEntityByUser(@Param("userEntity") UserEntity userEntity, Pageable pageable);

    Optional<VisitedProductsEntity> findVisitedProductsEntityByUserAndProduct(@Param("userEntity") UserEntity userEntity, ProductEntity productEntity);

    List<VisitedProductsEntity> findVisitedProductsEntityByUser(@Param("userEntity") UserEntity userEntity);
}
