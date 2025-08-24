package com.elearning.elearning_support.dtos.test.testSet;

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
public class TestSetPreviewDTO {

    @Schema(description = "Id của test set")
    Long testSetId;

    @Schema(description = "Mã đề thi trong kỳ thi")
    String testSetCode;

    @Schema(description = "Số thứ tự đề")
    String testSetNo;

    @Schema(description = "Id kỳ thi")
    Long testId;

}
