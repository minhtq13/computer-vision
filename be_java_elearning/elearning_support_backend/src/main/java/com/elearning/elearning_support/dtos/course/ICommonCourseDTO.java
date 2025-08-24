package com.elearning.elearning_support.dtos.course;

import java.util.Date;
import com.elearning.elearning_support.utils.DateUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

public interface ICommonCourseDTO {

    @Schema(description = "Id lớp thi")
    Long getId();

    @Schema(description = "Mã lớp thi")
    String getCode();

    @Schema(description = "Phòng thi")
    String getRoomName();

    @Schema(description = "Số lượng giảng viên")
    Integer getNumberOfTeachers();

    @Schema(description = "Số lượng sinh viên")
    Integer getNumberOfStudents();

    @Schema(description = "Giờ học")
    String getCourseTime();

    @Schema(description = "Tuần học")
    String getCourseWeeks();

    @Schema(description = "Id kỳ học")
    Long getSemesterId();

    @Schema(description = "Tên kỳ học")
    String getSemester();

    @Schema(description = "Id học phần")
    Long getSubjectId();

    @Schema(description = "Mã học phần")
    String getSubjectCode();

    @Schema(description = "Tiêu đề học phần")
    String getSubjectTitle();

}
