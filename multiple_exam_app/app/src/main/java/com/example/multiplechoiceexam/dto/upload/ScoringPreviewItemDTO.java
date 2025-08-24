package com.example.multiplechoiceexam.dto.upload;

import java.util.List;

public class ScoringPreviewItemDTO {
    String studentCode;

    String examClassCode;

    String testSetCode;

    String handledScoredImg;

    String originalImgFileName;

    String originalImg;

    Integer numTestSetQuestions;

    Integer numMarkedAnswers;

    Integer numCorrectAnswers;

    Integer numWrongAnswers;

    Double totalScore;
    List<HandledAnswerDTO> details;

    String processedAt;

    public String getStudentCode() {
        return studentCode;
    }

    public void setStudentCode(String studentCode) {
        this.studentCode = studentCode;
    }

    public String getExamClassCode() {
        return examClassCode;
    }

    public void setExamClassCode(String examClassCode) {
        this.examClassCode = examClassCode;
    }



    public String getHandledScoredImg() {
        return handledScoredImg;
    }

    public void setHandledScoredImg(String handledScoredImg) {
        this.handledScoredImg = handledScoredImg;
    }

    public Integer getNumMarkedAnswers() {
        return numMarkedAnswers;
    }

    public void setNumMarkedAnswers(Integer numMarkedAnswers) {
        this.numMarkedAnswers = numMarkedAnswers;
    }

    public String getTestSetCode() {
        return testSetCode;
    }

    public void setTestSetCode(String testSetCode) {
        this.testSetCode = testSetCode;
    }

    public Integer getNumCorrectAnswers() {
        return numCorrectAnswers;
    }

    public void setNumCorrectAnswers(Integer numCorrectAnswers) {
        this.numCorrectAnswers = numCorrectAnswers;
    }

    public Double getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(Double totalScore) {
        this.totalScore = totalScore;
    }

    public List<HandledAnswerDTO> getDetails() {
        return details;
    }

    public void setDetails(List<HandledAnswerDTO> details) {
        this.details = details;
    }

    public String getProcessedAt() {
        return processedAt;
    }

    public void setProcessedAt(String processedAt) {
        this.processedAt = processedAt;
    }

    public String getOriginalImgFileName() {
        return originalImgFileName;
    }

    public void setOriginalImgFileName(String originalImgFileName) {
        this.originalImgFileName = originalImgFileName;
    }

    public String getOriginalImg() {
        return originalImg;
    }

    public void setOriginalImg(String originalImg) {
        this.originalImg = originalImg;
    }

    public Integer getNumTestSetQuestions() {
        return numTestSetQuestions;
    }

    public void setNumTestSetQuestions(Integer numTestSetQuestions) {
        this.numTestSetQuestions = numTestSetQuestions;
    }

    public Integer getNumWrongAnswers() {
        return numWrongAnswers;
    }

    public void setNumWrongAnswers(Integer numWrongAnswers) {
        this.numWrongAnswers = numWrongAnswers;
    }

    public ScoringPreviewItemDTO(StudentHandledTestDTO handledAnswerDTO){
        this.studentCode = handledAnswerDTO.getStudentCode();
        this.examClassCode = handledAnswerDTO.getExamClassCode();
        this.testSetCode = handledAnswerDTO.getTestSetCode();
        this.details = handledAnswerDTO.getAnswers();
        this.handledScoredImg= handledAnswerDTO.getHandledScoredImg();
        this.originalImgFileName = handledAnswerDTO.getOriginalImgFileName();
        this.originalImg = handledAnswerDTO.getOriginalImg();
        this.processedAt = processedAt;
    }
}
