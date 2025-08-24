package com.elearning.elearning_support.enums.mail;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MailTemplateEnum {

    EMAIL_EXAM_CLASS_RESULT("EMAIL_EXAM_CLASS_RESULT", "Danh sách và kết quả lớp thi %s");


    private final String code;
    private final String subject;
}
