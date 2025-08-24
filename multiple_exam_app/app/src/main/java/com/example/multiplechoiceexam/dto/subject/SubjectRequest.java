package com.example.multiplechoiceexam.dto.subject;

import androidx.annotation.Nullable;


public class SubjectRequest {
    @Nullable
    private String title;

    @Nullable
    private String code;

    @Nullable
    private String description;

    @Nullable
    private Integer credit;

    private Long departmentId;

    public SubjectRequest(){
    }

    @Nullable
    public String getTitle() {
        return title;
    }

    public void setTitle(@Nullable String title) {
        this.title = title;
    }

    @Nullable
    public String getCode() {
        return code;
    }

    public void setCode(@Nullable String code) {
        this.code = code;
    }

    @Nullable
    public String getDescription() {
        return description;
    }

    public void setDescription(@Nullable String description) {
        this.description = description;
    }

    @Nullable
    public Integer getCredit() {
        return credit;
    }

    public void setCredit(@Nullable Integer credit) {
        this.credit = credit;
    }


    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }
}
