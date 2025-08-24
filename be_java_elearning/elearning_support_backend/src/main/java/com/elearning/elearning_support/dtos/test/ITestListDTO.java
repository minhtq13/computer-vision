package com.elearning.elearning_support.dtos.test;

import java.math.BigDecimal;
import java.util.Date;
import com.elearning.elearning_support.utils.DateUtils;
import com.fasterxml.jackson.annotation.JsonFormat;

public interface ITestListDTO {

    Long getId();

    String getName();

    Long getSemesterId();

    String getSemester();

    Integer getDuration();

    BigDecimal getTotalPoint();

    Integer getQuestionQuantity();

    Long getSubjectId();

    String getSubjectName();

    String getSubjectCode();

    @JsonFormat(pattern = DateUtils.FORMAT_DATE_DD_MM_YYYY_HH_MM_SS, timezone = DateUtils.TIME_ZONE)
    Date getCreatedAt();

    Long getCreatedBy();

    @JsonFormat(pattern = DateUtils.FORMAT_DATE_DD_MM_YYYY_HH_MM_SS, timezone = DateUtils.TIME_ZONE)
    Date getModifiedAt();

    @JsonFormat(pattern = DateUtils.FORMAT_DATE_DD_MM_YYYY_HH_MM, timezone = DateUtils.TIME_ZONE)
    Date getStartTime();

    @JsonFormat(pattern = DateUtils.FORMAT_DATE_DD_MM_YYYY_HH_MM, timezone = DateUtils.TIME_ZONE)
    Date getEndTime();

    String getLstTestSetCode();

    String getGenTestConfig();

    String getTestType();


}
