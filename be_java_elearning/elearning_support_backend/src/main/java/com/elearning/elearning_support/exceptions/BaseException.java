package com.elearning.elearning_support.exceptions;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BaseException extends RuntimeException {

    String code;

    String message;

    String resource;

    String fieldError;

    String values;

    public BaseException(String code, String message, String fieldError, String... values) {
        this.code = code;
        this.message = message;
        this.fieldError = fieldError;
        this.values = String.join(",", values);
    }

    public BaseException(String code, String resource, String message){
        this.code = code;
        this.message = message;
        this.resource = resource;
    }
}
