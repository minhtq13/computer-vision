package com.elearning.elearning_support.enums.users;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum UserTypeEnum {

    ALL(-2, "Tất cả"),
    ADMIN(-1, "Admin"),
    TEACHER(0, "Giáo viên / Giảng viên"),
    STUDENT(1, "Học sinh / Sinh viên");

    private final Integer type;
    private final String name;
}
