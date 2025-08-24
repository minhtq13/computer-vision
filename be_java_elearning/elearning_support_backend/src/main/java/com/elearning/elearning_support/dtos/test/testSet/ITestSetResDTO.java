package com.elearning.elearning_support.dtos.test.testSet;

import java.util.Date;
import com.elearning.elearning_support.utils.DateUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

public interface ITestSetResDTO {

    Long getTestSetId();

    Long getTestId();

    String getTestName();

    String getSubjectTitle();

    String getSubjectCode();

    String getDepartmentName();

    Integer getQuestionQuantity();

    Integer getDuration();
    String getTestSetCode();

    String getSemester();

    @JsonFormat(pattern = DateUtils.FORMAT_DATE_DD_MM_YYYY_HH_MM, timezone = DateUtils.TIME_ZONE)
    Date getCreatedAt();

    @JsonFormat(pattern = DateUtils.FORMAT_DATE_DD_MM_YYYY_HH_MM, timezone = DateUtils.TIME_ZONE)
    Date getModifiedAt();

    @Schema(description = "Đề thi có được sử dụng tài liệu hay không")
    Boolean getIsAllowedUsingDocuments();
}
