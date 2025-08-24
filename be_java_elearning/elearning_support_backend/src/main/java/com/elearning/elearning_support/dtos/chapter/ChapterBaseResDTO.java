package com.elearning.elearning_support.dtos.chapter;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChapterBaseResDTO {

    @Schema(description = "Id chương")
    Long id;

    @Schema(description = "Tiêu đều chương")
    String title;

    @Schema(description = "Mã chương")
    String code;

    @Schema(description = "Thứ tự chương trong môn học")
    Integer orders;

}
