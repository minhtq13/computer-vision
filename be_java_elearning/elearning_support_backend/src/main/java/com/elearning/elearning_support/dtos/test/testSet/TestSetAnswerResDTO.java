package com.elearning.elearning_support.dtos.test.testSet;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TestSetAnswerResDTO {

    Long answerId;

    @Schema(description = "Thứ tự đáp án dạng số 1, 2, 3, 4,...")
    Integer answerNo;

    @Schema(description = "Đáp án dạng A,B,C,D,...")
    String answerNoMask;

    @Schema(description = "Cờ đánh dấu đáp án đúng")
    Boolean isCorrect;

    @Schema(description = "Nội dung đáp án")
    String content;

    @Schema(description = "Phương án có đợc check hay không")
    Boolean isChecked = false;
}
