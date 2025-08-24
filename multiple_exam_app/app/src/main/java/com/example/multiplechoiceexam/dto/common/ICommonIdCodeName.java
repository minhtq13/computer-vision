package com.example.multiplechoiceexam.dto.common;

public class ICommonIdCodeName {

    String name;

    Long id;

    String code;

    public ICommonIdCodeName() {
    }

    public ICommonIdCodeName(String name, Long id, String code) {
        this.name = name;
        this.id = id;
        this.code = code;
    }

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
