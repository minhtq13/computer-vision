package com.elearning.elearning_support.enums.system;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SpringProfileEnum {

    DEV("dev"), DEV_MPEC("devmpec"), LOCAL("local"), PRODUCTION("production");


    private final String name;
}
