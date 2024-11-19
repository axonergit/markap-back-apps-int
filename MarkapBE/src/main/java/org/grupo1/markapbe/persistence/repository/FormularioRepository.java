package org.grupo1.markapbe.persistence.repository;

import org.grupo1.markapbe.persistence.entity.FormularioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FormularioRepository extends JpaRepository<FormularioEntity, Long> {
}
