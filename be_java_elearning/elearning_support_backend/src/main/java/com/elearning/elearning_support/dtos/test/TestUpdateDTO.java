package com.elearning.elearning_support.dtos.test;


import java.util.HashSet;
import java.util.Set;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
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
public class TestUpdateDTO {

    @Schema(description = "Số câu hỏi trong một đề thi")
    @NotNull
    @Min(value = 1)
    @Max(value = 120)
    Integer questionQuantity = 1;

    @Schema(description = "Điểm tối đa của bài thi")
    @Min(value = 1)
    @Max(value = 100)
    Integer totalPoint;

    @Schema(description = "Thời gian làm bài theo đơn vị phút")
    @Min(value = 5)
    Integer duration;

    @Schema(description = "Cấu hình tạo đề thi (tổng số câu hỏi và số câu hỏi theo các mức độ)")
    GenTestConfigDTO generateConfig = new GenTestConfigDTO();

    @Schema(description = "Id của các câu hỏi trong kỳ thi (ngân hàng câu hỏi của kỳ thi)")
    @NotNull
    Set<Long> questionIds = new HashSet<>();
}
