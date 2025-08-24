package com.elearning.elearning_support.enums.test;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TestTypeEnum {



    ALL(-1), OFFLINE(0), ONLINE(1);

    private final Integer type;
}
