package com.elearning.elearning_support.dtos.test.studentTestSet;

public interface IStudentTestSetResultDTO {

    Long getId();

    Long getStudentId();

    String getStudentName();

    String getStudentCode();

    Long getTestSetId();

    String getTestSetCode();

    Long getExamClassId();

    String getExamClassCode();

    Integer getNumTestSetQuestions();

    Integer getNumMarkedAnswers();

    Integer getNumCorrectAnswers();

    String getCorrectAnswersStr();

    Double getTotalPoints();

    String getHandledSheetImg();

    Integer getStoredType();

    Integer getStatus();

    String getStatusLabel();
}
