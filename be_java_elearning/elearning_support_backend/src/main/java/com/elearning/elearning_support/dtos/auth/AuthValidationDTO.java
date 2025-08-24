package com.elearning.elearning_support.dtos.auth;

import java.io.Serializable;
import java.util.Date;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthValidationDTO implements Serializable {

    String accessToken;

    String refreshToken;

    Integer status;

    Date issuedAt;

    String deviceToken;

}
