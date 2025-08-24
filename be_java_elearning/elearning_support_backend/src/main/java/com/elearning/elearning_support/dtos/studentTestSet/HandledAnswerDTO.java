package com.elearning.elearning_support.dtos.studentTestSet;

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
public class HandledAnswerDTO {

    @Schema(description = "Số thứ tự câu hỏi")
    Integer questionNo;

    @Schema(description = "Các câu hỏi lựa chọn theo quy ước (1-A, 2-B, 3-C, 4-D, 5-E, 6-F)")
    String selectedAnswers;

    @Schema(description = "Đáp án đúng")
    String correctAnswers;

}
