package com.example.multiplechoiceexam.dto.test;

import java.util.List;

public class TestSetDetailResponse  {

    TestSetResDTO testSet;

    List<TestQuestionAnswerResDTO> lstQuestion;

    public TestSetDetailResponse() {
    }

    public TestSetResDTO getTestSet() {
        return testSet;
    }

    public void setTestSet(TestSetResDTO testSet) {
        this.testSet = testSet;
    }

    public List<TestQuestionAnswerResDTO> getLstQuestion() {
        return lstQuestion;
    }

    public void setLstQuestion(List<TestQuestionAnswerResDTO> lstQuestion) {
        this.lstQuestion = lstQuestion;
    }
}
