package com.elearning.elearning_support.exceptions;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
public class ResourceExistedException extends BaseException {

    public ResourceExistedException(String resource, String message, String errorField, String... values){
        super(resource, message, errorField, values);
    }

}
