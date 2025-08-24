package com.elearning.elearning_support.dtos.test.testSet;

import java.util.List;
import com.elearning.elearning_support.dtos.test.testQuestion.TestQuestionAnswerResDTO;
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
public class TestSetDetailDTO {

    @Schema(description = "Thông tin đề thi")
    ITestSetResDTO testSet;

    @Schema(description = "Danh sách câu hỏi trong đề thi")
    List<TestQuestionAnswerResDTO> lstQuestion;

}
