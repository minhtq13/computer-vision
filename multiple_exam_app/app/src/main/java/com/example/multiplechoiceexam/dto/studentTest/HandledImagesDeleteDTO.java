package com.example.multiplechoiceexam.dto.studentTest;

import java.util.HashSet;
import java.util.Set;

public class HandledImagesDeleteDTO {

    String examClassCode;

    Set<String> lstFileName = new HashSet<>();

    public HandledImagesDeleteDTO() {
    }

    public HandledImagesDeleteDTO(String examClassCode, Set<String> lstFileName) {
        this.examClassCode = examClassCode;
        this.lstFileName = lstFileName;
    }

    public String getExamClassCode() {
        return examClassCode;
    }

    public void setExamClassCode(String examClassCode) {
        this.examClassCode = examClassCode;
    }

    public Set<String> getLstFileName() {
        return lstFileName;
    }

    public void setLstFileName(Set<String> lstFileName) {
        this.lstFileName = lstFileName;
    }
}
