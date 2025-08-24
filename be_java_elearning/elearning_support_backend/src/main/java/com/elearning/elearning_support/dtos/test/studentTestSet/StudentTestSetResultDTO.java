package com.elearning.elearning_support.dtos.test.studentTestSet;

import org.springframework.beans.BeanUtils;
import com.elearning.elearning_support.constants.CharacterConstants;
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
public class StudentTestSetResultDTO {

    @Schema(description = "Id bản ghi student_test_set")
    Long id;

    @Schema(description = "Id thí sinh")
    Long studentId;

    @Schema(description = "Tên thí sinh")
    String studentName;

    @Schema(description = "MSSV")
    String studentCode;

    @Schema(description = "Id đề thi")
    Long testSetId;

    @Schema(description = "Mã đề thi")
    String testSetCode;

    @Schema(description = "Id lớp thi")
    Long examClassId;

    @Schema(description = "Mã lớp thi")
    String examClassCode;

    // result
    @Schema(description = "Số câu hỏi trong đề")
    Integer numTestSetQuestions = 0;

    @Schema(description = "Số câu đã tô phương án trả lời")
    Integer numMarkedAnswers = 0;

    @Schema(description = "Số câu trả lời đúng")
    Integer numCorrectAnswers = 0;

    @Schema(description = "Các câu trả lời đúng")
    String correctAnswersStr = CharacterConstants.BLANK;

    @Schema(description = "Tổng số điểm")
    Double totalPoints = 0.0;

    @Schema(description = "Đường dẫn ảnh phiếu trả lời")
    String handledSheetImg;

    @Schema(description = "Hình thức lưu trữ ảnh phiếu trả lời")
    Integer storedType;

    @Schema(description = "Trạng thái làm bài thi dạng số")
    Integer status;

    @Schema(description = "Trạng thái làm bài thi dạng label")
    String statusLabel;

    public StudentTestSetResultDTO(IStudentTestSetResultDTO iStudentTestSetResultDTO, Long examClassId, String examClassCode) {
        BeanUtils.copyProperties(iStudentTestSetResultDTO, this);
        this.examClassId = examClassId;
        this.examClassCode = examClassCode;
    }

    public StudentTestSetResultDTO(Long studentId, String studentName, String studentCode) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.studentCode = studentCode;
    }
}
