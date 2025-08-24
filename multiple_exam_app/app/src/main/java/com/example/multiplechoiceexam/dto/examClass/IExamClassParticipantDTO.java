package com.example.multiplechoiceexam.dto.examClass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class IExamClassParticipantDTO {
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("roleType")
    @Expose
    private Integer roleType;
    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("roleName")
    @Expose
    private String roleName;
    @SerializedName("examClassCode")
    @Expose
    private String examClassCode;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRoleType() {
        return roleType;
    }

    public void setRoleType(Integer roleType) {
        this.roleType = roleType;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getExamClassCode() {
        return examClassCode;
    }

    public void setExamClassCode(String examClassCode) {
        this.examClassCode = examClassCode;
    }

}
