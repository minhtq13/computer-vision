package com.elearning.elearning_support.dtos.question;

public interface IQuestionDetailsDTO {

    Long getId();

    String getContent();

    Integer getLevel();

    String getCode();

    Long getSubjectId();

    String getSubjectTitle();

    Long getChapterId();

    String getChapterTitle();

    String getLstImageJson();

    String getLstAnswerJson();

    Boolean getIsMultipleAnswers();

    String getTagIdsStr();

}
