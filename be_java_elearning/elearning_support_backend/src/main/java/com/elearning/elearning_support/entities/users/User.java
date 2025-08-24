package com.elearning.elearning_support.entities.users;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.springframework.beans.BeanUtils;
import org.springframework.util.ObjectUtils;
import com.elearning.elearning_support.dtos.users.importUser.CommonUserImportDTO;
import com.elearning.elearning_support.entities.BaseEntity;
import com.elearning.elearning_support.entities.department.Department;
import com.elearning.elearning_support.entities.role.Role;
import com.elearning.elearning_support.enums.commons.StatusEnum;
import com.elearning.elearning_support.enums.users.GenderEnum;
import com.elearning.elearning_support.utils.DateUtils;
import com.elearning.elearning_support.utils.StringUtils;
import com.elearning.elearning_support.utils.auth.AuthUtils;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class User extends BaseEntity implements Serializable {

    private static final long serialVersionUID = -1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column(name = "code", unique = true, nullable = false)
    private String code;

    @Column(name = "gender", nullable = false)
    private Integer gender;

    @Column(name = "identification_number")
    private String identificationNumber;

    @Column(name = "identity_type")
    private Integer identityType;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "birth_date")
    private Date birthDate;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "address")
    private String address;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Transient
    private String passwordRaw;

    @Column(name = "avatar_id")
    private Long avatarId;

    @Column(name = "status", insertable = false)
    private Integer status;

    @Column(name = "user_type", nullable = false)
    private Integer userType;

    @Transient
    private Long departmentId;

    @Column(name = "fcm_token")
    private String fcmToken;

    @Column(name = "activation_key")
    String activationKey;

    @Column(name = "created_source", insertable = false)
    private Integer createdSource;

    @Column(name = "user_uuid", insertable = false, updatable = false)
    private UUID userUUID;

    @Column(name = "deleted_flag", insertable = false)
    private Integer deletedFlag;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "users_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    Set<Role> roles = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "users_departments", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "department_id"))
    Set<Department> departments = new HashSet<>();

    @Column(name = "meta_data", columnDefinition = "jsonb")
    @Type(type = "jsonb")
    Object metaData;

    /**
     * Hàm tạo user khi import người dùng
     */
    public User(CommonUserImportDTO importDTO) {
        BeanUtils.copyProperties(importDTO, this);
        this.setCreatedAt(new Date());
        List<String> nameParts = StringUtils.parseNameParts(importDTO.getFullNameRaw());
        this.lastName = !nameParts.isEmpty() ? nameParts.get(0) : "";
        this.firstName = nameParts.size() == 2 ? nameParts.get(1) : "";
        this.gender = GenderEnum
                .getGenderByVnName(ObjectUtils.isEmpty(importDTO.getGenderRaw()) ? "Nam" : importDTO.getGenderRaw());
        this.setCreatedBy(AuthUtils.getCurrentUserId());
        this.userType = importDTO.getUserType();
        this.createdSource = 0;
        this.birthDate = DateUtils.parseWithDefault(importDTO.getBirthDateRaw(), DateUtils.FORMAT_YYYY_MM_DD, null);
        this.status = StatusEnum.ENABLED.getStatus();
        this.departmentId = -1L;
    }

    /**
     * Get fullname of user
     */
    public String getFullName() {
        String fullName = "";
        if (Objects.nonNull(this.lastName) && !this.lastName.isEmpty())
            fullName += this.lastName + " ";
        if (Objects.nonNull(this.firstName) && !this.lastName.isEmpty())
            fullName += this.firstName;
        return fullName;
    }

}