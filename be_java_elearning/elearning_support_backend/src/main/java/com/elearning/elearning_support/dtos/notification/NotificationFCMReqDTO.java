package com.elearning.elearning_support.dtos.notification;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NotificationFCMReqDTO {

    Long targetUserId;

    @Schema(description = "Tiêu đề")
    String title;

    @Schema(description = "Nội dung")
    String content;

    @Schema(description = "FCM Token")
    String fcmToken;

}
