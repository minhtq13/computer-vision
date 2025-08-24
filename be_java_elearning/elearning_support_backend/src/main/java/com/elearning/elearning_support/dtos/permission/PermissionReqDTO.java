package com.elearning.elearning_support.dtos.permission;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PermissionReqDTO {

    @NotNull
    @Size(max = 30)
    private String code;

    private Long parentId;

    private String description;

    @Size(max = 30)
    private String parentCode;

    @NotNull
    @Size(max = 255)
    private String name;
}
