package com.elearning.elearning_support.controllers.restful.permission;

import com.elearning.elearning_support.dtos.permission.PermissionReqDTO;
import com.elearning.elearning_support.entities.permission.Permission;
import com.elearning.elearning_support.services.permission.PermissionService;

import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/permissions")
@RequiredArgsConstructor
public class PermissionController {

    private final PermissionService permissionService;

    @PreAuthorize("@dynamicAccessAspect.hasAnyRole('ADMIN')")
    @PostMapping
    public Permission createPermission(@Valid @RequestBody PermissionReqDTO permissionReqDTO) {
        return permissionService.createPermission(permissionReqDTO);
    }

    @PreAuthorize("@dynamicAccessAspect.hasAnyRole('ADMIN')")
    @PutMapping("/{id}")
    public Permission updatePermission(@PathVariable Long id,
            @Valid @RequestBody PermissionReqDTO permissionReqDTO) {
        return permissionService.updatePermission(id, permissionReqDTO);
    }

    @PreAuthorize("@dynamicAccessAspect.hasAnyRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void deletePermission(@PathVariable Long id) {
        permissionService.deletePermission(id);
    }

    @PreAuthorize("@dynamicAccessAspect.hasAnyRole('ADMIN')")
    @GetMapping("/{id}")
    public Permission getPermission(@PathVariable Long id) {
        return permissionService.getPermission(id);
    }

    @PreAuthorize("@dynamicAccessAspect.hasAnyRole('ADMIN')")
    @GetMapping
    public List<Permission> getAllPermissions() {
        return permissionService.getAllPermissions();
    }

    @PreAuthorize("@dynamicAccessAspect.hasAnyRole('ADMIN')")
    @GetMapping("/grouped")
    public List<Permission> getGroupedPermissions() {
        return permissionService.getGroupedPermissions();
    }
}