package com.elearning.elearning_support.dtos.subject;

import java.util.Date;
import com.elearning.elearning_support.utils.DateUtils;
import com.fasterxml.jackson.annotation.JsonFormat;

public interface ISubjectListDTO {

    Long getId();

    String getTitle();

    String getCode();

    Integer getCredit();

    String getDepartmentName();

    @JsonFormat(pattern = DateUtils.FORMAT_DATE_DD_MM_YYYY_HH_MM_SS, timezone = DateUtils.TIME_ZONE)
    Date getModifiedAt();
}
