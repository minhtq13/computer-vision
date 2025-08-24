package com.elearning.elearning_support.enums.fileAttach;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum FileTypeEnum {

    AVATAR(0, "File avatar/icon"),
    IMAGE(1, "File ảnh"),

    DOCUMENT(2, "File tài liệu");

    private final Integer type;
    private final String typeName;
}
