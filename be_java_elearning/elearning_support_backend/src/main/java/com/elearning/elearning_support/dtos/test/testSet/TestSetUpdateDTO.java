package com.elearning.elearning_support.dtos.test.testSet;

import java.util.ArrayList;
import java.util.List;
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
public class TestSetUpdateDTO {

    @NotNull
    @Schema(description = "Id đề thi")
    Long testSetId;

    @Schema(description = "Danh sách câu hỏi và đáp án")
    List<TestQuestionAnswerUpdateDTO> questions = new ArrayList<>();

}
