package com.elearning.elearning_support.dtos.examClass;

import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.NotNull;
import com.elearning.elearning_support.enums.examClass.UserExamClassRoleEnum;
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
public class UserExamClassDTO {

    @Schema(description = "Id lớp thi")
    @NotNull
    Long examClassId;

    @Schema(description = "Danh sách Id user và vai trò trong lớp thi")
    List<UserExamClassRoleDTO> lstParticipant = new ArrayList<>();

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class UserExamClassRoleDTO {
        @Schema(description = "Id user")
        @NotNull
        Long userId;

        @Schema(description = "Vai trò trong lớp thi (Thí sinh / Giám thị)")
        @NotNull
        UserExamClassRoleEnum role;
    }

}
