package com.elearning.elearning_support.enums.users;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum UserStatusEnum {

    // (0: Đang hoạt động, 1: Tạm dừng hoạt động, 2: Tắt hoạt động)
    ACTIVE(0, "Đang hoạt động"),
    INACTIVE(1, "Tạm dừng hoạt động"),
    DISABLED(2, "Tắt hoạt động");

    private final Integer status;
    private final String statusText;
}
