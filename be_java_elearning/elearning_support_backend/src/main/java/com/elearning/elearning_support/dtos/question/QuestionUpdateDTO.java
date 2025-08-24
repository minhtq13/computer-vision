package com.elearning.elearning_support.dtos.question;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.validation.constraints.NotNull;
import com.elearning.elearning_support.constants.message.messageConst.MessageConst;
import com.elearning.elearning_support.dtos.answer.AnswerReqDTO;
import com.elearning.elearning_support.enums.question.QuestionLevelEnum;
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
public class QuestionUpdateDTO {

    @Schema(description = "Id chương (Không bắt buộc với case cập nhật)")
    Long chapterId;

    @Schema(description = "Nội dung câu hỏi")
    @NotNull
    String content;

    @Schema(description = "Mức độ câu hỏi")
    @NotNull
    QuestionLevelEnum level;

    @Schema(description = "Id các ảnh đính kèm câu hỏi")
    Long[] lstImageId;

    @Schema(description = "Câu hỏi có nhiều phương án trả lời")
    Boolean isMultipleAnswers = Boolean.TRUE;

    @Schema(description = "Danh sách câu trả cập nhật")
    List<AnswerReqDTO> lstAnswer = new ArrayList<>();

    Boolean isChanged = false;

    @Schema(description = "Danh sách id của tag gắn với câu hỏi")
    Set<Long> tagIds = Collections.emptySet();

}
