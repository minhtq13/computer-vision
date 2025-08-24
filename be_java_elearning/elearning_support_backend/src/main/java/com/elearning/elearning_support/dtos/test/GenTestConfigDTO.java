package com.elearning.elearning_support.dtos.test;

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
public class GenTestConfigDTO {

    @NotNull
    Integer numTotalQuestion = 0;

    @NotNull
    Integer numEasyQuestion = 0;

    @NotNull
    Integer numMediumQuestion = 0;

    @NotNull
    Integer numHardQuestion = 0;


}
