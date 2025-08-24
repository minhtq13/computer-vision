package com.elearning.elearning_support.dtos.users;

import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.elearning.elearning_support.entities.role.Role;
import com.elearning.elearning_support.utils.DateUtils;
import com.elearning.elearning_support.utils.object.ObjectMapperUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class GetAllUserResponseDTO {

    private Long roleBaseId;
    private Long id;
    private String code;
    private String gender;
    private String firstName;
    private String lastName;

    @JsonFormat(pattern = DateUtils.FORMAT_DATE_DD_MM_YYYY_SLASH, timezone = DateUtils.TIME_ZONE)
    private Date birthDate;

    private String phoneNumber;
    private String email;
    private Integer courseNum;

    @JsonIgnore
    private String roleInfoJson;

    private List<Role> roleInfo;

    public GetAllUserResponseDTO(GetAllUserDTO getAllUserDTO) {
        this.roleInfo = ObjectMapperUtils.listMapper(getAllUserDTO.getRoleInfoJson(), Role.class);
        BeanUtils.copyProperties(getAllUserDTO, this);
    }

}
