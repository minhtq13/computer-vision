package com.elearning.elearning_support.dtos.answer;

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
public class AnswerResDTO {

    @Schema(description = "Id của câu trả lời")
    Long id;

    @Schema(description = "Nội dung câu trả lời")
    String content;

    @Schema(description = "Cờ kiểm tra đúng sai")
    Boolean isCorrect;

}
