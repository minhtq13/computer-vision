package com.elearning.elearning_support.dtos.notification;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
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
public class FCMTokenRegisterDTO {

    @NotNull
    @NotEmpty
    @Schema(description = "FCM token đăng ký")
    String fcmToken;

    Long userId;

}
