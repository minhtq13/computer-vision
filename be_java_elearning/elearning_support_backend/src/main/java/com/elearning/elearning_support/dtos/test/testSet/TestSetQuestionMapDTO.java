package com.elearning.elearning_support.dtos.test.testSet;


import java.util.ArrayList;
import java.util.List;
import com.elearning.elearning_support.dtos.question.QuestionAnswerDTO;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TestSetQuestionMapDTO {

    Long testSetId;

    Integer totalPoint;

    List<QuestionAnswerDTO> lstQuestionAnswer = new ArrayList<>();

}
