package com.example.multiplechoiceexam.dto.question;

import okhttp3.ResponseBody;

public class CustomInputStreamResource {

    ResponseBody resource;


    public ResponseBody getResource() {
        return resource;
    }

    public void setResource(ResponseBody resource) {
        this.resource = resource;
    }

    public CustomInputStreamResource() {
    }
}
