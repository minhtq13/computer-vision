package com.elearning.elearning_support.repositories.postgres.users;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.elearning.elearning_support.constants.sql.SQLUser;
import com.elearning.elearning_support.dtos.common.ICommonIdCode;
import com.elearning.elearning_support.dtos.users.GetAllUserDTO;
import com.elearning.elearning_support.dtos.users.IGetDetailUserDTO;
import com.elearning.elearning_support.dtos.users.IGetUserListDTO;
import com.elearning.elearning_support.entities.users.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsernameAndStatus(String username, Integer status);

    Optional<User> findByIdAndDeletedFlag(Long id, Integer deletedFlag);

    Integer countByUsername(String username);

    Boolean existsByCode(String code);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    Boolean existsByEmailAndIdNot(String email, Long id);

    Boolean existsByCodeAndUserType(String code, Integer userType);

    Boolean existsByCodeAndUserTypeAndIdNot(String code, Integer userType, Long userId);

    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.userType = :userType WHERE u.id = :userId")
    void updateUserType(Long userId, Integer userType);

    @Query(nativeQuery = true, value = SQLUser.GET_USER_DETAIL)
    IGetDetailUserDTO getDetailUser(Long userId);

    @Query(nativeQuery = true, value = SQLUser.GET_LIST_STUDENT)
    Page<IGetUserListDTO> getPageStudent(BigDecimal[] departmentArr, String search, Set<Integer> courseNums,
            Pageable pageable);

    @Query(nativeQuery = true, value = SQLUser.GET_LIST_STUDENT)
    List<IGetUserListDTO> getListStudent(BigDecimal[] departmentArr, String search, Set<Integer> courseNums);

    @Query(nativeQuery = true, value = SQLUser.GET_LIST_TEACHER)
    Page<IGetUserListDTO> getPageTeacher(BigDecimal[] departmentArr, String search, Pageable pageable);

    @Query(nativeQuery = true, value = SQLUser.GET_ALL_USERS)
    Page<GetAllUserDTO> getAllUsers(String search, Long roleBaseId, Pageable pageable);

    @Query(nativeQuery = true, value = SQLUser.GET_LIST_TEACHER)
    List<IGetUserListDTO> getListTeacher(BigDecimal[] departmentArr, String search);

    @Query(nativeQuery = true, value = SQLUser.GET_LIST_CURRENT_USERNAME)
    Set<String> getLstCurrentUsername();

    @Query(nativeQuery = true, value = SQLUser.GET_LIST_CURRENT_USER_EMAIL)
    Set<String> getListCurrentEmail();

    @Query(nativeQuery = true, value = SQLUser.GET_LIST_CURRENT_USER_CODE_BY_USER_TYPE)
    Set<String> getListCurrentCodeByUserType(Integer userType);

    @Query(nativeQuery = true, value = SQLUser.GET_LIST_USER_ID_CODE_BY_CODE_AND_USER_TYPE)
    List<ICommonIdCode> getListIdCodeByCodeAndUserType(Set<String> lstCode, Integer userType);

    @Query(nativeQuery = true, value = SQLUser.FIND_STUDENT_ID_BY_UNIQUE_INFO)
    Set<Long> findStudentByUniqueInfo(String code, String email, String username);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = SQLUser.UPDATE_USER_FCM_TOKEN)
    void updateUserFCMToken(Long userId, String fcmToken);

    @Query(nativeQuery = true, value = SQLUser.GET_LIST_ADMIN)
    Page<IGetUserListDTO> getPageAdmin(BigDecimal[] departmentArr, String search, Pageable pageable);

    @Query(nativeQuery = true, value = SQLUser.GET_FCM_TOKEN_BY_USER_ID)
    String getFCMTokenByUserId(Long userId);

}
