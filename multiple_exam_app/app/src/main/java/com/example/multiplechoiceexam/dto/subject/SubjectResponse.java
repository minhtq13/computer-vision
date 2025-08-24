package com.example.multiplechoiceexam.dto.subject;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SubjectResponse {
    @SerializedName("totalElements") private int totalElements;
    @SerializedName("totalPages") private int totalPages;
    @SerializedName("size") private int size;
    @SerializedName("content") private List<SubjectItem> content;
    @SerializedName("number") private int number;
    @SerializedName("sort") private SortInfo sort;
    @SerializedName("first") private boolean first;
    @SerializedName("last") private boolean last;
    @SerializedName("numberOfElements") private int numberOfElements;
    @SerializedName("pageable") private PageableInfo pageable;
    @SerializedName("empty") private boolean empty;

    public int getTotalElements() { return totalElements; }
    public int getTotalPages() { return totalPages; }
    public int getSize() { return size; }
    public List<SubjectItem> getContent() { return content; }
    public int getNumber() { return number; }
    public SortInfo getSort() { return sort; }
    public boolean isFirst() { return first; }
    public boolean isLast() { return last; }
    public int getNumberOfElements() { return numberOfElements; }
    public PageableInfo getPageable() { return pageable; }
    public boolean isEmpty() { return empty; }

    public static class SubjectItem {
        @SerializedName("id") private long id;
        @SerializedName("code") private String code;
        @SerializedName("departmentName") private String departmentName;
        @SerializedName("title") private String title;
        @SerializedName("credit") private int credit;

        public long getId() { return id; }
        public String getCode() { return code; }
        public String getDepartmentName() { return departmentName; }
        public String getTitle() { return title; }
        public int getCredit() { return credit; }
    }

    public static class SortInfo {
        @SerializedName("empty") private boolean empty;
        @SerializedName("sorted") private boolean sorted;
        @SerializedName("unsorted") private boolean unsorted;

        public boolean isEmpty() { return empty; }
        public boolean isSorted() { return sorted; }
        public boolean isUnsorted() { return unsorted; }
    }

    public static class PageableInfo {
        @SerializedName("offset") private int offset;
        @SerializedName("sort") private SortInfo sort;
        @SerializedName("pageNumber") private int pageNumber;
        @SerializedName("pageSize") private int pageSize;
        @SerializedName("unpaged") private boolean unpaged;
        @SerializedName("paged") private boolean paged;

        public int getOffset() { return offset; }
        public SortInfo getSort() { return sort; }
        public int getPageNumber() { return pageNumber; }
        public int getPageSize() { return pageSize; }
        public boolean isUnpaged() { return unpaged; }
        public boolean isPaged() { return paged; }
    }

}
