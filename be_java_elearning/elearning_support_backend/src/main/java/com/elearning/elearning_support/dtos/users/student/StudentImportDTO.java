package com.elearning.elearning_support.dtos.users.student;

import com.elearning.elearning_support.dtos.users.importUser.CommonUserImportDTO;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StudentImportDTO extends CommonUserImportDTO {

    String courseRaw;
}
