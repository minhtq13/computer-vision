package com.elearning.elearning_support.dtos.examClass;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.util.ObjectUtils;
import com.elearning.elearning_support.constants.CharacterConstants;
import com.elearning.elearning_support.utils.DateUtils;
import com.elearning.elearning_support.utils.auth.AuthUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

public interface IExamClassDetailDTO extends ICommonExamClassDTO {

    @JsonFormat(pattern = DateUtils.FORMAT_DATE_HH_MM_YYYY_HH_MM, timezone = DateUtils.TIME_ZONE)
    Date getLastModifiedAt();

    String getLstStudentId();

    String getLstSupervisorId();

    @JsonIgnore
    String getLstLecturerIdStr();

    default List<Long> getLstLecturerId() {
        if (ObjectUtils.isEmpty(this.getLstLecturerIdStr())) {
            return Collections.emptyList();
        }
        return Arrays.stream(this.getLstLecturerIdStr().split(CharacterConstants.COMMA)).map(Long::valueOf)
                .collect(Collectors.toList());
    }

    String getLstTestSetCode();

    String getLstTestSetId();

    Boolean getExistedResult();

    default Boolean getHasScoringPermission() {
        return AuthUtils.isAdmin() || this.getLstLecturerId().contains(AuthUtils.getCurrentUserId());
    }

}
