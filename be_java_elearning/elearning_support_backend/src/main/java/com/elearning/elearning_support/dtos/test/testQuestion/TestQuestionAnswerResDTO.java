package com.elearning.elearning_support.dtos.test.testQuestion;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.BeanUtils;
import com.elearning.elearning_support.dtos.fileAttach.FileAttachDTO;
import com.elearning.elearning_support.dtos.test.testSet.TestSetAnswerResDTO;
import com.elearning.elearning_support.utils.object.ObjectMapperUtils;
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
public class TestQuestionAnswerResDTO {

    @Schema(description = "Id bảng question")
    Long id; // questionId

    @Schema(description = "Id bảng test_set_question")
    Long testSetQuestionId; // testSetQuestionId

    String content;

    Integer level;

    @Schema(description = "File ảnh của câu hỏi")
    List<FileAttachDTO> images = new ArrayList<>();

    Integer questionNo;

    @Schema(description = "Câu hỏi có nhiều phương án?")
    Boolean isMultipleAnswers;

    @Schema(description = "Danh sách câu trả lời kèm theo thứ tự")
    List<TestSetAnswerResDTO> answers;

    @Schema(description = "Câu hỏi đã chọn đáp án hay chưa")
    Boolean isChecked;

    public TestQuestionAnswerResDTO(ITestQuestionAnswerResDTO iTestQuestionAnswerResDTO, Boolean isBlindedCorrectAns) {
        BeanUtils.copyProperties(iTestQuestionAnswerResDTO, this);
        // Mapping to object
        this.answers = ObjectMapperUtils.listMapper(iTestQuestionAnswerResDTO.getLstAnswerJson(), TestSetAnswerResDTO.class);
        // hide correct answers
        if (isBlindedCorrectAns) {
            this.answers.forEach(answer -> answer.setIsCorrect(null));
        }
        // Map question images
        this.images = ObjectMapperUtils.listMapper(iTestQuestionAnswerResDTO.getLstImageJson(), FileAttachDTO.class);
    }

}
