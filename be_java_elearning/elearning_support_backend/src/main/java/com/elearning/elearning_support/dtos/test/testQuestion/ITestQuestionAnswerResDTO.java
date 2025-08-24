package com.elearning.elearning_support.dtos.test.testQuestion;

public interface ITestQuestionAnswerResDTO {

    Long getId();

    Long getTestSetQuestionId();

    String getContent();

    Integer getLevel();

    String getLstImageJson();

    Integer getQuestionNo();

    String getLstAnswerJson();

    Boolean getIsMultipleAnswers();

}
