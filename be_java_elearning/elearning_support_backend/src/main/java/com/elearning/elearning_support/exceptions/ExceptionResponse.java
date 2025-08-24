package com.elearning.elearning_support.exceptions;

import com.elearning.elearning_support.utils.DateUtils;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ExceptionResponse {

    private String code;

    private Integer status;

    private String message;

    private String entity;

    private String fieldError;

    private Long timestamp = DateUtils.getCurrentDateTime().getTime();

    private String values;

    public ExceptionResponse(String code, Integer status, String message, String fieldError, String values) {
        this.code = code;
        this.status = status;
        this.message = message;
        this.fieldError = fieldError;
        this.values = values;
    }

    public ExceptionResponse(String code, Integer status, String message, String values) {
        this.code = code;
        this.status = status;
        this.message = message;
        this.values = values;
    }
}
