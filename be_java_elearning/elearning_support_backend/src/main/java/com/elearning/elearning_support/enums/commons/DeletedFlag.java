package com.elearning.elearning_support.enums.commons;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum DeletedFlag {
    DELETED(0), NOT_YET_DELETED(1);

    private final int value;
}
