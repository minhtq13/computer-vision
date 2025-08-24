package com.example.multiplechoiceexam.dto.auth;

public class ProfileUserDTO {
    Long id;

    String name;

    Long avatarId;

    String avatarPath;

    String code;

    String identificationNum;

    String identityType;

    String address;

    String birthDate;

    String phoneNumber;

    String email;

    String userName;

    String role;

    String department;

    Long departmentId;

    String createdAt;

    String modifiedAt;

    public ProfileUserDTO(IGetDetailUserDTO iGetDetailUserDTO) {
        this.id = iGetDetailUserDTO.getId();
        this.name = iGetDetailUserDTO.getName();
        this.avatarId = iGetDetailUserDTO.getAvatarId();
        this.avatarPath = iGetDetailUserDTO.getAvatarPath();
        this.code = iGetDetailUserDTO.getCode();
        this.identificationNum = iGetDetailUserDTO.getIdentificationNum();
        this.identityType = iGetDetailUserDTO.getIdentityType();
        this.address = iGetDetailUserDTO.getAddress();
        this.birthDate = iGetDetailUserDTO.getBirthDate();
        this.phoneNumber = iGetDetailUserDTO.getPhoneNumber();
        this.email = iGetDetailUserDTO.getEmail();
        this.userName = iGetDetailUserDTO.getUserName();
        this.role = iGetDetailUserDTO.getRoleJson();
        this.department = iGetDetailUserDTO.getDepartment();
        this.departmentId = iGetDetailUserDTO.getDepartmentId();
        this.createdAt = iGetDetailUserDTO.getCreatedAt();
        this.modifiedAt = iGetDetailUserDTO.getModifiedAt();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getAvatarId() {
        return avatarId;
    }

    public void setAvatarId(Long avatarId) {
        this.avatarId = avatarId;
    }

    public String getAvatarPath() {
        return avatarPath;
    }

    public void setAvatarPath(String avatarPath) {
        this.avatarPath = avatarPath;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getIdentificationNum() {
        return identificationNum;
    }

    public void setIdentificationNum(String identificationNum) {
        this.identificationNum = identificationNum;
    }

    public String getIdentityType() {
        return identityType;
    }

    public void setIdentityType(String identityType) {
        this.identityType = identityType;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(String modifiedAt) {
        this.modifiedAt = modifiedAt;
    }
}
