package com.elearning.elearning_support.enums.system;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SystemObjectEnum {

    ALL(-1), USER(0), EXAM_CLASS(100), SUBJECT(200), CHAPTER(300), QUESTION(400),
    TEST(500), TEST_SET(600), SCORED_EXAM_CLASS(700);

    private final Integer type;
}
