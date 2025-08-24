package com.elearning.elearning_support.enums.notification;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum NotificationObjectTypeEnum {

    USER(1), EXAM_CLASS(2), ONLINE_TEST_DETAIL(3);

    private final Integer type;

}
