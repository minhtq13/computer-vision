package com.elearning.elearning_support.services.role;

import java.util.List;

import com.elearning.elearning_support.dtos.role.RoleResponse;
import com.elearning.elearning_support.dtos.users.AssignBaseRolesRequest;
import com.elearning.elearning_support.dtos.users.AssignCustomRolesRequest;
import com.elearning.elearning_support.dtos.role.RoleReqDTO;
import com.elearning.elearning_support.entities.role.Role;

public interface RoleService {

    Role createRole(RoleReqDTO roleReqDTO);

    Role updateRole(Long id, RoleReqDTO roleReqDTO);

    void deleteRole(Long id);

    Role getRole(Long id);

    List<RoleResponse> getAllRoles(String type);

    /**
     * Assign roles to users
     */
    void assignCustomRoles(AssignCustomRolesRequest reqDTO);

    void assignBaseRoles(AssignBaseRolesRequest reqDTO);

}
