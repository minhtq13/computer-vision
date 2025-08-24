package com.elearning.elearning_support.repositories.postgres.permission;

import com.elearning.elearning_support.entities.permission.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {

    Boolean existsByCode(String code);
}
