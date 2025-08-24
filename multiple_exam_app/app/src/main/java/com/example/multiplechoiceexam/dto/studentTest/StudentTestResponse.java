package com.example.multiplechoiceexam.dto.studentTest;

public class StudentTestResponse {

    private Integer id;

    private String examClassCode;

    private String testNo;

    private String state;

    private String testDate;

    private Double grade;

    private Integer mark;

    public StudentTestResponse() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getExamClassCode() {
        return examClassCode;
    }

    public void setExamClassCode(String examClassCode) {
        this.examClassCode = examClassCode;
    }

    public String getTestNo() {
        return testNo;
    }

    public void setTestNo(String testNo) {
        this.testNo = testNo;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getTestDate() {
        return testDate;
    }

    public void setTestDate(String testDate) {
        this.testDate = testDate;
    }

    public Double getGrade() {
        return grade;
    }

    public void setGrade(Double grade) {
        this.grade = grade;
    }

    public Integer getMark() {
        return mark;
    }

    public void setMark(Integer mark) {
        this.mark = mark;
    }
}
