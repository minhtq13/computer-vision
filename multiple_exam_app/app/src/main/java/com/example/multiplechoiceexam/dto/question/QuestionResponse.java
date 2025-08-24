package com.example.multiplechoiceexam.dto.question;

import com.example.multiplechoiceexam.Utils.ObjectMapperUtil;
import com.example.multiplechoiceexam.dto.answer.AnswerResponse;
import com.example.multiplechoiceexam.dto.fileAttach.FileAttachDTO;

import java.util.ArrayList;
import java.util.List;

public class QuestionResponse {

    Long id;

    String content;

    String code;
    Integer level;

    Long subjectId;

    String subjectTitle;

    Long chapterId;

    String chapterTitle;

    List<AnswerResponse> lstAnswer = new ArrayList<>();

    List<FileAttachDTO> lstImage = new ArrayList<>();

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public QuestionResponse(IQuestionDetailsDTO iQuestionDetailsDTO) {
        this.id = iQuestionDetailsDTO.getId();
        this.content = iQuestionDetailsDTO.getContent();
        this.code = iQuestionDetailsDTO.getCode();
        this.subjectId=iQuestionDetailsDTO.getSubjectId();
        this.chapterTitle=iQuestionDetailsDTO.getChapterTitle();
        this.subjectTitle=iQuestionDetailsDTO.getSubjectTitle();
        this.chapterId=iQuestionDetailsDTO.getChapterId();
        this.lstImage = ObjectMapperUtil.listMapper(iQuestionDetailsDTO.getLstImageJson(), FileAttachDTO.class);
        this.lstAnswer = ObjectMapperUtil.listMapper(iQuestionDetailsDTO.getLstAnswerJson(), AnswerResponse.class);
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

    public Long getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Long subjectId) {
        this.subjectId = subjectId;
    }

    public String getSubjectTitle() {
        return subjectTitle;
    }

    public void setSubjectTitle(String subjectTitle) {
        this.subjectTitle = subjectTitle;
    }

    public Long getChapterId() {
        return chapterId;
    }

    public void setChapterId(Long chapterId) {
        this.chapterId = chapterId;
    }

    public String getChapterTitle() {
        return chapterTitle;
    }

    public void setChapterTitle(String chapterTitle) {
        this.chapterTitle = chapterTitle;
    }

    public List<AnswerResponse> getLstAnswer() {
        return lstAnswer;
    }

    public void setLstAnswer(List<AnswerResponse> lstAnswer) {
        this.lstAnswer = lstAnswer;
    }

    public List<FileAttachDTO> getLstImage() {
        return lstImage;
    }

    public void setLstImage(List<FileAttachDTO> lstImage) {
        this.lstImage = lstImage;
    }
}
