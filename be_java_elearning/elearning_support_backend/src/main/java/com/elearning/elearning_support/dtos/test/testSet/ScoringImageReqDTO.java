package com.elearning.elearning_support.dtos.test.testSet;

import java.util.Collections;
import java.util.Set;
import javax.validation.constraints.NotNull;
import com.elearning.elearning_support.constants.message.messageConst.MessageConst;
import com.elearning.elearning_support.constants.message.messageConst.MessageConst.Validation;
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
public class ScoringImageReqDTO {

    @Schema(description = "Mã lớp thi tương ứng")
    String examClassCode;


    @Schema(description = "Ưu tiên tốc độ (accuracy) hay ưu tiên độ chính xác (speed)")
    String mode;

    @Schema(description = "Số lượng câu cần chấm")
    Integer numberAnswers; // "ALL" or a specific number of answers

    @NotNull(message = Validation.NOT_NULL)
    @Schema(description = "Tên các file ảnh có trong folder lớp thi: ALL -> toàn bộ")
    Set<String> selectedImages = Collections.singleton("ALL");

}
