package com.elearning.elearning_support.enums.course;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum UserCourseRoleTypeEnum {
    STUDENT(0, "Học Sinh/Sinh Viên"),
    TEACHER(1, "Giáo Viên/Giảng Viên");

    private final Integer type;
    private final String name;

}
