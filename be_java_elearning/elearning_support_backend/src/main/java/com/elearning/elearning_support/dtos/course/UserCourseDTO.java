package com.elearning.elearning_support.dtos.course;

import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.NotNull;
import com.elearning.elearning_support.enums.course.UserCourseRoleTypeEnum;
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
public class UserCourseDTO {

    @Schema(description = "Id lớp thi")
    @NotNull
    Long courseId;

    @Schema(description = "Danh sách Id user và vai trò trong lớp thi")
    List<UserCourseRoleDTO> lstParticipant = new ArrayList<>();

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class UserCourseRoleDTO {
        @Schema(description = "Id user")
        @NotNull
        Long userId;

        @Schema(description = "Vai trò trong lớp thi (Sinh viên/ Giảng viên)")
        @NotNull
        UserCourseRoleTypeEnum role;
    }

}
