package com.elearning.elearning_support.enums.commons;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum StatusEnum {

    DISABLED(0), ENABLED(1);

    private final Integer status;

}
