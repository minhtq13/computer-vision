package com.elearning.elearning_support.dtos.question;

import java.util.Date;
import java.util.List;
import com.elearning.elearning_support.dtos.common.CommonIdNameDTO;
import com.elearning.elearning_support.utils.DateUtils;
import com.elearning.elearning_support.utils.object.ObjectMapperUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

public interface IListQuestionDTO {

    Long getId();

    String getCode();

    String getContent();

    Integer getLevel();

    String getLstAnswerJson();

    Boolean getIsInTest();

    Boolean getIsMultipleAnswers();

    Boolean getIsNewest();

    @JsonFormat(pattern = DateUtils.FORMAT_DATE_DD_MM_YYYY_HH_MM_SS, timezone = DateUtils.TIME_ZONE)
    Date getModifiedAt();

    @JsonIgnore
    String getTagsJsonStr();

    default List<CommonIdNameDTO> getTags() {
        return ObjectMapperUtils.listMapper(this.getTagsJsonStr(), CommonIdNameDTO.class);
    }
}
