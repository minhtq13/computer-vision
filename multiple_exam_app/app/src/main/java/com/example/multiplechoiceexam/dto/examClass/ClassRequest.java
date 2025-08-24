package com.example.multiplechoiceexam.dto.examClass;

import java.util.List;

public class ClassRequest {

    private String roomName;
    private String examineTime;
    private Long testId;
    private List<Long> lstSupervisorId;
    private List<Long> lstStudentId;
    private String code;

    public ClassRequest() {
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

    public Long getTestId() {
        return testId;
    }

    public void setTestId(Long testId) {
        this.testId = testId;
    }

    public List<Long> getLstSupervisorId() {
        return lstSupervisorId;
    }

    public void setLstSupervisorId(List<Long> lstSupervisorId) {
        this.lstSupervisorId = lstSupervisorId;
    }

    public List<Long> getLstStudentId() {
        return lstStudentId;
    }

    public void setLstStudentId(List<Long> lstStudentId) {
        this.lstStudentId = lstStudentId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
