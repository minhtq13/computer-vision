package com.example.multiplechoiceexam.dto.test.semeter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SemesterBox {
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("id")
    @Expose
    private Long id;
    @SerializedName("code")
    @Expose
    private String code;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

}
