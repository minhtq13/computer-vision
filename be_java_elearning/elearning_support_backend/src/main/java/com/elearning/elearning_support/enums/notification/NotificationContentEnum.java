package com.elearning.elearning_support.enums.notification;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum NotificationContentEnum {

    // 1xxx : chấm thi
    SAVED_EXAM_CLASS_SCORING_RESULT_SUCCESSFULLY(1000, "Lưu kết quả chấm thi", "Hệ thống đã lưu thành công kết quả chấm thi của lớp thi %s"),
    SAVED_ONLINE_SCORING_RESULT_SUCCESSFULLY(1001, "Kết quả bài thi Online", "Đã có điểm bài thi Online %s")
    ;

    private final Integer type;

    private final String title;

    private final String content;
}
