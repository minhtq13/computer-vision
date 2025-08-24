package com.example.multiplechoiceexam.dto.test;

import com.example.multiplechoiceexam.dto.examClass.Pageable;
import com.example.multiplechoiceexam.dto.examClass.Sort;

import java.util.List;

public class TestClassResponse {
    private List<TestItem> content;
    private Pageable pageable;
    private Boolean last;
    private Integer totalPages;
    private Integer totalElements;
    private Integer size;
    private Integer number;
    private Sort sort;
    private Boolean first;
    private Integer numberOfElements;
    private Boolean empty;


    public TestClassResponse() {
    }

    public List<TestItem> getContent() {
        return content;
    }

    public void setContent(List<TestItem> content) {
        this.content = content;
    }

    public Pageable getPageable() {
        return pageable;
    }

    public void setPageable(Pageable pageable) {
        this.pageable = pageable;
    }

    public Boolean getLast() {
        return last;
    }

    public void setLast(Boolean last) {
        this.last = last;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public Integer getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(Integer totalElements) {
        this.totalElements = totalElements;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Sort getSort() {
        return sort;
    }

    public void setSort(Sort sort) {
        this.sort = sort;
    }

    public Boolean getFirst() {
        return first;
    }

    public void setFirst(Boolean first) {
        this.first = first;
    }

    public Integer getNumberOfElements() {
        return numberOfElements;
    }

    public void setNumberOfElements(Integer numberOfElements) {
        this.numberOfElements = numberOfElements;
    }

    public Boolean getEmpty() {
        return empty;
    }

    public void setEmpty(Boolean empty) {
        this.empty = empty;
    }


    public static class TestItem {
        private String name;
        private Integer id;
        private Integer duration;
        private String startTime;
        private String endTime;
        private String semester;
        private String subjectCode;
        private Integer questionQuantity;
        private String modifiedAt;
        private String createdAt;
        private String lstTestSetCode;
        private String subjectName;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Integer getDuration() {
            return duration;
        }

        public void setDuration(Integer duration) {
            this.duration = duration;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public String getSemester() {
            return semester;
        }

        public void setSemester(String semester) {
            this.semester = semester;
        }

        public String getSubjectCode() {
            return subjectCode;
        }

        public void setSubjectCode(String subjectCode) {
            this.subjectCode = subjectCode;
        }

        public Integer getQuestionQuantity() {
            return questionQuantity;
        }

        public void setQuestionQuantity(Integer questionQuantity) {
            this.questionQuantity = questionQuantity;
        }

        public String getModifiedAt() {
            return modifiedAt;
        }

        public void setModifiedAt(String modifiedAt) {
            this.modifiedAt = modifiedAt;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getLstTestSetCode() {
            return lstTestSetCode;
        }

        public void setLstTestSetCode(String lstTestSetCode) {
            this.lstTestSetCode = lstTestSetCode;
        }

        public String[] getLstTestSetCodeArray() {
            if (lstTestSetCode != null && !lstTestSetCode.isEmpty()) {
                return lstTestSetCode.split(",");
            } else {
                return new String[0]; // or handle it as needed when the string is empty
            }
        }

        public String getSubjectName() {
            return subjectName;
        }

        public void setSubjectName(String subjectName) {
            this.subjectName = subjectName;
        }

        private boolean expanded;

        public boolean isExpanded() {
            return expanded;
        }

        public void setExpanded(boolean expanded) {
            this.expanded = expanded;
        }
    }
}
