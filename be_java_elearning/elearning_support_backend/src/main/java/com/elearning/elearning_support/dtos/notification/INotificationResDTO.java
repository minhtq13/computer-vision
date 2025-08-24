package com.elearning.elearning_support.dtos.notification;

import java.util.Date;
import com.elearning.elearning_support.utils.DateUtils;
import com.fasterxml.jackson.annotation.JsonFormat;

public interface INotificationResDTO {

    Long getId();

    String getTitle();

    String getContent();

    Integer getContentType();

    @JsonFormat(pattern = DateUtils.FORMAT_DATE_HH_MM_YYYY_HH_MM, timezone = DateUtils.TIME_ZONE)
    Date getCreatedAt();

    Boolean getIsSeen();

    String getObjectIdentifier();

    Integer getObjectType();

    Boolean getIsNew();
}
