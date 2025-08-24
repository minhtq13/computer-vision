package com.elearning.elearning_support.enums.question;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum QuestionLevelEnum {

    ALL(-1, "Tât cả"),

    EASY(0, "Dễ"),

    MEDIUM(1, "Trung bình"),

    HARD(2, "Khó");


    private final Integer level;

    private final String levelName;

    private static final Map<String, QuestionLevelEnum> mapLevelByEngName = new HashMap<>();

    private static final Map<String, QuestionLevelEnum> mapLevelByVnName = new HashMap<>();

    static {
        for (QuestionLevelEnum level : QuestionLevelEnum.values()) {
            mapLevelByEngName.put(level.toString().toLowerCase(), level);
            mapLevelByVnName.put(level.getLevelName().toLowerCase(), level);
        }
    }

    /**
     * get question level (english) by test name (for import)
     */
    public static QuestionLevelEnum getQuestionLevelByEngName(String levelName) {
        return mapLevelByEngName.get(levelName.toLowerCase());
    }

    /**
     * get question level (vietnamese) by test name (for import)
     */
    public static QuestionLevelEnum getQuestionLevelByVnName(String levelName) {
        return mapLevelByVnName.get(levelName.toLowerCase());
    }


}
