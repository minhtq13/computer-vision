package com.elearning.elearning_support.dtos.question.importQuestion;

import java.util.HashMap;
import java.util.Map;
import org.apache.commons.math3.util.Pair;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ImportQuestionValidatorDTO {

    Map<String, Long> mapSubjectCodeId = new HashMap<>();

    Map<Pair<Long, Integer>, Long> mapSubjectChapters = new HashMap<>();

}
