package com.example.multiplechoiceexam.dto.auth;

public enum UserTypeEnum {

    ALL(-2, "Tất cả"),
    ADMIN(-1, "Admin"),
    TEACHER(0, "Giáo viên / Giảng viên"),
    STUDENT(1, "Học sinh / Sinh viên");

    private Integer type;
    private String name;

    UserTypeEnum(Integer type, String name) {
        this.type = type;
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public void setName(String name) {
        this.name = name;
    }
}
