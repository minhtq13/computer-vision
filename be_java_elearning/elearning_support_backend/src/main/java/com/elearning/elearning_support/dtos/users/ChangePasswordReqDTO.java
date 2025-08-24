package com.elearning.elearning_support.dtos.users;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import com.elearning.elearning_support.constants.message.messageConst.MessageConst;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChangePasswordReqDTO {

    @NotNull
    Long userId;

    @Size(max = 50, message = MessageConst.INVALID_FIELD_LENGTH)
    String oldPassword;

    @NotNull
    @NotEmpty
    @Size(min = 6, max = 100, message = MessageConst.INVALID_FIELD_LENGTH)
    String newPassword;

    @NotNull
    @NotEmpty
    String changeType; // RESET/UPDATE
}
