package com.elearning.elearning_support.dtos.test.testSet;

import java.util.Date;
import java.util.List;
import org.springframework.beans.BeanUtils;
import com.elearning.elearning_support.dtos.studentTestSet.HandledAnswerDTO;
import com.elearning.elearning_support.dtos.studentTestSet.StudentHandledTestDTO;
import com.elearning.elearning_support.entities.studentTest.StudentTestSet;
import com.elearning.elearning_support.enums.fileAttach.FileStoredTypeEnum;
import com.elearning.elearning_support.utils.DateUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.firebase.database.DatabaseError;
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
public class ScoringPreviewItemDTO {

    @Schema(description = "Mã số thí sinh")
    String studentCode;

    @Schema(description = "Mã lớp thi")
    String examClassCode;

    @Schema(description = "Mã đề thi")
    String testSetCode;

    @Schema(description = "Đường dẫn file chấm bài thi")
    String handledScoredImg;

    @Schema(description = "Hình thức lưu bài thi đã chấm (tạm thời)")
    FileStoredTypeEnum storedType;

    @Schema(description = "Tên ảnh gốc")
    String originalImgFileName;

    @Schema(description = "Đường dẫn đến ảnh gốc")
    String originalImg;

    @Schema(description = "Số lượng câu hỏi trong đề")
    Integer numTestSetQuestions;

    @Schema(description = "Số câu hỏi đã khoanh")
    Integer numMarkedAnswers;

    @Schema(description = "Số câu trả lời đúng")
    Integer numCorrectAnswers;

    @Schema(description = "Số câu trả lời sai")
    Integer numWrongAnswers;

    @Schema(description = "Điểm tổng kết")
    Double totalScore;

    @Schema(description = "Chi tiết dữ liệu phiếu trả lời")
    List<HandledAnswerDTO> details;

    @Schema(description = "Thời gian xử lý bài thi")
    @JsonFormat(pattern = DateUtils.FORMAT_DATE_DD_MM_YY_HH_MM_SS, timezone = DateUtils.TIME_ZONE)
    Date processedAt;

    @Schema(description = "Item lưu vào DB khi cần")
    StudentTestSet studentTestSet;

    public ScoringPreviewItemDTO(StudentHandledTestDTO handledAnswerDTO){
        BeanUtils.copyProperties(handledAnswerDTO, this);
        this.details = handledAnswerDTO.getAnswers();
        this.processedAt = new Date();
    }
}
