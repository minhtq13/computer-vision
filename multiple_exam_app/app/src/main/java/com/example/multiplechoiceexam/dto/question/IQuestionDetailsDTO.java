package com.example.multiplechoiceexam.dto.question;

public interface IQuestionDetailsDTO {
    Long getId();

    String getContent();

    String getCode();

    Long getSubjectId();

    String getSubjectTitle();

    Long getChapterId();

    String getChapterTitle();

    String getLstImageJson();

    String getLstAnswerJson();
}
