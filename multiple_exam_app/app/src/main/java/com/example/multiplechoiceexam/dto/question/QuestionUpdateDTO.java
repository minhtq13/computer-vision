package com.example.multiplechoiceexam.dto.question;

import com.example.multiplechoiceexam.dto.answer.AnswerRequest;

import java.util.ArrayList;
import java.util.List;

public class QuestionUpdateDTO {
    Long chapterId;

    String content;

    QuestionLevelEnum level;

    Long[] lstImageId;

    List<AnswerRequest> lstAnswer = new ArrayList<>();

    public QuestionUpdateDTO() {
    }

    public Long getChapterId() {
        return chapterId;
    }

    public void setChapterId(Long chapterId) {
        this.chapterId = chapterId;
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
