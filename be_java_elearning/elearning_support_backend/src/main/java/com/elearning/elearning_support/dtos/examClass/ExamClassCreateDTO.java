package com.elearning.elearning_support.dtos.examClass;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import com.elearning.elearning_support.constants.message.messageConst.MessageConst;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ExamClassCreateDTO extends ExamClassSaveReqDTO {

    @Schema(description = "Mã lớp học tương ứng")
    Long courseId;

    @Schema(description = "Chọn các sinh viên từ lớp thi đã tồn tại")
    Long fromExamClassId;

}
