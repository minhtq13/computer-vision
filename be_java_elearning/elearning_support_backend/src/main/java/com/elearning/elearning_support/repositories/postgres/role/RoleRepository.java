package com.elearning.elearning_support.repositories.postgres.role;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.elearning.elearning_support.constants.sql.SQLRole;
import com.elearning.elearning_support.dtos.role.IRoleDetailDTO;
import com.elearning.elearning_support.entities.role.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Boolean existsByCode(String code);

    @Query(nativeQuery = true, value = SQLRole.GET_ALL_ROLES)
    List<IRoleDetailDTO> findRolesByType(@Param("type") String type);

}
