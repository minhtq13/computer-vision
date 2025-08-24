package com.elearning.elearning_support.enums.mail;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum MailSentStatusEnum {

    WAITING_SENT(0, "Chờ gửi"),
    SENT(1, "Đã gửi"),
    SENT_SUCCESS(2, "Gửi thành công"),
    SENT_FAIL(3, "Gửi thất bại");

    private final Integer status;
    private final String text;
}
