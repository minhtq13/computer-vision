package com.elearning.elearning_support.dtos.role;

import java.util.Set;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleReqDTO {

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

    private Set<Long> permissionIds;

    private String displayedName;
}
