package com.elearning.elearning_support.dtos.users;

import java.util.Date;

import com.elearning.elearning_support.utils.DateUtils;
import com.fasterxml.jackson.annotation.JsonFormat;

public interface GetAllUserDTO {

    Long getRoleBaseId();

    Long getId();

    String getCode();

    String getGender();

    String getFirstName();

    String getLastName();

    @JsonFormat(pattern = DateUtils.FORMAT_DATE_DD_MM_YYYY_SLASH, timezone = DateUtils.TIME_ZONE)
    Date getBirthDate();

    String getPhoneNumber();

    String getEmail();

    Integer getCourseNum();

    String getRoleInfoJson();
}
