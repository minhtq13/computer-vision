package com.example.multiplechoiceexam.dto.chapter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ChapterRequest {
    Long subjectId;

    List<ChapterSaveReqDTO> lstChapter = new ArrayList<>();

    public ChapterRequest() {
    }

    public Long getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Long subjectId) {
        this.subjectId = subjectId;
    }

    public List<ChapterSaveReqDTO> getLstChapter() {
        return lstChapter;
    }

    public void setLstChapter(List<ChapterSaveReqDTO> lstChapter) {
        this.lstChapter = lstChapter;
    }


    public static class ChapterSaveReqDTO {

        @SerializedName("title")
        @Expose
        private String title;
        @SerializedName("description")
        @Expose
        private String description;
        @SerializedName("orders")
        @Expose
        private Integer orders;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public Integer getOrders() {
            return orders;
        }

        public void setOrders(Integer orders) {
            this.orders = orders;
        }
    }
}
