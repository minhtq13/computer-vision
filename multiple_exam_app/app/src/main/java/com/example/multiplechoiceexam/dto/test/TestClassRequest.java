package com.example.multiplechoiceexam.dto.test;

import java.util.List;
public class TestClassRequest {


        String name;
        Long subjectId;
        List<Long> chapterIds ;

        Integer questionQuantity = 1;

        String startTime;

        String endTime;

        Long semesterId;

        Integer totalPoint;
        Integer duration;

        GenTestConfigDTO generateConfig = new GenTestConfigDTO();

        String description;

    public TestClassRequest() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Long subjectId) {
        this.subjectId = subjectId;
    }

    public List<Long> getChapterIds() {
        return chapterIds;
    }

    public void setChapterIds(List<Long> chapterIds) {
        this.chapterIds = chapterIds;
    }

    public Integer getQuestionQuantity() {
        return questionQuantity;
    }

    public void setQuestionQuantity(Integer questionQuantity) {
        this.questionQuantity = questionQuantity;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Long getSemesterId() {
        return semesterId;
    }

    public void setSemesterId(Long semesterId) {
        this.semesterId = semesterId;
    }

    public Integer getTotalPoint() {
        return totalPoint;
    }

    public void setTotalPoint(Integer totalPoint) {
        this.totalPoint = totalPoint;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public GenTestConfigDTO getGenerateConfig() {
        return generateConfig;
    }

    public void setGenerateConfig(GenTestConfigDTO generateConfig) {
        this.generateConfig = generateConfig;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public static class GenTestConfigDTO {

        Integer numTotalQuestion = 0;

        Integer numEasyQuestion = 0;

        Integer numMediumQuestion = 0;

        Integer numHardQuestion = 0;

        public GenTestConfigDTO() {
        }

        public Integer getNumTotalQuestion() {
            return numTotalQuestion;
        }

        public void setNumTotalQuestion(Integer numTotalQuestion) {
            this.numTotalQuestion = numTotalQuestion;
        }

        public Integer getNumEasyQuestion() {
            return numEasyQuestion;
        }

        public void setNumEasyQuestion(Integer numEasyQuestion) {
            this.numEasyQuestion = numEasyQuestion;
        }

        public Integer getNumMediumQuestion() {
            return numMediumQuestion;
        }

        public void setNumMediumQuestion(Integer numMediumQuestion) {
            this.numMediumQuestion = numMediumQuestion;
        }

        public Integer getNumHardQuestion() {
            return numHardQuestion;
        }

        public void setNumHardQuestion(Integer numHardQuestion) {
            this.numHardQuestion = numHardQuestion;
        }
    }
}
