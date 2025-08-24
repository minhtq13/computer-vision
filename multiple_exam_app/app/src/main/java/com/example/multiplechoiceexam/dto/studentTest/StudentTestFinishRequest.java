package com.example.multiplechoiceexam.dto.studentTest;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StudentTestFinishRequest {
    @SerializedName("examClassId")
    @Expose
    private Integer examClassId;
    @SerializedName("testNo")
    @Expose
    private String testNo;
    @SerializedName("questions")
    @Expose
    private List<Question> questions;

    public Integer getExamClassId() {
        return examClassId;
    }

    public void setExamClassId(Integer examClassId) {
        this.examClassId = examClassId;
    }

    public String getTestNo() {
        return testNo;
    }

    public void setTestNo(String testNo) {
        this.testNo = testNo;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }
    public static class Question{
        @SerializedName("questionNo")
        @Expose
        private Integer questionNo;
        @SerializedName("selectedAnswerNo")
        @Expose
        private String selectedAnswerNo;

        public Integer getQuestionNo() {
            return questionNo;
        }

        public void setQuestionNo(Integer questionNo) {
            this.questionNo = questionNo;
        }

        public String getSelectedAnswerNo() {
            return selectedAnswerNo;
        }

        public void setSelectedAnswerNo(String selectedAnswerNo) {
            this.selectedAnswerNo = selectedAnswerNo;
        }
    }

}
