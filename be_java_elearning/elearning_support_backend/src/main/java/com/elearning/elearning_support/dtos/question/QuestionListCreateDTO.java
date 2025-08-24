package com.elearning.elearning_support.dtos.question;

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
public class QuestionListCreateDTO {

    @Schema(description = "Id chương")
    @NotNull
    Long chapterId;

    List<QuestionCreateDTO> lstQuestion = new ArrayList<>();



}
