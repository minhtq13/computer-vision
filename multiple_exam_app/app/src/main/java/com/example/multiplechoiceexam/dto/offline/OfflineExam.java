package com.example.multiplechoiceexam.dto.offline;

import java.util.List;

public class OfflineExam {
    private String classCode;

    private String studentCode;

    private Integer testNo;

    private List<OfflineAnswer> answers;

    public static class OfflineAnswer {

        private Integer questionNo;

        private String isSelected;

    }
}
