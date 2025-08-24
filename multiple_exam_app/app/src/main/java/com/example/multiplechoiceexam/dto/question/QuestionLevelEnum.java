package com.example.multiplechoiceexam.dto.question;

import java.util.HashMap;
import java.util.Map;

public enum QuestionLevelEnum {
    ALL(-1, "Tất cả"),

    EASY(0, "Dễ"),

    MEDIUM(1, "Trung bình"),

    HARD(2, "Khó");

    private final Integer level;
    private final String levelName;

    private static final Map<String, QuestionLevelEnum> mapLevelByName = new HashMap<>();

    static {
        for (QuestionLevelEnum level : QuestionLevelEnum.values()) {
            mapLevelByName.put(level.toString().toLowerCase(), level);
        }
    }

    QuestionLevelEnum(int level, String levelName) {
        this.level = level;
        this.levelName = levelName;
    }

    public Integer getLevel() {
        return level;
    }

    public String getLevelName() {
        return levelName;
    }

    /**
     * Get question level by level name (for import)
     */
    public static QuestionLevelEnum getQuestionLevelByName(String levelName) {
        return mapLevelByName.get(levelName.toLowerCase());
    }
}
