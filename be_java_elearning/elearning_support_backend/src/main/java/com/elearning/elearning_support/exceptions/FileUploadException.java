package com.elearning.elearning_support.exceptions;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
public class FileUploadException extends BaseException {

    public FileUploadException(String code, String resource, String message){
        super(code, resource, message);
    }
}
