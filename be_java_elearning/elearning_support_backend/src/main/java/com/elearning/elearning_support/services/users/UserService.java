package com.elearning.elearning_support.services.users;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.elearning.elearning_support.dtos.fileAttach.importFile.ImportResponseDTO;
import com.elearning.elearning_support.dtos.users.ChangePasswordReqDTO;
import com.elearning.elearning_support.dtos.users.GetAllUserResponseDTO;
import com.elearning.elearning_support.dtos.users.IGetUserListDTO;
import com.elearning.elearning_support.dtos.users.ImportUserValidatorDTO;
import com.elearning.elearning_support.dtos.users.ProfileUpdateDTO;
import com.elearning.elearning_support.dtos.users.ProfileUserDTO;
import com.elearning.elearning_support.dtos.users.UserCreateDTO;
import com.elearning.elearning_support.dtos.users.UserDetailDTO;
import com.elearning.elearning_support.dtos.users.UserSaveReqDTO;
import com.elearning.elearning_support.dtos.users.importUser.CommonUserImportDTO;
import com.elearning.elearning_support.dtos.users.importUser.ValidatedImportUserDTO;
import com.elearning.elearning_support.entities.users.User;
import com.elearning.elearning_support.enums.users.UserTypeEnum;
import com.elearning.elearning_support.dtos.users.AssignCustomRolesRequest;

@Service
public interface UserService {

    /**
     * Get loged in user's profile
     */
    ProfileUserDTO getUserProfile();

    /**
     * Tạo user trong hệ thống
     */
    void createUser(UserCreateDTO createDTO);

    /**
     * Cập nhật thông tin user hệ thống
     */
    void updateUser(Long userId, UserSaveReqDTO updateDTO);

    /**
     * Cập nhật profile người dùng
     */
    void updateProfile(ProfileUpdateDTO profileUpdateDTO);

    /**
     * Chi tiết thông tin user
     */
    UserDetailDTO getUserDetail(Long userId);

    /**
     * Xóa hoàn toàn user
     */
    void deleteUserHard(UserTypeEnum userType, Long userId);

    /**
     * Danh sách quản trị viên dạng page
     */
    Page<IGetUserListDTO> getPageAdmin(String search, Pageable pageable);

    /**
     * Lấy danh sách HSSV dạng page
     */
    Page<IGetUserListDTO> getPageStudent(String search, Set<Integer> courseNums, Pageable pageable);

    /**
     * Lấy danh sách HSSV dạng list
     */
    List<IGetUserListDTO> getListStudent(String search, Set<Integer> courseNums);

    /**
     * Lấy danh sách GV dạng page
     */
    Page<IGetUserListDTO> getPageTeacher(String search, Pageable pageable);

    /**
     * Lấy danh sách Users toàn hệ thống dạng page
     */
    Page<GetAllUserResponseDTO> getAllUsers(String search, Long roleBaseId, Pageable pageable);

    /**
     * Lấy danh sách GV dạng list
     */
    List<IGetUserListDTO> getListTeacher(String search);

    /**
     * Import danh sách HSSV
     */
    ImportResponseDTO importStudent(MultipartFile fileImport) throws IOException;

    /**
     * Validate import user
     */
    ValidatedImportUserDTO validateImportUser(ImportUserValidatorDTO validatorDTO, CommonUserImportDTO importDTO,
            List<String> causeList);

    /**
     * Export danh sách HSSV
     */
    InputStreamResource exportStudent(String search, Set<Integer> courseNums) throws IOException;

    /**
     * Import danh sách GV
     */
    ImportResponseDTO importTeacher(MultipartFile fileImport) throws IOException;

    /**
     * Export danh sách GV
     */
    InputStreamResource exportTeacher(String search) throws IOException;

    /**
     * Generate username, password and email for user
     */
    void generateUsernamePasswordEmail(User user);

    /**
     * Check exists by username
     */
    Boolean existsByUsername(String username);

    /**
     * Change user's password
     */
    void changePassword(ChangePasswordReqDTO reqDTO);

}
