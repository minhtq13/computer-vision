package com.example.multiplechoiceexam.dto.question;


import java.util.ArrayList;
import java.util.List;

public class MultiQuestionRequest {
    Long chapterId;

    List<SingleQuestionRequest> lstQuestion = new ArrayList<>();

    public MultiQuestionRequest() {
    }

    public Long getChapterId() {
        return chapterId;
    }

    public void setChapterId(Long chapterId) {
        this.chapterId = chapterId;
    }

    public List<SingleQuestionRequest> getLstQuestion() {
        return lstQuestion;
    }

    public void setLstQuestion(List<SingleQuestionRequest> lstQuestion) {
        this.lstQuestion = lstQuestion;
    }
}
