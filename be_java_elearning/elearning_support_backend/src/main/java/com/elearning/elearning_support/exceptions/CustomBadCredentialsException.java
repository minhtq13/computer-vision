package com.elearning.elearning_support.exceptions;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class CustomBadCredentialsException extends BaseException {

    public CustomBadCredentialsException(String code, String message, String fieldError, String... values) {
        super(code, message, fieldError, values);
    }

}
