package com.elearning.elearning_support.dtos.common;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BasePermissionDTO {

    Boolean hasViewPermission;

    Boolean hasCreatePermission;

    Boolean hasEditPermission;

    Boolean hasDeletePermission;

}
