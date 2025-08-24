package com.elearning.elearning_support.dtos.course;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import com.elearning.elearning_support.constants.message.messageConst.MessageConst;
import com.elearning.elearning_support.utils.DateUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CourseSaveReqDTO {

    @NotNull
    @NotBlank
    @Size(min = 1, max = 10, message = MessageConst.INVALID_FIELD_LENGTH)
    @Schema(description = "Mã lớp học")
    String code;

    @Schema(description = "Phòng thi")
    String roomName;

    @Schema(description = "Thời gian thi")
    @JsonFormat(pattern = DateUtils.FORMAT_DATE_HH_MM_YYYY_HH_MM, timezone = DateUtils.TIME_ZONE)
    Date courseTime;

    @Schema(description = "Tuần học")
    Integer[] courseWeeks;

    @Schema(description = "Danh sách Id giảng viên")
    Set<Long> lstTeacherId = new HashSet<>();

    @Schema(description = "Danh sách Id sinh viên")
    Set<Long> lstStudentId = new HashSet<>();

    @Schema(description = "Id kỳ học")
    Long semesterId;

    @Schema(description = "Id học phần")
    Long subjectId;

}
