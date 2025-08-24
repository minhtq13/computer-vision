package com.elearning.elearning_support.enums.subject;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TeacherSubjectRoleTypeEnum {
    LECTURER(0, "Giảng viên giảng dạy"),
    HEAD_OF_SUBJECT(1, "Trưởng nhóm môn học");

    private final Integer type;
    private final String name;
}
