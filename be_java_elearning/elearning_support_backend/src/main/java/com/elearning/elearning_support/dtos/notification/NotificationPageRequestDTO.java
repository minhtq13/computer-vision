package com.elearning.elearning_support.dtos.notification;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationPageRequestDTO {
    private Integer page = 0;
    private Integer size = 10;
    private String sort = "id,desc";
}

