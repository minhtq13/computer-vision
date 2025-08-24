package com.elearning.elearning_support.dtos.role;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.springframework.beans.BeanUtils;

import com.elearning.elearning_support.entities.permission.Permission;
import com.elearning.elearning_support.utils.object.ObjectMapperUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class RoleResponse {

    @NotBlank
    private Long id;

    @NotBlank
    @Size(max = 30)
    private String code;

    @NotBlank
    @Size(max = 255)
    private String name;

    @Size(max = 255)
    private String description;

    private Boolean isDefault = false;

    private Long roleBaseId;

    private Long userCount;

    private List<Permission> permissions;

    private String displayedName;

    @JsonIgnore
    private String permissionInfo;

    public RoleResponse(IRoleDetailDTO iRoleDetailDTO) {
        this.permissions = ObjectMapperUtils.listMapper(iRoleDetailDTO.getPermissionInfo(), Permission.class);
        BeanUtils.copyProperties(iRoleDetailDTO, this);
    }
}
