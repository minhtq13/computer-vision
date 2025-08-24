package com.elearning.elearning_support.enums.test;

import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum StudentTestStatusEnum {

    ALL(-1, "Tất cả"),
    OPEN(0, "Chưa làm bài (mở)"),
    IN_PROGRESS(1, "Đang làm bài"),
    SUBMITTED(2, "Đã nộp bài"),
    DUE(3, "Quá hạn");

    private final Integer type;
    private final String description;

    public static StudentTestStatusEnum valueOf(Integer type) {
        if (Objects.isNull(type)){
            return null;
        }
        switch (type) {
            case -1:
                return ALL;
            case 0:
                return OPEN;
            case 1:
                return IN_PROGRESS;
            case 2:
                return SUBMITTED;
            case 3:
                return DUE;
            default:
                return null;
        }
    }
}
