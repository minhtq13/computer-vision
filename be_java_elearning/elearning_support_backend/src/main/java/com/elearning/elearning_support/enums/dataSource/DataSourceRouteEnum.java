package com.elearning.elearning_support.enums.dataSource;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DataSourceRouteEnum {
    MASTER(0), SLAVE(1);

    private final Integer type;
}
