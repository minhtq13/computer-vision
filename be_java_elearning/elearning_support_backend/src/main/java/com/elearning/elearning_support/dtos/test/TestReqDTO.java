package com.elearning.elearning_support.dtos.test;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
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
public class TestReqDTO {

    @Schema(description = "Tên kỳ thi")
    @NotNull
    String name;

    @Schema(description = "Id môn học")
    @NotNull
    Long subjectId;

    @NotNull
    @Schema(description = "Hình thức thi (0: Offline, 1: Online)")
    TestTypeEnum testType = TestTypeEnum.OFFLINE;

    @Schema(description = "Id của các câu hỏi trong kỳ thi (ngân hàng câu hỏi của kỳ thi)")
    @NotNull
    Set<Long> questionIds = new HashSet<>();

    @Schema(description = "Id của các chương muốn đưa nội dung vào test")
    Set<Long> chapterIds = new HashSet<>();

    @Schema(description = "Số câu hỏi trong một đề thi")
    @NotNull
    @Min(value = 1)
    @Max(value = 120)
    Integer questionQuantity = 1;

    @Schema(description = "Thời gian bắt đầu kỳ thi")
    @JsonFormat(pattern = DateUtils.FORMAT_DATE_DD_MM_YYYY_HH_MM, timezone = DateUtils.TIME_ZONE)
    Date startTime;

    @Schema(description = "Thời gian kết thúc kỳ thi")
    @JsonFormat(pattern = DateUtils.FORMAT_DATE_DD_MM_YYYY_HH_MM, timezone = DateUtils.TIME_ZONE)
    Date endTime;

    @Schema(description = "Id học kỳ")
    Long semesterId;

    @Schema(description = "Điểm tối đa của bài thi")
    @Min(value = 1)
    @Max(value = 100)
    Integer totalPoint;

    @Schema(description = "Thời gian làm bài theo đơn vị phút")
    @Min(value = 5)
    Integer duration;

    @Schema(description = "Cấu hình tạo đề thi (tổng số câu hỏi và số câu hỏi theo các mức độ)")
    GenTestConfigDTO generateConfig = new GenTestConfigDTO();

    @Schema(description = "Mô tả kỳ thi")
    String description;

    @Schema(description = "Cho phép sử dụng tài liệu hay không")
    Boolean isAllowedUsingDocuments = Boolean.FALSE;
}
