package com.elearning.elearning_support.dtos.question.importQuestion;

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
public class QuestionImportDTO {

    @Schema(description = "Nội dung câu hỏi")
    String content;

    @Schema(description = "Mức độ câu hỏi")
    String levelRaw;

    @Schema(description = "Số thứ tự chương")
    String chapterNo;

    @Schema(description = "Mã môn học")
    String subjectCode;

    @Schema(description = "Đáp án trả lời thứ 1")
    String firstAnswer;

    @Schema(description = "Đáp án trả lời thứ 2")
    String secondAnswer;

    @Schema(description = "Đáp án trả lời thứ 3")
    String thirdAnswer;

    @Schema(description = "Đáp án trả lời thứ 4")
    String fourthAnswer;

    @Schema(description = "Các đáp án đúng dạng '1,2,3,...'")
    String correctAnswers;
}
