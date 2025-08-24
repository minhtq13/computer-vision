package com.elearning.elearning_support.dtos.users.student;

import java.util.Date;
import javax.validation.constraints.NotNull;
import org.springframework.beans.BeanUtils;
import com.elearning.elearning_support.dtos.users.IGetUserListDTO;
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
public class StudentExportDTO {

    @Schema(description = "Mã GV/HSSV ứng với loại người dùng")
    String code;

    @Schema(description = "Họ và tên đệm")
    String firstName;

    @Schema(description = "Tên")
    String lastName;

    @Schema(description = "Giới tính (ENG)")
    String gender;

    @Schema(description = "Giới tính (VN)")
    String genderName;

    @JsonFormat(pattern = DateUtils.FORMAT_DATE_DD_MM_YYYY_SLASH, timezone = DateUtils.TIME_ZONE)
    @Schema(description = "Ngày sinh")
    Date birthDate;

    @Schema(description = "Số điện thoại")
    @NotNull
    String phoneNumber;

    @Schema(description = "Email")
    String email;

    @Schema(description = "Khóa")
    Integer courseNum;

    @Schema(description = "Đơn vị quản lý")
    String departmentName;

    public StudentExportDTO(IGetUserListDTO iGetUserListDTO){
        BeanUtils.copyProperties(iGetUserListDTO, this);
    }
}
