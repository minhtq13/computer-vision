package com.elearning.elearning_support.dtos.users.teacher;

import com.elearning.elearning_support.dtos.users.importUser.CommonUserImportDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TeacherImportDTO extends CommonUserImportDTO {

    @Schema(description = "Mã học phần giảng dạy")
    private String subjectCode;

}
