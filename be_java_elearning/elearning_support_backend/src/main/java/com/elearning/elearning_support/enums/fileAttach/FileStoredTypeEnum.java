package com.elearning.elearning_support.enums.fileAttach;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum FileStoredTypeEnum {

    INTERNAL_SERVER(0, "Lưu tại server"),
    EXTERNAL_SERVER(1, "Lưu tại cloud bên thứ 3");

    private final Integer type;
    private final String typeName;
}
