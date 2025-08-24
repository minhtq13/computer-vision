package com.elearning.elearning_support.dtos.course;

import java.util.Date;
import com.elearning.elearning_support.utils.DateUtils;
import com.fasterxml.jackson.annotation.JsonFormat;

public interface ICourseDetailDTO extends ICommonCourseDTO {

    @JsonFormat(pattern = DateUtils.FORMAT_DATE_HH_MM_YYYY_HH_MM, timezone = DateUtils.TIME_ZONE)
    Date getLastModifiedAt();

    String getLstStudentId();

    String getLstTeacherId();


}
