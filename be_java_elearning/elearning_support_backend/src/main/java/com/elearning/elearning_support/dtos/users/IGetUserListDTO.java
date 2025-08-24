package com.elearning.elearning_support.dtos.users;

import java.util.Date;
import com.elearning.elearning_support.utils.DateUtils;
import com.fasterxml.jackson.annotation.JsonFormat;

public interface IGetUserListDTO {

    String getRoleBaseId();

    Long getRoleId();

    Long getId();

    String getCode();

    Integer getIdentityType();

    String getGender();

    String getGenderName();

    String getIdentificationNumber();

    String getAvatarPath();

    Long getAvatarId();

    String getFirstName();

    String getLastName();

    @JsonFormat(pattern = DateUtils.FORMAT_DATE_DD_MM_YYYY_SLASH, timezone = DateUtils.TIME_ZONE)
    Date getBirthDate();

    String getAddress();

    String getPhoneNumber();

    String getEmail();

    Long getDepartmentId();

    String getDepartmentName();

    String getUserType();

    Integer getCourseNum();
}
