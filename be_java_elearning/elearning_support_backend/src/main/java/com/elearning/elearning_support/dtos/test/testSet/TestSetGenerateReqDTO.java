package com.elearning.elearning_support.dtos.test.testSet;

import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TestSetGenerateReqDTO {

    @NotNull
    Long testId;

    @NotNull
    Integer numOfTestSet;
}
