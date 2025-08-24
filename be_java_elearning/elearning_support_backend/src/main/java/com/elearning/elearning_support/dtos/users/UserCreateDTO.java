package com.elearning.elearning_support.dtos.users;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreateDTO extends UserSaveReqDTO {

    @Schema(description = "Tên đăng nhập")
    String username;

    @Schema(description = "Mật khẩu đăng nhập")
    String password;

}
