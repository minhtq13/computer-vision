package com.elearning.elearning_support.dtos.users;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import com.elearning.elearning_support.dtos.common.ICommonIdCodeName;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ImportUserValidatorDTO {

    Set<String> lstExistedUsername = new HashSet<>();

    Set<String> lstExistedEmail = new HashSet<>();

    Set<String> lstExistedCode = new HashSet<>();

    Map<String, ICommonIdCodeName> mapDepartments = new HashMap<>();

    public ImportUserValidatorDTO(Set<String> lstExistedUsername, Set<String> lstExistedEmail, Set<String> lstExistedCode) {
        this.lstExistedUsername = lstExistedUsername;
        this.lstExistedEmail = lstExistedEmail;
        this.lstExistedCode = lstExistedCode;
    }
}
