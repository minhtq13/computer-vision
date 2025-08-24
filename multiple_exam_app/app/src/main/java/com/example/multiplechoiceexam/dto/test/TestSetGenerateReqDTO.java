package com.example.multiplechoiceexam.dto.test;

public class TestSetGenerateReqDTO {
    Long testId;

    Integer numOfTestSet;

    public TestSetGenerateReqDTO() {
    }

    public Long getTestId() {
        return testId;
    }

    public void setTestId(Long testId) {
        this.testId = testId;
    }

    public Integer getNumOfTestSet() {
        return numOfTestSet;
    }

    public void setNumOfTestSet(Integer numOfTestSet) {
        this.numOfTestSet = numOfTestSet;
    }
}
