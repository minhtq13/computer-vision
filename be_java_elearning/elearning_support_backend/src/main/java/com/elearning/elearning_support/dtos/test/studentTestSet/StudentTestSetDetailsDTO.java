package com.elearning.elearning_support.dtos.test.studentTestSet;

import java.util.Date;
import java.util.List;
import com.elearning.elearning_support.enums.test.StudentTestStatusEnum;
import com.elearning.elearning_support.utils.DateUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StudentTestSetDetailsDTO {

    @Schema(description = "Id bài thi")
    Long studentTestSetId;

    @Schema(description = "Id đề thi")
    Long testSetId;

    @Schema(description = "Id thí sinh")
    Long studentId;

    @Schema(description = "Trạng thái bài thi")
    Integer status;

    @Schema(description = "Trạng thái bài thi dạng tag")
    StudentTestStatusEnum statusTag;

    @Schema(description = "Thời gian lưu lần cuối")
    @JsonFormat(pattern = DateUtils.FORMAT_DATE_DD_MM_YYYY_HH_MM_SS, timezone = DateUtils.TIME_ZONE)
    Date savedTime;

    @Schema(description = "Thời gian bắt đầu làm bài")
    @JsonFormat(pattern = DateUtils.FORMAT_DATE_DD_MM_YYYY_HH_MM_SS, timezone = DateUtils.TIME_ZONE)
    Date startedTime;

    @Schema(description = "Hạn submit bài làm")
    @JsonFormat(pattern = DateUtils.FORMAT_DATE_DD_MM_YYYY_HH_MM_SS, timezone = DateUtils.TIME_ZONE)
    Date allowedSubmitTime;

    @Schema(description = "Hạn submit bài làm")
    @JsonFormat(pattern = DateUtils.FORMAT_DATE_DD_MM_YYYY_HH_MM_SS, timezone = DateUtils.TIME_ZONE)
    Date allowedStartTime;

    @Schema(description = "Timezone đồng bộ")
    String timeZoneReference = DateUtils.TIME_ZONE;

    @Schema(description = "Thời gian làm bài")
    Integer duration;

    @Schema(description = "Dữ liệu bài làm lưu lần gần nhất")
    List<SubmissionDataItem> temporarySubmission;

    @Schema(description = "Điểm bài thi")
    Double totalPoints;

    Integer questionQuantity;

    // test information
    String testName;

    String subjectTitle;

    String subjectCode;

    String semester;

}
