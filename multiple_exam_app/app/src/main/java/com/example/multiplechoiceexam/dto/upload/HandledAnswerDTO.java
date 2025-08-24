package com.example.multiplechoiceexam.dto.upload;

public class HandledAnswerDTO {
    Integer questionNo;
    String selectedAnswers;
    String correctAnswers;

    public String getCorrectAnswers() {
        return correctAnswers;
    }

    public void setCorrectAnswers(String correctAnswers) {
        this.correctAnswers = correctAnswers;
    }

    public HandledAnswerDTO() {
    }

    public Integer getQuestionNo() {
        return questionNo;
    }

    public void setQuestionNo(Integer questionNo) {
        this.questionNo = questionNo;
    }

    public String getSelectedAnswers() {
        return selectedAnswers;
    }

    public void setSelectedAnswers(String selectedAnswers) {
        this.selectedAnswers = selectedAnswers;
    }
}
