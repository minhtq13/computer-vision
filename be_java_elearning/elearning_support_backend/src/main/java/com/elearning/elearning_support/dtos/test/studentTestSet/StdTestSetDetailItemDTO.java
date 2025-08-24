package com.elearning.elearning_support.dtos.test.studentTestSet;

import java.util.Date;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;


@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StdTestSetDetailItemDTO {

    Boolean isEnabled = Boolean.TRUE;

    Boolean isCorrected;

    Integer[] selectedAnswers;

    Integer[] correctAnswers;

    Long testSetQuestionId;

    @CreatedBy
    Long createdBy;

    @CreatedDate
    Date createdAt;

}
