package com.example.multiplechoiceexam.dto.test;

import java.io.Serializable;

public class ClassTestInfo implements Serializable {
    private String testCode;
    private Long testId;

    public ClassTestInfo(String testCode, Long testId) {
        this.testCode = testCode;
        this.testId = testId;
    }

    public String getTestCode() {
        return testCode;
    }

    public void setTestCode(String testCode) {
        this.testCode = testCode;
    }

    public Long getTestId() {
        return testId;
    }

    public void setTestId(Long testId) {
        this.testId = testId;
    }
}

