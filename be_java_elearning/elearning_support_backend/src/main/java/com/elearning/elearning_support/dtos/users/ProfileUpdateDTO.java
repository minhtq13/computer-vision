package com.elearning.elearning_support.dtos.users;

import java.util.Date;
import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import com.elearning.elearning_support.constants.ValidationConstants;
import com.elearning.elearning_support.constants.message.messageConst.MessageConst;
import com.elearning.elearning_support.enums.users.GenderEnum;
import com.elearning.elearning_support.enums.users.IdentityTypeEnum;
import com.elearning.elearning_support.utils.DateUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
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
public class ProfileUpdateDTO {

    @Schema(description = "Loại giấy tờ chứng thực cá nhân")
    IdentityTypeEnum identityType;

    @Schema(description = "Số giấy tờ chứng thực")
    String identificationNumber;

    @Schema(description = "Đường dẫn ảnh đại diện")
    String avatarPath;

    @Schema(description = "Id ảnh đại diện đã tải lên")
    Long avatarId;

    @NotBlank
    @NotNull
    @Size(min = 1, max = 20, message = MessageConst.INVALID_FIELD_LENGTH)
    String code;

    @NotNull
    @NotBlank
    @Schema(description = "Họ và tên đệm")
    String firstName;

    @NotNull
    @NotBlank
    @Schema(description = "Tên")
    String lastName;

    @Schema(description = "Giới tính")
    @NotNull
    GenderEnum genderType;

    @JsonFormat(pattern = DateUtils.FORMAT_DATE_DD_MM_YYYY_SLASH, timezone = DateUtils.TIME_ZONE)
    @Schema(description = "Ngày sinh")
    Date birthDate;

    @Schema(description = "Địa chỉ")
    String address;

    @Schema(description = "Số điện thoại")
    @Pattern(regexp = ValidationConstants.REGEX_PHONE)
    String phoneNumber;

    @Schema(description = "Loại người dùng (-1: ADMIN, 0: TEACHER, 1: STUDENT)")
    @NotNull
    Integer userType;

    @NotBlank
    @Schema(description = "Email")
    @Email(regexp = ValidationConstants.REGEX_EMAIL)
    String email;

}
