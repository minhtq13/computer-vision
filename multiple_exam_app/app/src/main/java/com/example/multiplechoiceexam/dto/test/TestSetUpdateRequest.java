package com.example.multiplechoiceexam.dto.test;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TestSetUpdateRequest {
    @SerializedName("testNo")
    @Expose
    private String testNo;
    @SerializedName("duration")
    @Expose
    private Integer duration;
    @SerializedName("testDay")
    @Expose
    private String testDay;
    @SerializedName("questionPositions")
    @Expose
    private List<QuestionPosition> questionPositions;

    public String getTestNo() {
        return testNo;
    }

    public void setTestNo(String testNo) {
        this.testNo = testNo;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getTestDay() {
        return testDay;
    }

    public void setTestDay(String testDay) {
        this.testDay = testDay;
    }

    public List<QuestionPosition> getQuestionPositions() {
        return questionPositions;
    }

    public void setQuestionPositions(List<QuestionPosition> questionPositions) {
        this.questionPositions = questionPositions;
    }

    public static class QuestionPosition{
        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("questionNo")
        @Expose
        private Integer questionNo;
        @SerializedName("answerPositions")
        @Expose
        private List<AnswerPosition> answerPositions;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Integer getQuestionNo() {
            return questionNo;
        }

        public void setQuestionNo(Integer questionNo) {
            this.questionNo = questionNo;
        }

        public List<AnswerPosition> getAnswerPositions() {
            return answerPositions;
        }

        public void setAnswerPositions(List<AnswerPosition> answerPositions) {
            this.answerPositions = answerPositions;
        }

        public static class AnswerPosition{
            @SerializedName("id")
            @Expose
            private Integer id;
            @SerializedName("answerNo")
            @Expose
            private String answerNo;

            public Integer getId() {
                return id;
            }

            public void setId(Integer id) {
                this.id = id;
            }

            public String getAnswerNo() {
                return answerNo;
            }

            public void setAnswerNo(String answerNo) {
                this.answerNo = answerNo;
            }
        }
    }
}
