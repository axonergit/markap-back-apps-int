package org.grupo1.markapbe.persistence.repository;

import org.grupo1.markapbe.persistence.entity.RoleEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RoleRepository extends CrudRepository<RoleEntity,Long> {
    List<RoleEntity> findRoleEntitiesByRoleEnumIn(List<String> roleNames);

}
