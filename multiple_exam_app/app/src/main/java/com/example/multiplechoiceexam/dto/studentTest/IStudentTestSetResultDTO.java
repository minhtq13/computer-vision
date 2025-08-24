package com.example.multiplechoiceexam.dto.studentTest;

public interface IStudentTestSetResultDTO {
    Long getId();

    Long getStudentId();

    String getStudentName();

    Long getTestSetId();

    String getTestSetCode();

    Long getExamClassId();

    String getExamClassCode();

    Integer getNumMarkedAnswers();

    Integer getNumCorrectAnswers();

    Double getTotalPoints();

    String getHandledSheetImg();
}
