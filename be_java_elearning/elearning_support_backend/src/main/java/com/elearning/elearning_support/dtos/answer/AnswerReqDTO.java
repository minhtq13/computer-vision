package com.elearning.elearning_support.dtos.answer;

import com.fasterxml.jackson.annotation.JsonProperty;
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
public class AnswerReqDTO {

    @Schema(description = "Id của answer (case update)")
    @JsonProperty("id")
    Long currentId;

    @Schema(description = "Nội dung câu trả lời")
    String content;

    @Schema(description = "Cờ kiểm tra đúng sai")
    Boolean isCorrect;

    @Schema(description = "Id ảnh trong đáp án")
    Long imageId;

    public AnswerReqDTO(String content, Boolean isCorrect, Long imageId) {
        this.content = content;
        this.isCorrect = isCorrect;
        this.imageId = imageId;
    }
}
