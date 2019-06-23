package com.tasteland.app.thetasteland.repository;

import com.tasteland.app.thetasteland.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
    RoleEntity findRoleByName(String name);
}
