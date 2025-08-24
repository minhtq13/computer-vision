package com.elearning.elearning_support.dtos.users.importUser;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ValidatedImportUserDTO {

    Boolean missedRequiredField = false;

    Boolean hasInvalidFormatField = false;

    Boolean hasDuplicatedField = false;

    Boolean hasNotFoundField = false;

}
