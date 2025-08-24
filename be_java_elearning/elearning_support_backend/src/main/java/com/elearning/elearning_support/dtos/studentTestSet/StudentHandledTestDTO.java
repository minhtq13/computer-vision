package com.elearning.elearning_support.dtos.studentTestSet;

import java.util.List;
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
public class StudentHandledTestDTO {

    @Schema(description = "Mã lớp thi")
    String examClassCode;

    @Schema(description = "Mã thí sinh")
    String studentCode;

    @Schema(description = "Mã đề thi")
    String testSetCode;

    @Schema(description = "Đường dẫn tuyệt đối file ảnh đã chấm")
    String handledScoredImg;

    @Schema(description = "Tên ảnh gốc")
    String originalImgFileName;

    @Schema(description = "Đường dẫn đến ảnh gốc")
    String originalImg;

    @Schema(description = "Đường dẫn tương đối đến file ảnh đã xử lý từ server (cho FE hiển thị)")
    String handledScoredFileName;

    @Schema(description = "Các câu trả lời của các câu hỏi")
    List<HandledAnswerDTO> answers;


}
