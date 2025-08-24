package com.elearning.elearning_support.dtos.examClass;

import java.util.Date;
import com.elearning.elearning_support.utils.DateUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

public interface ICommonExamClassDTO {

    @Schema(description = "Id lớp thi")
    Long getId();

    @Schema(description = "Mã lớp thi")
    String getCode();

    @Schema(description = "Phòng thi")
    String getRoomName();

    @Schema(description = "Số lượng giám thị")
    Integer getNumberOfSupervisors();

    @Schema(description = "Số lượng thí sinh")
    Integer getNumberOfStudents();

    @JsonFormat(pattern = DateUtils.FORMAT_DATE_DD_MM_YYYY_SLASH, timezone = DateUtils.TIME_ZONE)
    Date getExamineDate();

    @Schema(description = "Giờ thi")
    @JsonFormat(pattern = DateUtils.FORMAT_DATE_HH_MM, timezone = DateUtils.TIME_ZONE)
    Date getExamineTime();

    @Schema(description = "Thời gian làm bài")
    Integer getDuration();

    @Schema(description = "Id kỳ học")
    Long getSemesterId();

    @Schema(description = "Tên kỳ học")
    String getSemester();

    @Schema(description = "Id kỳ thi")
    Long getTestId();

    @Schema(description = "Tên kỳ thi")
    String getTestName();

    @Schema(description = "Id môn học")
    Long getSubjectId();

    @Schema(description = "Tiêu đề môn học")
    String getSubjectTitle();

    @Schema(description = "Hình thức thi")
    String getTestType();

    @JsonFormat(pattern = DateUtils.FORMAT_DATE_DD_MM_YYYY_HH_MM_SS, timezone = DateUtils.TIME_ZONE)
    Date getModifiedAt();

}
