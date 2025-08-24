package com.elearning.elearning_support.enums.commons;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TimeIntervalEnum {

    DAY(1, "day"),
    WEEK(2, "week"),
    MONTH(3, "month"),
    QUARTER(4, "quarter"),
    YEAR(5, "year"),
    MINUTE(6, "minute"),
    HOUR(7, "hour"),
    SECOND(8, "second");

    private final Integer type;
    private final String textType;
}
