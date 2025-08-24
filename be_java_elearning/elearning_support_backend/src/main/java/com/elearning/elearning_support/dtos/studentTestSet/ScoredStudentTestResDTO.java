package com.elearning.elearning_support.dtos.studentTestSet;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ScoredStudentTestResDTO {

    Long studentId;

    Float correctRate;

    Integer markedNum;

    Double mark;

}
