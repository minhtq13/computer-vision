package com.example.multiplechoiceexam.dto.auth;

public interface IGetDetailUserDTO {
    Long getId();

    String getName();

    Long getAvatarId();

    String getAvatarPath();

    String getFirstName();

    String getLastName();

    String getCode();

    String getIdentificationNum();

    String getIdentityType();

    String getAddress();

    String getGender();


    String getBirthDate();

    String getPhoneNumber();

    String getEmail();

    String getUserName();

    String getDepartment();

    Long getDepartmentId();

    String getCreatedAt();

    String getModifiedAt();

    Integer getUserType();
    String getRoleJson();

    Integer getCourseNum();
}
