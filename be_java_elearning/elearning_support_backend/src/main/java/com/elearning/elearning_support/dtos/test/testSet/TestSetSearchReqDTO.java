package com.elearning.elearning_support.dtos.test.testSet;


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
public class TestSetSearchReqDTO {

    @Schema(description = "Id đề thi")
    Long testSetId;

    @Schema(description = "Id kỳ thi")
    @NotNull
    Long testId;

    @Schema(description = "Mã đề thi trong kỳ thi")
    @NotNull
    String code;

    @Schema(description = "Số thứ tự đề thi")
    String testNo;

}
