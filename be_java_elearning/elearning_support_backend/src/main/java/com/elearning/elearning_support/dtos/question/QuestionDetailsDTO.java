package com.elearning.elearning_support.dtos.question;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.beans.BeanUtils;
import com.elearning.elearning_support.dtos.answer.AnswerResDTO;
import com.elearning.elearning_support.dtos.fileAttach.FileAttachDTO;
import com.elearning.elearning_support.utils.StringUtils;
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
public class QuestionDetailsDTO {

    @Schema(description = "Id câu hỏi")
    Long id;

    @Schema(description = "Nội dung câu hỏi")
    String content;

    @Schema(description = "Mã số câu hỏi")
    String code;

    @Schema(description = "Mức độ câu hỏi")
    Integer level;

    @Schema(description = "Id môn học")
    Long subjectId;

    @Schema(description = "Tiêu đề môn học")
    String subjectTitle;

    @Schema(description = "Id chương")
    Long chapterId;

    @Schema(description = "Tiêu đề chương")
    String chapterTitle;

    @Schema(description = "Danh sách đáp án trả lời")
    List<AnswerResDTO> lstAnswer = new ArrayList<>();

    @Schema(description = "File ảnh của câu hỏi")
    List<FileAttachDTO> lstImage = new ArrayList<>();

    @Schema(description = "Cho chọn nhiều đáp án")
    Boolean isMultipleAnswers = false;

    @Schema(description = "Danh sách id tag")
    Set<Long> tagIds = new HashSet<>();

    public QuestionDetailsDTO(IQuestionDetailsDTO iQuestionDetailsDTO) {
        BeanUtils.copyProperties(iQuestionDetailsDTO, this);
        // map and sort answer
        this.lstAnswer = ObjectMapperUtils.listMapper(iQuestionDetailsDTO.getLstAnswerJson(), AnswerResDTO.class);
        this.lstAnswer.sort(Comparator.comparing(AnswerResDTO::getId));
        this.tagIds = StringUtils.convertStrLongToSet(iQuestionDetailsDTO.getTagIdsStr());
    }

}
