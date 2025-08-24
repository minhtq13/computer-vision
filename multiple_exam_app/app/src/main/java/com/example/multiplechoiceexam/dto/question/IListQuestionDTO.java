package com.example.multiplechoiceexam.dto.question;

public interface IListQuestionDTO {
    Long getId();

    String getCode();

    String getContent();

    Integer getLevel();

    String getLstImageJson();

    String getLstAnswerJson();
}
