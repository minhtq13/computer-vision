package com.elearning.elearning_support.exceptions;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class BadRequestException extends BaseException {

    public BadRequestException(String code, String message, String fieldError, String... values) {
        super(code, message, fieldError, values);
    }
}
