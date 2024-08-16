package org.grupo1.markapbe.persistence.repository;

import org.grupo1.markapbe.persistence.entity.RoleEntity;
import org.grupo1.markapbe.persistence.entity.RoleEnum;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface RoleRepository extends CrudRepository<RoleEntity,Long> {
    List<RoleEntity> findRoleEntitiesByRoleEnumIn(List<String> roleNames);

    Optional<RoleEntity> findRoleEntityByRoleEnum(RoleEnum roleEnum);

}
