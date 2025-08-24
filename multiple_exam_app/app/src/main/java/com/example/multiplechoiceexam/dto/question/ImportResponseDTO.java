package com.example.multiplechoiceexam.dto.question;

import java.util.ArrayList;
import java.util.List;

public class ImportResponseDTO {
    String message;

    Integer status;

    List<RowErrorDTO> errorRows = new ArrayList<>();

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<RowErrorDTO> getErrorRows() {
        return errorRows;
    }

    public void setErrorRows(List<RowErrorDTO> errorRows) {
        this.errorRows = errorRows;
    }

    public static class RowErrorDTO {

        Integer rowNum;

        Object rawImportData;

        List<String> errorCauseList = new ArrayList<>();

        public Integer getRowNum() {
            return rowNum;
        }

        public void setRowNum(Integer rowNum) {
            this.rowNum = rowNum;
        }

        public Object getRawImportData() {
            return rawImportData;
        }

        public void setRawImportData(Object rawImportData) {
            this.rawImportData = rawImportData;
        }

        public List<String> getErrorCauseList() {
            return errorCauseList;
        }

        public void setErrorCauseList(List<String> errorCauseList) {
            this.errorCauseList = errorCauseList;
        }
    }

}
