package com.elearning.elearning_support.dtos.test.studentTestSet;

import java.util.Collections;
import java.util.Set;
import javax.validation.constraints.NotNull;
import com.elearning.elearning_support.constants.message.messageConst.MessageConst.Validation;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AIExtractionReqDTO {

    @JsonProperty("examClassCode")
    @NotNull(message = Validation.NOT_NULL)
    String examClassCode;

    @JsonProperty("mode")
    String mode; // "accuracy" or "speed"

    @JsonProperty("numberAnswers")
    Integer numberAnswers; // "ALL" or a specific number of answers


    @JsonProperty("selectedImages")
    Set<String> selectedImages = Collections.singleton("ALL");

}
