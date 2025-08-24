package com.elearning.elearning_support.dtos.question;


import java.util.List;
import java.util.Set;
import org.springframework.beans.BeanUtils;
import com.elearning.elearning_support.utils.StringUtils;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class QuestionAnswerDTO {

    Long id;

    String code;

    Integer level;

    List<Long> lstAnswerId;

    public QuestionAnswerDTO(IQuestionAnswerDTO iQuestionAnswerDTO){
        BeanUtils.copyProperties(iQuestionAnswerDTO, this);
        this.setLstAnswerId(StringUtils.convertStrLongToList(iQuestionAnswerDTO.getLstAnswerId()));
    }

}
