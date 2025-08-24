package com.example.multiplechoiceexam.dto.test;

public class TestSetResDTO {
    private Integer duration;
    private String semester;
    private String testName;
    private Integer testId;
    private String subjectTitle;
    private String subjectCode;
    private Integer questionQuantity;
    private Integer testSetId;
    private String modifiedAt;
    private String createdAt;
    private String testSetCode;

    public TestSetResDTO() {
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public Integer getTestId() {
        return testId;
    }

    public void setTestId(Integer testId) {
        this.testId = testId;
    }

    public String getSubjectTitle() {
        return subjectTitle;
    }

    public void setSubjectTitle(String subjectTitle) {
        this.subjectTitle = subjectTitle;
    }

    public String getSubjectCode() {
        return subjectCode;
    }

    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }

    public Integer getQuestionQuantity() {
        return questionQuantity;
    }

    public void setQuestionQuantity(Integer questionQuantity) {
        this.questionQuantity = questionQuantity;
    }

    public Integer getTestSetId() {
        return testSetId;
    }

    public void setTestSetId(Integer testSetId) {
        this.testSetId = testSetId;
    }

    public String getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(String modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getTestSetCode() {
        return testSetCode;
    }

    public void setTestSetCode(String testSetCode) {
        this.testSetCode = testSetCode;
    }
}
