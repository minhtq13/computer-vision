package com.elearning.elearning_support.dtos.chapter;


import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import com.elearning.elearning_support.constants.message.messageConst.MessageConst;
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
public class ChapterSaveReqDTO {

    @NotNull
    @NotBlank
    @Size(min = 1, max = 255, message = MessageConst.INVALID_FIELD_LENGTH)
    @Schema(description = "Tiêu đề của chương")
    String title;

    @Schema(description = "Mô tả chương")
    String description;

    @NotNull
    Integer orders;

}
