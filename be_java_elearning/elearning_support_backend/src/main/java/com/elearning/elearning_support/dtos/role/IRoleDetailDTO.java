package com.elearning.elearning_support.dtos.role;

import com.fasterxml.jackson.annotation.JsonProperty;

public interface IRoleDetailDTO {

    Long getId();

    String getCode();

    String getName();

    String getDescription();

    @JsonProperty("isDefault")
    Boolean getIsDefault();

    Long getRoleBaseId();

    Long getUserCount();

    String getPermissionInfo();

    String getDisplayedName();
}
