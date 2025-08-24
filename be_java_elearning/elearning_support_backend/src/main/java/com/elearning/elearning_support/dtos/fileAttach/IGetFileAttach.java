package com.elearning.elearning_support.dtos.fileAttach;

import io.swagger.v3.oas.annotations.media.Schema;

public interface IGetFileAttach {

    @Schema(description = "File's id")
    Long getFileId();

    @Schema(description = "File's name")
    String getFileName();

    @Schema(description = "File's location")
    String getLocation();

    @Schema(description = "File's type")
    Integer getType();

}
