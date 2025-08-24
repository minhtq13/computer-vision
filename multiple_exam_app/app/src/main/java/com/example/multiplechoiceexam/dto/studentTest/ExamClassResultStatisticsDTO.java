package com.example.multiplechoiceexam.dto.studentTest;

import java.util.List;

public class ExamClassResultStatisticsDTO {
    List<StudentTestSetResultDTO> results;

    public ExamClassResultStatisticsDTO() {
    }

    public List<StudentTestSetResultDTO> getResults() {
        return results;
    }

    public void setResults(List<StudentTestSetResultDTO> results) {
        this.results = results;
    }
}
