package org.grupo1.markapbe.persistence.repository;

import org.grupo1.markapbe.persistence.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteProductsRepository extends JpaRepository<FavoriteProductsEntity, Long> {

    Optional<List<FavoriteProductsEntity>> findFavoriteProductsEntitiesByUser(UserEntity user);

}
