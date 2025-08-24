package com.elearning.elearning_support.dtos.test.studentTestSet;

import java.io.Serializable;
import java.util.Set;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SubmissionDataItem implements Serializable {

    @Schema(description = "Id câu hỏi trong đề thi")
    Long testSetQuestionId;

    @Schema(description = "Số thứ tự câu hỏi")
    Integer questionNo;

    @Schema(description = "Các câu hỏi lựa chọn theo quy ước (1-A, 2-B, 3-C, 4-D, 5-E, 6-F)")
    Set<Integer> selectedAnswers;

}
