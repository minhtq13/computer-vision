package com.example.multiplechoiceexam.dto.examClass;

public class ExamClass {
    private Long id;
    private Integer duration;
    private String code;
    private Long subjectId;
    private Long testId;
    private String roomName;
    private String examineTime;
    private Long semesterId;
    private String subjectTitle;
    private String semester;
    private String examineDate;
    private String testName;
    private Integer numberOfSupervisors;
    private Integer numberOfStudents;

    public ExamClass() {
    }

    public String getExamineDate() {
        return examineDate;
    }

    public void setExamineDate(String examineDate) {
        this.examineDate = examineDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Long getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Long subjectId) {
        this.subjectId = subjectId;
    }

    public Long getTestId() {
        return testId;
    }

    public void setTestId(Long testId) {
        this.testId = testId;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getExamineTime() {
        return examineTime;
    }

    public void setExamineTime(String examineTime) {
        this.examineTime = examineTime;
    }

    public Long getSemesterId() {
        return semesterId;
    }

    public void setSemesterId(Long semesterId) {
        this.semesterId = semesterId;
    }

    public String getSubjectTitle() {
        return subjectTitle;
    }

    public void setSubjectTitle(String subjectTitle) {
        this.subjectTitle = subjectTitle;
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

    public Integer getNumberOfSupervisors() {
        return numberOfSupervisors;
    }

    public void setNumberOfSupervisors(Integer numberOfSupervisors) {
        this.numberOfSupervisors = numberOfSupervisors;
    }

    public Integer getNumberOfStudents() {
        return numberOfStudents;
    }

    public void setNumberOfStudents(Integer numberOfStudents) {
        this.numberOfStudents = numberOfStudents;
    }
}
