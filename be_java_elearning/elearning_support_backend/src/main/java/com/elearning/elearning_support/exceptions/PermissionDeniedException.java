package com.elearning.elearning_support.exceptions;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
public class PermissionDeniedException extends BaseException {

    public PermissionDeniedException(String code, String resource, String message) {
        super(code, resource, message);
    }
}
