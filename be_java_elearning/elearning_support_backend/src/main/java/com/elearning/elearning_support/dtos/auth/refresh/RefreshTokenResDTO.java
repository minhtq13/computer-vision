package com.elearning.elearning_support.dtos.auth.refresh;

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
public class RefreshTokenResDTO {

    @Schema(description = "Access token sau khi refresh")
    String accessToken;

    @Schema(description = "Refresh token sau khi refresh")
    String refreshToken;


}
