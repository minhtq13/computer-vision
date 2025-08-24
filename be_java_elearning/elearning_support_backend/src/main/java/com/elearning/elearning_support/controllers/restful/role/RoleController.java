package com.elearning.elearning_support.controllers.restful.role;

import java.util.List;

import javax.validation.Valid;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.elearning.elearning_support.dtos.role.RoleResponse;
import com.elearning.elearning_support.dtos.users.AssignBaseRolesRequest;
import com.elearning.elearning_support.dtos.users.AssignCustomRolesRequest;
import com.elearning.elearning_support.dtos.role.RoleReqDTO;
import com.elearning.elearning_support.entities.role.Role;
import com.elearning.elearning_support.services.role.RoleService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @PreAuthorize("@dynamicAccessAspect.hasAnyRole('ADMIN')")
    @PostMapping
    public Role createRole(@Valid @RequestBody RoleReqDTO roleReqDTO) {
        return roleService.createRole(roleReqDTO);
    }

    @PreAuthorize("@dynamicAccessAspect.hasAnyRole('ADMIN')")
    @PutMapping("/{id}")
    public Role updateRole(@PathVariable Long id, @Valid @RequestBody RoleReqDTO roleReqDTO) {
        return roleService.updateRole(id, roleReqDTO);
    }

    @PreAuthorize("@dynamicAccessAspect.hasAnyRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void deleteRole(@PathVariable Long id) {
        roleService.deleteRole(id);
    }

    @PreAuthorize("@dynamicAccessAspect.hasAnyRole('ADMIN')")
    @GetMapping("/{id}")
    public Role getRole(@PathVariable Long id) {
        return roleService.getRole(id);
    }

    @PreAuthorize("@dynamicAccessAspect.hasAnyRole('ADMIN')")
    @GetMapping
    public List<RoleResponse> getAllRoles(@RequestParam(name = "type", defaultValue = "ALL") String type) {
        return roleService.getAllRoles(type);
    }

    @PreAuthorize("@dynamicAccessAspect.hasAnyRole('ADMIN')")
    @PostMapping("/assign-custom-role")
    @Operation(summary = "Gán vai trò cho danh sách người dùng")
    public void assignCustomRoles(@RequestBody @Validated AssignCustomRolesRequest reqDTO) {
        roleService.assignCustomRoles(reqDTO);
    }

    @PreAuthorize("@dynamicAccessAspect.hasAnyRole('ADMIN')")
    @PostMapping("/assign-base-role")
    @Operation(summary = "Gán vai trò cho danh sách người dùng")
    public void assignBaseRoles(@RequestBody @Validated AssignBaseRolesRequest reqDTO) {
        roleService.assignBaseRoles(reqDTO);
    }

}