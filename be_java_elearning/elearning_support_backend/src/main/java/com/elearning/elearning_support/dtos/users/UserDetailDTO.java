package com.elearning.elearning_support.dtos.users;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import javax.validation.constraints.NotNull;
import org.springframework.beans.BeanUtils;

import com.elearning.elearning_support.entities.role.Role;
import com.elearning.elearning_support.utils.DateUtils;
import com.elearning.elearning_support.utils.StringUtils;
import com.elearning.elearning_support.utils.object.ObjectMapperUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDetailDTO {

    @Schema(description = "Id user")
    Long id;

    @Schema(description = "Mã GV/HSSV ứng với loại người dùng")
    String code;

    @Schema(description = "Loại giấy tờ chứng thực cá nhân")
    String identityType;

    @Schema(description = "Số giấy tờ chứng thực")
    String identificationNum;

    @Schema(description = "Đường dẫn ảnh đại diện")
    String avatarPath;

    @Schema(description = "Id ảnh đại diện đã tải lên")
    Long avatarId;

    @Schema(description = "Hình thức lưu dữ liệu ảnh đại diện")
    Integer avatarStoredType;

    @Schema(description = "Họ và tên")
    String name;

    @Schema(description = "Họ và tên đệm")
    String firstName;

    @Schema(description = "Tên")
    String lastName;

    @Schema(description = "Tên đăng nhập người dùng")
    String username;

    @Schema(description = "Giới tính")
    String gender;

    @JsonFormat(pattern = DateUtils.FORMAT_DATE_DD_MM_YYYY_SLASH, timezone = DateUtils.TIME_ZONE)
    @Schema(description = "Ngày sinh")
    Date birthDate;

    @Schema(description = "Địa chỉ")
    String address;

    @Schema(description = "Số điện thoại")
    @NotNull
    String phoneNumber;

    @Schema(description = "Email")
    String email;

    @Schema(description = "Id phòng ban / đơn vị")
    @NotNull
    Set<Long> departmentIds;

    @Schema(description = "Tên phòng ban / đơn vị trực thuộc")
    String departmentName;

    @Schema(description = "Loại tài khoản")
    Integer userType;

    @Schema(description = "Khóa HS/SV nếu loại người dùng là HSSV")
    Integer courseNum;

    @Schema(description = "Danh sách vai trò của user")
    List<Role> roles = new ArrayList<>();

    @Schema(description = "Metadata của người dùng")
    Object metaData;

    @Schema(description = "FCM Token")
    String fcmToken;

    public UserDetailDTO(IGetDetailUserDTO iGetDetailUserDTO) {
        BeanUtils.copyProperties(iGetDetailUserDTO, this);
        this.departmentIds = StringUtils.convertStrLongToSet(iGetDetailUserDTO.getLstDepartmentId());
        this.roles = ObjectMapperUtils.listMapper(iGetDetailUserDTO.getRoleJson(), Role.class);
        this.metaData = ObjectMapperUtils.mapping(iGetDetailUserDTO.getMetaData(), Object.class);
    }

}
