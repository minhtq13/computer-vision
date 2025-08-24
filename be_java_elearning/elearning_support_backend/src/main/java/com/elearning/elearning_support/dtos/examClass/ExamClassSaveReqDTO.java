package com.elearning.elearning_support.dtos.examClass;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import com.elearning.elearning_support.constants.message.messageConst.MessageConst;
import com.elearning.elearning_support.enums.test.TestTypeEnum;
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
public class ExamClassSaveReqDTO {

    @NotNull
    @NotBlank
    @Size(min = 1, max = 10, message = MessageConst.INVALID_FIELD_LENGTH)
    @Schema(description = "Mã lớp thi")
    String code;

    @Schema(description = "Phòng thi")
    String roomName;

    @Schema(description = "Thời gian thi")
    @JsonFormat(pattern = DateUtils.FORMAT_DATE_HH_MM_YYYY_HH_MM, timezone = DateUtils.TIME_ZONE)
    Date examineTime;

    @NotNull
    @Schema(description = "Id kỳ thi")
    Long testId;

    @Schema(description = "Danh sách id giám thị")
    Set<Long> lstSupervisorId = new HashSet<>();

    @Schema(description = "Danh sách id thí sinh")
    Set<Long> lstStudentId = new HashSet<>();

    @Schema(description = "Danh sách id giảng viên giảng dạy")
    Set<Long> lstLecturerId = new HashSet<>();

    @Schema(description = "Hình thức thi")
    TestTypeEnum testType = TestTypeEnum.OFFLINE;

}
