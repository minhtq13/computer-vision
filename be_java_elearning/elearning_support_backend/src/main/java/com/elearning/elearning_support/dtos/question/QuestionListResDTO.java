package com.elearning.elearning_support.dtos.question;

import java.util.ArrayList;
import java.util.List;
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
public class QuestionListResDTO {

    Integer fetchedSize = 30;

    Integer totalSize = 0;

    List<QuestionListDTO> questions = new ArrayList<>();

}
