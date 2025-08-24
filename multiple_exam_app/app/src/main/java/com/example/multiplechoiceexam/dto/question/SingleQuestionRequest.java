package com.example.multiplechoiceexam.dto.question;

import com.example.multiplechoiceexam.dto.answer.AnswerRequest;

import java.util.ArrayList;
import java.util.List;

public class SingleQuestionRequest {

    String content;

    QuestionLevelEnum level;

    Long[] lstImageId;
    String imagePath;

    List<AnswerRequest> lstAnswer = new ArrayList<>();

    public SingleQuestionRequest() {
    }

    public SingleQuestionRequest(String content, QuestionLevelEnum level, List<AnswerRequest> lstAnswer) {
        this.content = content;
        this.level = level;
        this.lstAnswer = lstAnswer;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public QuestionLevelEnum getLevel() {
        return level;
    }

    public void setLevel(QuestionLevelEnum level) {
        this.level = level;
    }

    public Long[] getLstImageId() {
        return lstImageId;
    }

    public void setLstImageId(Long[] lstImageId) {
        this.lstImageId = lstImageId;
    }

    public List<AnswerRequest> getLstAnswer() {
        return lstAnswer;
    }

    public void setLstAnswer(List<AnswerRequest> lstAnswer) {
        this.lstAnswer = lstAnswer;
    }
}
