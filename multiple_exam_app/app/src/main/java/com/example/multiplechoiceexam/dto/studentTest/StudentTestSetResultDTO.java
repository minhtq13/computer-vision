package com.example.multiplechoiceexam.dto.studentTest;

public class StudentTestSetResultDTO {

    Long id;


    Long studentId;

    String studentName;
    String studentCode;
    Long testSetId;

    String testSetCode;

    Long examClassId;

    String examClassCode;

    Integer numTestSetQuestions;
    Integer numMarkedAnswers;

    Integer numCorrectAnswers = 0;

    Double totalPoints = 0.0;

    String handledSheetImg;

    public Integer getNumTestSetQuestions() {
        return numTestSetQuestions;
    }

    public void setNumTestSetQuestions(Integer numTestSetQuestions) {
        this.numTestSetQuestions = numTestSetQuestions;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStudentCode() {
        return studentCode;
    }

    public void setStudentCode(String studentCode) {
        this.studentCode = studentCode;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public Long getTestSetId() {
        return testSetId;
    }

    public void setTestSetId(Long testSetId) {
        this.testSetId = testSetId;
    }

    public String getTestSetCode() {
        return testSetCode;
    }

    public void setTestSetCode(String testSetCode) {
        this.testSetCode = testSetCode;
    }

    public Long getExamClassId() {
        return examClassId;
    }

    public void setExamClassId(Long examClassId) {
        this.examClassId = examClassId;
    }

    public String getExamClassCode() {
        return examClassCode;
    }

    public void setExamClassCode(String examClassCode) {
        this.examClassCode = examClassCode;
    }

    public Integer getNumMarkedAnswers() {
        return numMarkedAnswers;
    }

    public void setNumMarkedAnswers(Integer numMarkedAnswers) {
        this.numMarkedAnswers = numMarkedAnswers;
    }

    public Integer getNumCorrectAnswers() {
        return numCorrectAnswers;
    }

    public void setNumCorrectAnswers(Integer numCorrectAnswers) {
        this.numCorrectAnswers = numCorrectAnswers;
    }

    public Double getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(Double totalPoints) {
        this.totalPoints = totalPoints;
    }

    public String getHandledSheetImg() {
        return handledSheetImg;
    }

    public void setHandledSheetImg(String handledSheetImg) {
        this.handledSheetImg = handledSheetImg;
    }

    public StudentTestSetResultDTO(IStudentTestSetResultDTO iStudentTestSetResultDTO, Long examClassId, String examClassCode) {
        this.id = iStudentTestSetResultDTO.getId();
        this.studentId = iStudentTestSetResultDTO.getStudentId();
        this.studentName = iStudentTestSetResultDTO.getStudentName();
        this.testSetId = iStudentTestSetResultDTO.getTestSetId();
        this.testSetCode = iStudentTestSetResultDTO.getTestSetCode();
        this.examClassId = iStudentTestSetResultDTO.getExamClassId();
        this.examClassCode = iStudentTestSetResultDTO.getExamClassCode();
        this.numMarkedAnswers = iStudentTestSetResultDTO.getNumMarkedAnswers();
        this.numCorrectAnswers = iStudentTestSetResultDTO.getNumCorrectAnswers();
        this.totalPoints = iStudentTestSetResultDTO.getTotalPoints();
        this.handledSheetImg = iStudentTestSetResultDTO.getHandledSheetImg();
        this.examClassId = examClassId;
        this.examClassCode = examClassCode;
    }
}
