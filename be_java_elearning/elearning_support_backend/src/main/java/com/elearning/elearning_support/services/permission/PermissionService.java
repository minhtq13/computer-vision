package com.elearning.elearning_support.services.permission;

import com.elearning.elearning_support.dtos.permission.PermissionReqDTO;
import com.elearning.elearning_support.entities.permission.Permission;

import java.util.List;

public interface PermissionService {

    Permission createPermission(PermissionReqDTO permissionReqDTO);

    Permission updatePermission(Long id, PermissionReqDTO permissionReqDTO);

    void deletePermission(Long id);

    Permission getPermission(Long id);

    List<Permission> getAllPermissions();

    List<Permission> getGroupedPermissions();
}
