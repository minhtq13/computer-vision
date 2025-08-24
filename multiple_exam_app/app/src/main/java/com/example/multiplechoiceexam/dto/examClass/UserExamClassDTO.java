package com.example.multiplechoiceexam.dto.examClass;

import java.util.ArrayList;
import java.util.List;

public class UserExamClassDTO {

    Long examClassId;

    List<UserExamClassRoleDTO> lstParticipant = new ArrayList<>();

    public UserExamClassDTO() {
    }

    public Long getExamClassId() {
        return examClassId;
    }

    public void setExamClassId(Long examClassId) {
        this.examClassId = examClassId;
    }

    public List<UserExamClassRoleDTO> getLstParticipant() {
        return lstParticipant;
    }

    public void setLstParticipant(List<UserExamClassRoleDTO> lstParticipant) {
        this.lstParticipant = lstParticipant;
    }

    public static class UserExamClassRoleDTO {

        Long userId;

        UserExamClassRoleEnum role;

        public UserExamClassRoleDTO() {
        }

        public Long getUserId() {
            return userId;
        }

        public void setUserId(Long userId) {
            this.userId = userId;
        }

        public UserExamClassRoleEnum getRole() {
            return role;
        }

        public void setRole(UserExamClassRoleEnum role) {
            this.role = role;
        }
    }
}
