package com.elearning.elearning_support.enums.examClass;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum UserExamClassRoleEnum {


    ALL(-1, "Tất cả"),
    STUDENT(0, "Thí sinh/SV"),
    SUPERVISOR(1, "Giám thị"),
    LECTURER(2, "Giảng viên giảng dạy");

    private final Integer type;
    private final String name;

}
