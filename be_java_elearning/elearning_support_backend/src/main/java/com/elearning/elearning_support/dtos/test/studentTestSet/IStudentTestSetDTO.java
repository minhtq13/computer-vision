package com.elearning.elearning_support.dtos.test.studentTestSet;

import java.util.Date;
import com.elearning.elearning_support.utils.DateUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

public interface IStudentTestSetDTO {

    @Schema(description = "Id kết quả bài thi")
    Long getStudentTestSetId();

    @Schema(description = "Trạng thái bài làm")
    Integer getStatus();

    @Schema(description = "Trạng thái bài làm dạng tag")
    String getStatusTag();

    @JsonFormat(pattern = DateUtils.FORMAT_DATE_DD_MM_YYYY_HH_MM_SS, timezone = DateUtils.TIME_ZONE)
    Date getSubmittedTime();

    @JsonFormat(pattern = DateUtils.FORMAT_DATE_DD_MM_YYYY_HH_MM_SS, timezone = DateUtils.TIME_ZONE)
    Date getStartedTime();

    @JsonFormat(pattern = DateUtils.FORMAT_DATE_DD_MM_YYYY_HH_MM_SS, timezone = DateUtils.TIME_ZONE)
    Date getAllowedStartTime();

    @JsonFormat(pattern = DateUtils.FORMAT_DATE_DD_MM_YYYY_HH_MM_SS, timezone = DateUtils.TIME_ZONE)
    Date getAllowedSubmitTime();

    @Schema(description = "Điểm bài thi")
    Integer getTotalPoints();

    // thông tin bài thi, môn học, lớp thi, thời gian làm bài
    Long getSubjectId();

    String getSubjectCode();

    String getSubjectTitle();

    String getExamClassId();

    String getExamClassCode();

    String getSemester();

    Integer getDuration();

    Integer getNumTestSetQuestions();


}
