package com.elearning.elearning_support.enums.crobJob;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CronJobResultEnum {

    FAIL(0),
    SUCCESS(1);

    private final Integer type;
}
