package com.example.multiplechoiceexam.dto.question;

import com.example.multiplechoiceexam.Utils.ObjectMapperUtil;
import com.example.multiplechoiceexam.dto.answer.AnswerResponse;
import com.example.multiplechoiceexam.dto.fileAttach.FileAttachDTO;

import java.util.ArrayList;
import java.util.List;

public class QuestionListDTO {

    public Long id;


    public String content;


    public String code;


    public Integer level;

    public List<FileAttachDTO> lstImage = new ArrayList<>();


    public List<AnswerResponse> lstAnswer = new ArrayList<>();

    public QuestionListDTO(IListQuestionDTO iListQuestionDTO) {
        this.id = iListQuestionDTO.getId();
        this.content = iListQuestionDTO.getContent();
        this.code = iListQuestionDTO.getCode();
        this.level = iListQuestionDTO.getLevel();
        this.lstImage = ObjectMapperUtil.listMapper(iListQuestionDTO.getLstImageJson(), FileAttachDTO.class);
        this.lstAnswer = ObjectMapperUtil.listMapper(iListQuestionDTO.getLstAnswerJson(), AnswerResponse.class);
    }

    public String getCorrectAnswer() {
        // Implement the logic to get the correct answer from the list of answers
        for (AnswerResponse answer : lstAnswer) {
            if (Boolean.TRUE.equals(answer.getCorrected())) {
                return answer.getContent(); // Assuming the correct answer is stored in the 'content' field
            }
        }
        return ""; // Return empty string if no correct answer is found
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public List<FileAttachDTO> getLstImage() {
        return lstImage;
    }

    public void setLstImage(List<FileAttachDTO> lstImage) {
        this.lstImage = lstImage;
    }

    public List<AnswerResponse> getLstAnswer() {
        return lstAnswer;
    }

    public void setLstAnswer(List<AnswerResponse> lstAnswer) {
        this.lstAnswer = lstAnswer;
    }
}
