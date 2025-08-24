package com.example.multiplechoiceexam.dto.answer;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AnswerResponse {

    @SerializedName("id")
    @Expose
    private Long id;
    @SerializedName("content")
    @Expose
    private String content;
    @SerializedName("isCorrect")
    @Expose
    private Boolean isCorrected;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Boolean getCorrected() {
        return isCorrected;
    }

    public void setCorrected(Boolean corrected) {
        isCorrected = corrected;
    }
}
