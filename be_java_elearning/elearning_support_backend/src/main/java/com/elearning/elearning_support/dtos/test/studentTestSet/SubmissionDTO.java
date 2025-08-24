package com.elearning.elearning_support.dtos.test.studentTestSet;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
public class SubmissionDTO {

    @Schema(description = "Id bản ghi bài thi")
    Long studentTestSetId;

    @Schema(description = "Dữ liệu bài làm")
    List<SubmissionDataItem> submissionData = new ArrayList<>();

    @Schema(description = "Thời gian bắt đầu làm bài (lấy theo thời gian click start trên FE)")
    @JsonFormat(pattern = DateUtils.FORMAT_DATE_DD_MM_YYYY_HH_MM_SS, timezone = DateUtils.TIME_ZONE)
    Date startedTime;

    @Schema(description = "Thời lưu dữ liệu bài làm (lấy theo thời gian trên FE)")
    @JsonFormat(pattern = DateUtils.FORMAT_DATE_DD_MM_YYYY_HH_MM_SS, timezone = DateUtils.TIME_ZONE)
    Date saveTime;

    @Schema(description = "Thời gian bắt đầu làm bài (lấy theo thời gian click submit trên FE)")
    @JsonFormat(pattern = DateUtils.FORMAT_DATE_DD_MM_YYYY_HH_MM_SS, timezone = DateUtils.TIME_ZONE)
    Date submittedTime;

    @Schema(description = "Ghi chú khi nộp bài")
    String submissionNote;

}
