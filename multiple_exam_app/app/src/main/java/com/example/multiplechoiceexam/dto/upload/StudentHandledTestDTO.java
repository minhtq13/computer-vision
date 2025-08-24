package com.example.multiplechoiceexam.dto.upload;

import java.util.List;

public class StudentHandledTestDTO {
    String examClassCode;

    String studentCode;

    String testSetCode;

    String handledScoredImg;

    String originalImgFileName;

    String originalImg;

    List<HandledAnswerDTO> answers;

    public String getHandledScoredImg() {
        return handledScoredImg;
    }

    public void setHandledScoredImg(String handledScoredImg) {
        this.handledScoredImg = handledScoredImg;
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

    public StudentHandledTestDTO() {
    }

    public String getExamClassCode() {
        return examClassCode;
    }

    public void setExamClassCode(String examClassCode) {
        this.examClassCode = examClassCode;
    }

    public String getStudentCode() {
        return studentCode;
    }

    public void setStudentCode(String studentCode) {
        this.studentCode = studentCode;
    }


    public String getTestSetCode() {
        return testSetCode;
    }

    public void setTestSetCode(String testSetCode) {
        this.testSetCode = testSetCode;
    }

    public List<HandledAnswerDTO> getAnswers() {
        return answers;
    }

    public void setAnswers(List<HandledAnswerDTO> answers) {
        this.answers = answers;
    }
}
