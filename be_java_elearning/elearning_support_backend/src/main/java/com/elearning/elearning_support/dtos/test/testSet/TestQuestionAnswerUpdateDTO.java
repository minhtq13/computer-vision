package com.elearning.elearning_support.dtos.test.testSet;


import java.util.ArrayList;
import java.util.List;
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
public class TestQuestionAnswerUpdateDTO {

    @NotNull
    @Schema(description = "Id câu hỏi")
    Long questionId;

    @Schema(description = "Số thứ tự câu hỏi")
    @Min(value = 1)
    Integer questionNo;

    @Schema(description = "Các câu trả lời và thứ tự tương ứng")
    List<TestQuestionAnswer> answers = new ArrayList<>();
}
