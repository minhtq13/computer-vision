package com.example.multiplechoiceexam.dto.subject;

public class SubjectCode {

    private Long subjectCode;
    private String subjectTitle;

    public SubjectCode(Long subjectCode, String subjectTitle) {
        this.subjectCode = subjectCode;
        this.subjectTitle = subjectTitle;
    }

    public Long getSubjectCode() {
        return subjectCode;
    }

    public String getSubjectTitle() {
        return subjectTitle;
    }

    @Override
    public String toString() {
        return subjectCode + ", " + subjectTitle;
    }
}
