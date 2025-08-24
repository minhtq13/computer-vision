package com.elearning.elearning_support.enums.importFile;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ImportResponseEnum {

    SUCCESS(0, "Import successfully"),
    EXIST_INVALID_DATA(1, "Exists invalid data"),
    IO_ERROR(2, "IO error"),
    UNKNOWN_ERROR(3, "Unknown error");

    private final Integer status;

    private final String message;


}
