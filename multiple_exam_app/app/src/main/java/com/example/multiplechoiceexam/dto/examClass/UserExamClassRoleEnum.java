package com.example.multiplechoiceexam.dto.examClass;

public enum UserExamClassRoleEnum {
    STUDENT(0, "Thí sinh/SV"),
    SUPERVISOR(1, "Giám thị");

    private Integer type;
    private String name;

    UserExamClassRoleEnum(Integer type, String name) {
        this.type = type;
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
