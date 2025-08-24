package com.elearning.elearning_support.services.users.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.math3.util.Pair;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.elearning.elearning_support.annotations.dataSource.DataSource;
import com.elearning.elearning_support.annotations.log.ExecutionTimeLog;
import com.elearning.elearning_support.constants.FileConstants.Extension.Excel;
import com.elearning.elearning_support.constants.RoleConstants;
import com.elearning.elearning_support.constants.message.errorKey.ErrorKey;
import com.elearning.elearning_support.constants.message.messageConst.MessageConst;
import com.elearning.elearning_support.constants.message.messageConst.MessageConst.CommonError;
import com.elearning.elearning_support.constants.message.messageConst.MessageConst.FileAttach;
import com.elearning.elearning_support.constants.message.messageConst.MessageConst.Resources;
import com.elearning.elearning_support.dtos.common.ICommonIdCodeName;
import com.elearning.elearning_support.dtos.fileAttach.importFile.ImportResponseDTO;
import com.elearning.elearning_support.dtos.fileAttach.importFile.RowErrorDTO;
import com.elearning.elearning_support.dtos.users.ChangePasswordReqDTO;
import com.elearning.elearning_support.dtos.users.GetAllUserDTO;
import com.elearning.elearning_support.dtos.users.GetAllUserResponseDTO;
import com.elearning.elearning_support.dtos.users.IGetDetailUserDTO;
import com.elearning.elearning_support.dtos.users.IGetUserListDTO;
import com.elearning.elearning_support.dtos.users.ImportUserValidatorDTO;
import com.elearning.elearning_support.dtos.users.ProfileUpdateDTO;
import com.elearning.elearning_support.dtos.users.ProfileUserDTO;
import com.elearning.elearning_support.dtos.users.UserCreateDTO;
import com.elearning.elearning_support.dtos.users.UserDetailDTO;
import com.elearning.elearning_support.dtos.users.UserSaveReqDTO;
import com.elearning.elearning_support.dtos.users.importUser.CommonUserImportDTO;
import com.elearning.elearning_support.dtos.users.importUser.ValidatedImportUserDTO;
import com.elearning.elearning_support.dtos.users.student.StudentExportDTO;
import com.elearning.elearning_support.dtos.users.student.StudentImportDTO;
import com.elearning.elearning_support.dtos.users.student.StudentMetaData;
import com.elearning.elearning_support.dtos.users.teacher.TeacherExportDTO;
import com.elearning.elearning_support.dtos.users.teacher.TeacherImportDTO;
import com.elearning.elearning_support.dtos.users.teacher.TeacherMetaData;
import com.elearning.elearning_support.entities.studentTest.StudentTestSet;
import com.elearning.elearning_support.entities.teacherSubject.TeacherSubject;
import com.elearning.elearning_support.entities.users.User;
import com.elearning.elearning_support.entities.users.UserDepartment;
import com.elearning.elearning_support.entities.users.UserRole;
import com.elearning.elearning_support.enums.commons.DeletedFlag;
import com.elearning.elearning_support.enums.dataSource.DataSourceRouteEnum;
import com.elearning.elearning_support.enums.importFile.ImportResponseEnum;
import com.elearning.elearning_support.enums.importFile.StudentImportFieldMapping;
import com.elearning.elearning_support.enums.importFile.TeacherImportFieldMapping;
import com.elearning.elearning_support.enums.subject.TeacherSubjectRoleTypeEnum;
import com.elearning.elearning_support.enums.users.GenderEnum;
import com.elearning.elearning_support.enums.users.UserTypeEnum;
import com.elearning.elearning_support.exceptions.BadRequestException;
import com.elearning.elearning_support.exceptions.exceptionFactory.ExceptionFactory;
import com.elearning.elearning_support.repositories.postgres.department.DepartmentRepository;
import com.elearning.elearning_support.repositories.postgres.examClass.UserExamClassRepository;
import com.elearning.elearning_support.repositories.postgres.studentTestSet.StudentTestSetRepository;
import com.elearning.elearning_support.repositories.postgres.subject.SubjectRepository;
import com.elearning.elearning_support.repositories.postgres.teacherSubject.TeacherSubjectRepository;
import com.elearning.elearning_support.repositories.postgres.users.UserDepartmentRepository;
import com.elearning.elearning_support.repositories.postgres.users.UserRepository;
import com.elearning.elearning_support.repositories.postgres.users.UserRoleRepository;
import com.elearning.elearning_support.services.users.UserService;
import com.elearning.elearning_support.utils.DateUtils;
import com.elearning.elearning_support.utils.StringUtils;
import com.elearning.elearning_support.utils.auth.AuthUtils;
import com.elearning.elearning_support.utils.excelFile.ExcelFileUtils;
import com.elearning.elearning_support.utils.excelFile.ExcelValidationUtils;
import com.elearning.elearning_support.utils.file.FileUtils;
import com.elearning.elearning_support.utils.object.ObjectMapperUtils;
import com.elearning.elearning_support.utils.object.ObjectUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final DepartmentRepository departmentRepository;

    private final UserRepository userRepository;

    private final UserRoleRepository userRoleRepository;

    private final PasswordEncoder passwordEncoder;

    private final ExceptionFactory exceptionFactory;

    private final ExcelFileUtils excelFileUtils;

    private final StudentTestSetRepository studentTestSetRepository;

    private final UserExamClassRepository userExamClassRepository;

    private final SubjectRepository subjectRepository;

    private final TeacherSubjectRepository teacherSubjectRepository;

    private final UserDepartmentRepository userDepartmentRepository;

    @Value("${app.mail.generate-domain}")
    private String defaultMailDomain = "yopmail.com";

    private final String[] IGNORE_COPY_USER_PROPERTIES = new String[] {
            "userType"
    };

    @ExecutionTimeLog
    @DataSource(value = DataSourceRouteEnum.SLAVE)
    @Override
    public ProfileUserDTO getUserProfile() {
        Long currentUserId = AuthUtils.getCurrentUserId();
        if (Objects.nonNull(currentUserId)) {
            return new ProfileUserDTO(userRepository.getDetailUser(AuthUtils.getCurrentUserId()));
        }
        return new ProfileUserDTO();
    }

    @Transactional
    @Override
    public void createUser(UserCreateDTO createDTO) {
        // Validate username
        if (userRepository.existsByUsername(createDTO.getUsername())) {
            throw exceptionFactory.resourceExistedException(MessageConst.User.USER_USERNAME_EXISTED_ERROR,
                    Resources.USER,
                    MessageConst.RESOURCE_EXISTED, ErrorKey.User.USERNAME, createDTO.getUsername());
        }
        // validate updated fields
        validateCreateUser(createDTO);

        // Tạo user mới
        User newUser = new User();
        org.springframework.beans.BeanUtils.copyProperties(createDTO, newUser);
        newUser.setMetaData(
                Objects.equals(createDTO.getUserType(), UserTypeEnum.STUDENT.getType()) ? createDTO.getMetaData()
                        : null);
        newUser.setCreatedAt(new Date());
        newUser.setCreatedBy(AuthUtils.getCurrentUserId());
        newUser.setPassword(passwordEncoder.encode(createDTO.getPassword()));
        newUser.setGender(createDTO.getGenderType().getType());
        newUser.setMetaData(createDTO.getMetaData());
        newUser = userRepository.save(newUser);

        // Add to user_role
        Long newUserId = newUser.getId();
        List<UserRole> lstUserRole = createDTO.getLstRoleId().stream().map(roleId -> new UserRole(newUserId, roleId))
                .collect(Collectors.toList());
        // check if has department => add ROLE_ADMIN_DEPARTMENT
        if (!org.springframework.util.ObjectUtils.isEmpty(createDTO.getDepartmentIds())
                && Objects.equals(createDTO.getUserType(), UserTypeEnum.ADMIN.getType())) {
            boolean isAdminDepartment = false;
            for (Long departmentId : createDTO.getDepartmentIds()) {
                if (departmentRepository.existsByIdAndCodeNot(departmentId, RoleConstants.ROOT_DEPARTMENT)) {
                    isAdminDepartment = true;
                    break;
                }
            }
            if (isAdminDepartment) {
                lstUserRole.add(new UserRole(newUserId, RoleConstants.ROLE_ADMIN_DEPARTMENT_ID));
            } else {
                lstUserRole.add(new UserRole(newUserId, RoleConstants.ROLE_ADMIN_SYSTEM_ID));
            }
        }
        userRoleRepository.saveAll(lstUserRole);

        // Add user_department
        List<UserDepartment> lstUserDepartment = createDTO.getDepartmentIds().stream()
                .map(departmentId -> new UserDepartment(newUserId, departmentId)).collect(Collectors.toList());
        userDepartmentRepository.saveAll(lstUserDepartment);

        // if user is teacher -> check and save teacher_subject
        final Long userId = newUser.getId();
        if (UserTypeEnum.TEACHER.getType().equals(createDTO.getUserType())
                && createDTO.getLstRoleId().contains(RoleConstants.ROLE_TEACHER_ID)
                && Objects.nonNull(createDTO.getMetaData())) {
            TeacherMetaData metaData = ObjectMapperUtils.objectMapper(
                    ObjectMapperUtils.toJsonString(createDTO.getMetaData()),
                    TeacherMetaData.class);
            if (Objects.nonNull(metaData) && !org.springframework.util.ObjectUtils.isEmpty(metaData.getSubjectIds())) {
                List<TeacherSubject> lstTeacherSubjects = metaData.getSubjectIds().stream()
                        .map(item -> new TeacherSubject(userId, item, TeacherSubjectRoleTypeEnum.LECTURER.getType()))
                        .collect(Collectors.toList());
                teacherSubjectRepository.saveAll(lstTeacherSubjects);
            }
        }
    }

    @Transactional
    @Override
    public void updateUser(Long userId, UserSaveReqDTO updateDTO) {
        // Kiểm tra user tồn tại
        User user = findUserById(userId);

        // Validate updated fields
        validateUpdateUser(userId, updateDTO);

        // Update fields
        org.springframework.beans.BeanUtils.copyProperties(updateDTO, user, IGNORE_COPY_USER_PROPERTIES);
        user.setMetaData(
                Objects.equals(updateDTO.getUserType(), UserTypeEnum.STUDENT.getType()) ? updateDTO.getMetaData()
                        : null);
        user.setGender(updateDTO.getGenderType().getType());
        user.setMetaData(updateDTO.getMetaData());
        user.setModifiedAt(new Date());
        user.setModifiedBy(AuthUtils.getCurrentUserId());
        user = userRepository.save(user);

        // Update user_role (delete all -> save new)
        if (!org.springframework.util.ObjectUtils.isEmpty(updateDTO.getLstRoleId())) {
            userRoleRepository.deleteAllByUserIdNative(user.getId());
            List<UserRole> lstUserRole = updateDTO.getLstRoleId().stream().map(roleId -> new UserRole(userId, roleId))
                    .collect(Collectors.toList());
            // department !== root => add ROLE_ADMIN_DEPARTMENT
            if (!org.springframework.util.ObjectUtils.isEmpty(updateDTO.getDepartmentIds())
                    && Objects.equals(updateDTO.getUserType(), UserTypeEnum.ADMIN.getType())) {
                boolean isAdminDepartment = false;
                for (Long departmentId : updateDTO.getDepartmentIds()) {
                    if (departmentRepository.existsByIdAndCodeNot(departmentId, RoleConstants.ROOT_DEPARTMENT)) {
                        isAdminDepartment = true;
                        break;
                    }
                }
                if (isAdminDepartment) {
                    lstUserRole.add(new UserRole(user.getId(), RoleConstants.ROLE_ADMIN_DEPARTMENT_ID));
                } else {
                    lstUserRole.add(new UserRole(user.getId(), RoleConstants.ROLE_ADMIN_SYSTEM_ID));
                }
            }
            userRoleRepository.saveAll(lstUserRole);
        }

        // update user_department (add new and remove old)
        if (Objects.nonNull(updateDTO.getDepartmentIds())) {
            List<UserDepartment> lstCurrentUserDepartment = userDepartmentRepository.findAllByUserId(user.getId());

            Set<Long> currentDepartmentIds = lstCurrentUserDepartment.stream().map(UserDepartment::getDepartmentId)
                    .collect(Collectors.toSet());
            // id to remove
            Set<Long> lstRemoveDepartmentIds = currentDepartmentIds.stream()
                    .filter(departmentId -> !updateDTO.getDepartmentIds().contains(departmentId))
                    .collect(Collectors.toSet());
            lstCurrentUserDepartment.removeIf(item -> lstRemoveDepartmentIds.contains(item.getDepartmentId()));
            userDepartmentRepository.deleteAllByUserIdAndDepartmentIdIn(user.getId(), lstRemoveDepartmentIds);

            // id to add
            Set<Long> lstAddDepartmentIds = updateDTO.getDepartmentIds().stream()
                    .filter(departmentId -> !currentDepartmentIds.contains(departmentId)).collect(Collectors.toSet());
            lstAddDepartmentIds
                    .forEach(departmentId -> lstCurrentUserDepartment.add(new UserDepartment(userId, departmentId)));

            // save to db
            userDepartmentRepository.saveAll(lstCurrentUserDepartment);
        }

        // if user is teacher -> check and save teacher_subject
        if (UserTypeEnum.TEACHER.getType().equals(user.getUserType()) && Objects.nonNull(updateDTO.getMetaData())) {
            TeacherMetaData metaData = ObjectMapperUtils.objectMapper(
                    ObjectMapperUtils.toJsonString(updateDTO.getMetaData()),
                    TeacherMetaData.class);
            if (Objects.nonNull(metaData)) {
                // delete all and insert replacement
                teacherSubjectRepository.deleteAllByTeacherId(userId);
                List<TeacherSubject> lstTeacherSubjects = metaData.getSubjectIds().stream()
                        .map(item -> new TeacherSubject(userId, item, TeacherSubjectRoleTypeEnum.LECTURER.getType()))
                        .collect(
                                Collectors.toList());
                teacherSubjectRepository.saveAll(lstTeacherSubjects);
            }
        }
    }

    @Override
    public void updateProfile(ProfileUpdateDTO profileUpdateDTO) {
        Long currentUserId = AuthUtils.getCurrentUserId();
        // Kiểm tra user tồn tại
        User user = findUserById(currentUserId);

        // Validate updated fields
        if (userRepository.existsByCodeAndUserTypeAndIdNot(profileUpdateDTO.getCode(), profileUpdateDTO.getUserType(),
                currentUserId)) {
            throw exceptionFactory.resourceExistedException(MessageConst.User.USER_CODE_AND_USER_TYPE_EXISTED_ERROR,
                    Resources.USER,
                    MessageConst.RESOURCE_EXISTED, ErrorKey.User.CODE, profileUpdateDTO.getCode());
        }

        // Validate existed email;
        if (userRepository.existsByEmailAndIdNot(profileUpdateDTO.getEmail(), currentUserId)) {
            throw exceptionFactory.resourceExistedException(MessageConst.User.USER_EMAIL_EXISTED_ERROR, Resources.USER,
                    MessageConst.RESOURCE_EXISTED, ErrorKey.User.EMAIL, profileUpdateDTO.getEmail());
        }

        // Update fields
        org.springframework.beans.BeanUtils.copyProperties(profileUpdateDTO, user, IGNORE_COPY_USER_PROPERTIES);
        user.setGender(profileUpdateDTO.getGenderType().getType());
        user.setModifiedAt(new Date());
        user.setModifiedBy(currentUserId);
        userRepository.save(user);
    }

    @Override
    public UserDetailDTO getUserDetail(Long userId) {
        IGetDetailUserDTO iUserDetails = userRepository.getDetailUser(userId);
        if (Objects.isNull(iUserDetails)) {
            throw exceptionFactory.resourceNotFoundException(MessageConst.User.USER_NOT_FOUND_ERROR_CODE,
                    Resources.USER,
                    MessageConst.RESOURCE_NOT_FOUND, ErrorKey.User.ID, String.valueOf(userId));
        }
        return new UserDetailDTO(iUserDetails);
    }

    @Transactional
    @Override
    public void deleteUserHard(UserTypeEnum userType, Long userId) {
        // check delete permission
        if (!AuthUtils.isAdmin() && Objects.equals(userType.getType(), UserTypeEnum.STUDENT.getType())) {
            throw exceptionFactory.permissionDeniedException(CommonError.PERMISSIONS_DENIED, Resources.USER,
                    MessageConst.PERMISSIONS_DENIED);
        }
        try {
            // check user exists
            if (!userRepository.existsById(userId)) {
                throw exceptionFactory.resourceNotFoundException(MessageConst.User.USER_NOT_FOUND_ERROR_CODE,
                        Resources.USER,
                        MessageConst.RESOURCE_NOT_FOUND, ErrorKey.User.ID, String.valueOf(userId));
            }
            // check user_type
            if (Objects.equals(userType, UserTypeEnum.STUDENT)) {
                // xóa student_test_set
                List<StudentTestSet> lstStudentTestSet = studentTestSetRepository.findAllByStudentId(userId);
                studentTestSetRepository.deleteAll(lstStudentTestSet);
            }
            // xóa users_roles
            userRoleRepository.deleteAllByUserIdNative(userId);
            // xóa user_exam_class
            userExamClassRepository.deleteAllByUserId(userId);
            // xóa users
            userRepository.deleteById(userId);
        } catch (Exception e) {
            log.error(MessageConst.EXCEPTION_LOG_FORMAT, e.getMessage(), e.getCause(), e);
        }
    }

    @Override
    public Page<IGetUserListDTO> getPageAdmin(String search, Pageable pageable) {
        if (AuthUtils.isAdmin()) {
            return userRepository.getPageAdmin(new BigDecimal[] { BigDecimal.valueOf(-1L) }, search, pageable);
        }
        BigDecimal[] departmentArr = userDepartmentRepository.findAllByUserId(AuthUtils.getCurrentUserId()).stream()
                .map(item -> BigDecimal.valueOf(item.getDepartmentId())).collect(
                        Collectors.toList())
                .toArray(BigDecimal[]::new);
        return userRepository.getPageAdmin(departmentArr, search, pageable);
    }

    @Override
    public Page<IGetUserListDTO> getPageStudent(String search, Set<Integer> courseNums, Pageable pageable) {
        if (AuthUtils.isAdmin()) {
            return userRepository.getPageStudent(new BigDecimal[] { BigDecimal.valueOf(-1L) }, search, courseNums,
                    pageable);
        }
        BigDecimal[] departmentArr = userDepartmentRepository.findAllByUserId(AuthUtils.getCurrentUserId()).stream()
                .map(item -> BigDecimal.valueOf(item.getDepartmentId())).collect(
                        Collectors.toList())
                .toArray(BigDecimal[]::new);
        return userRepository.getPageStudent(departmentArr, search, courseNums, pageable);
    }

    @Override
    public List<IGetUserListDTO> getListStudent(String search, Set<Integer> courseNums) {
        if (AuthUtils.isAdmin()) {
            return userRepository.getListStudent(new BigDecimal[] { BigDecimal.valueOf(-1L) }, search, courseNums);
        }
        BigDecimal[] departmentArr = userDepartmentRepository.findAllByUserId(AuthUtils.getCurrentUserId()).stream()
                .map(item -> BigDecimal.valueOf(item.getDepartmentId())).collect(
                        Collectors.toList())
                .toArray(BigDecimal[]::new);
        return userRepository.getListStudent(departmentArr, search, courseNums);
    }

    @Override
    public Page<IGetUserListDTO> getPageTeacher(String search, Pageable pageable) {
        if (AuthUtils.isAdmin()) {
            return userRepository.getPageTeacher(new BigDecimal[] { BigDecimal.valueOf(-1L) }, search, pageable);
        }
        BigDecimal[] departmentArr = userDepartmentRepository.findAllByUserId(AuthUtils.getCurrentUserId()).stream()
                .map(item -> BigDecimal.valueOf(item.getDepartmentId())).collect(
                        Collectors.toList())
                .toArray(BigDecimal[]::new);
        return userRepository.getPageTeacher(departmentArr, search, pageable);
    }

    @Override
    public Page<GetAllUserResponseDTO> getAllUsers(String search, Long roleBaseId, Pageable pageable) {
        Page<GetAllUserDTO> userPage;
        if (roleBaseId != null) {
            userPage = userRepository.getAllUsers(search, roleBaseId, pageable);
        } else {
            userPage = userRepository.getAllUsers(search, null, pageable);
        }
        return userPage.map(GetAllUserResponseDTO::new);
    }

    @Override
    public List<IGetUserListDTO> getListTeacher(String search) {
        if (AuthUtils.isAdmin()) {
            return userRepository.getListTeacher(new BigDecimal[] { BigDecimal.valueOf(-1L) }, search);
        }
        BigDecimal[] departmentArr = userDepartmentRepository.findAllByUserId(AuthUtils.getCurrentUserId()).stream()
                .map(item -> BigDecimal.valueOf(item.getDepartmentId())).collect(
                        Collectors.toList())
                .toArray(BigDecimal[]::new);
        return userRepository.getListTeacher(departmentArr, search);
    }

    @Transactional
    @Override
    public ImportResponseDTO importStudent(MultipartFile fileImport) throws IOException {
        // Tạo response mặc định
        ImportResponseDTO response = new ImportResponseDTO();
        response.setStatus(ImportResponseEnum.SUCCESS.getStatus());
        response.setMessage(ImportResponseEnum.SUCCESS.getMessage());
        XSSFWorkbook inputWorkbook = null;
        // Đọc file và import dữ liệu
        try {
            // Validate file sơ bộ
            FileUtils.validateUploadFile(fileImport, Arrays.asList(Excel.XLS, Excel.XLSX));

            // Tạo workbook để đọc file import
            inputWorkbook = new XSSFWorkbook(fileImport.getInputStream());
            XSSFSheet inputSheet = inputWorkbook.getSheetAt(0);
            if (Objects.isNull(inputSheet)) {
                throw exceptionFactory.fileUploadException(FileAttach.FILE_EXCEL_EMPTY_SHEET_ERROR,
                        Resources.FILE_ATTACHED,
                        MessageConst.UPLOAD_FAILED);
            }

            // Validators
            ImportUserValidatorDTO validatorDTO = new ImportUserValidatorDTO(userRepository.getLstCurrentUsername(),
                    userRepository.getListCurrentEmail(),
                    userRepository.getListCurrentCodeByUserType(UserTypeEnum.STUDENT.getType()));
            // map current department
            List<ICommonIdCodeName> allCurrentDepartment = departmentRepository.getAllDepartments();
            Map<String, ICommonIdCodeName> mapDepartments = new HashMap<>();
            allCurrentDepartment.forEach(item -> mapDepartments.put(item.getCode(), item));
            validatorDTO.setMapDepartments(mapDepartments);

            // Tạo các map field
            Map<Integer, String> mapIndexColumnKey = new HashMap<>();
            // students save DB
            List<User> lstNewStudent = new ArrayList<>();

            // Duyệt file input
            Iterator<Row> rowIterator = inputSheet.rowIterator();
            int numberOfColumns = StudentImportFieldMapping.values().length;
            while (rowIterator.hasNext()) {
                Row currentRow = rowIterator.next();
                boolean isEmptyRow = true;
                if (currentRow.getRowNum() == 0) { // header row
                    // Validate thừa thiếu/cột
                    if (currentRow.getLastCellNum() < numberOfColumns) {
                        throw exceptionFactory.fileUploadException(FileAttach.FILE_EXCEL_MISSING_COLUMN_NUMBER_ERROR,
                                Resources.FILE_ATTACHED,
                                MessageConst.UPLOAD_FAILED);
                    }
                    if (currentRow.getLastCellNum() > numberOfColumns) {
                        throw exceptionFactory.fileUploadException(FileAttach.FILE_EXCEL_MISSING_COLUMN_NUMBER_ERROR,
                                Resources.FILE_ATTACHED,
                                MessageConst.UPLOAD_FAILED);
                    }
                    for (Cell cell : currentRow) {
                        // Validate tên cột
                        String columnKey = ExcelFileUtils.getStringCellValue(cell);
                        if (Objects.isNull(StudentImportFieldMapping.getObjectFieldByColumnKey(columnKey))) {
                            throw exceptionFactory.fileUploadException(FileAttach.FILE_EXCEL_INVALID_COLUMN_NAME,
                                    Resources.FILE_ATTACHED,
                                    MessageConst.UPLOAD_FAILED);
                        }
                        mapIndexColumnKey.put(cell.getColumnIndex(), columnKey);
                    }
                    continue;
                }
                // Duyệt các cell trong row
                StudentImportDTO importDTO = new StudentImportDTO();
                importDTO.setUserType(UserTypeEnum.STUDENT.getType());
                for (Cell cell : currentRow) {
                    String columnKey = mapIndexColumnKey.get(cell.getColumnIndex());
                    String objectFieldKey = StudentImportFieldMapping.getObjectFieldByColumnKey(columnKey);
                    String cellValue = ExcelFileUtils.getStringCellValue(cell);
                    if (!org.springframework.util.ObjectUtils.isEmpty(cellValue)) {
                        isEmptyRow = false;
                    }
                    if (!org.springframework.util.ObjectUtils.isEmpty(objectFieldKey)) {
                        BeanUtils.setProperty(importDTO, objectFieldKey, cellValue);
                    }
                }
                // Validate và mapping vào entity
                List<String> causeList = new ArrayList<>();
                validateImportUser(validatorDTO, importDTO, causeList);
                if (!isEmptyRow) {
                    if (org.springframework.util.ObjectUtils.isEmpty(causeList)) {
                        User newStudent = new User(importDTO);
                        // username or password null/empty -> auto generated
                        if (org.springframework.util.ObjectUtils.isEmpty(importDTO.getUsername())
                                || org.springframework.util.ObjectUtils.isEmpty(importDTO.getPasswordRaw())) {
                            generateUsernamePasswordEmail(newStudent);
                        } else {
                            newStudent.setUsername(importDTO.getUsername());
                            newStudent.setPassword(passwordEncoder.encode(importDTO.getPasswordRaw()));
                        }
                        // meta data
                        StudentMetaData metaData = new StudentMetaData();
                        metaData.setCourseNum(ObjectUtils.getOrDefault(Integer.valueOf(importDTO.getCourseRaw()), 0));
                        newStudent.setMetaData(metaData);

                        newStudent.setDepartmentId(mapDepartments.get(importDTO.getDepartmentCode()).getId());
                        lstNewStudent.add(newStudent);
                        // add username/email/code to validators
                        validatorDTO.getLstExistedUsername().add(newStudent.getUsername());
                        validatorDTO.getLstExistedEmail().add(newStudent.getEmail());
                        validatorDTO.getLstExistedCode().add(newStudent.getCode());
                    } else {
                        response.getErrorRows().add(new RowErrorDTO(currentRow.getRowNum() + 1, importDTO, causeList));
                        response.setMessage(ImportResponseEnum.EXIST_INVALID_DATA.getMessage());
                        response.setStatus(ImportResponseEnum.EXIST_INVALID_DATA.getStatus());
                    }
                }
            }
            inputWorkbook.close(); // closed stream to avoid leaking
            lstNewStudent = userRepository.saveAll(lstNewStudent);

            // map user role
            List<UserRole> lstStudentUserRole = lstNewStudent.stream()
                    .map(student -> new UserRole(student.getId(), RoleConstants.ROLE_STUDENT_ID))
                    .collect(Collectors.toList());
            userRoleRepository.saveAll(lstStudentUserRole);

            // map user_department
            List<UserDepartment> lstUserDepartment = lstNewStudent.stream()
                    .map(student -> new UserDepartment(student.getId(), student.getDepartmentId())).collect(
                            Collectors.toList());
            userDepartmentRepository.saveAll(lstUserDepartment);

            return response;
        } catch (IOException ioException) {
            response.setMessage(ImportResponseEnum.IO_ERROR.getMessage());
            response.setStatus(ImportResponseEnum.IO_ERROR.getStatus());
        } catch (Exception exception) {
            if (Objects.nonNull(inputWorkbook)) {
                inputWorkbook.close();
            }
            response.setMessage(ImportResponseEnum.UNKNOWN_ERROR.getMessage());
            response.setStatus(ImportResponseEnum.UNKNOWN_ERROR.getStatus());
            log.error(MessageConst.EXCEPTION_LOG_FORMAT, exception.getMessage(), exception.getCause(), exception);
        }
        return null;
    }

    @Override
    public InputStreamResource exportStudent(String search, Set<Integer> courseNums) throws IOException {
        // filter by departments
        List<StudentExportDTO> lstStudent;
        if (AuthUtils.isAdmin()) {
            lstStudent = userRepository.getListStudent(new BigDecimal[] { BigDecimal.valueOf(-1L) }, search, courseNums)
                    .stream()
                    .map(StudentExportDTO::new).collect(Collectors.toList());
        } else {
            BigDecimal[] departmentArr = userDepartmentRepository.findAllByUserId(AuthUtils.getCurrentUserId()).stream()
                    .map(item -> BigDecimal.valueOf(item.getDepartmentId())).collect(
                            Collectors.toList())
                    .toArray(BigDecimal[]::new);
            lstStudent = userRepository.getListStudent(departmentArr, search, courseNums).stream()
                    .map(StudentExportDTO::new).collect(Collectors.toList());
        }
        // Tạo map cấu trúc file excel
        Map<Integer, Pair<String, String>> mapStructure = new LinkedHashMap<>();
        mapStructure.put(1, Pair.create("Họ", "getLastName"));
        mapStructure.put(2, Pair.create("Tên", "getFirstName"));
        mapStructure.put(3, Pair.create("Giới tính", "getGenderName"));
        mapStructure.put(4, Pair.create("Ngày sinh", "getBirthDate"));
        mapStructure.put(5, Pair.create("Email", "getEmail"));
        mapStructure.put(6, Pair.create("Số điện thoại", "getPhoneNumber"));
        mapStructure.put(7, Pair.create("MSSV", "getCode"));
        mapStructure.put(8, Pair.create("Khóa", "getCourseNum"));
        mapStructure.put(9, Pair.create("Đơn vị quản lý", "getDepartmentName"));
        return excelFileUtils.createWorkbook(lstStudent, mapStructure, "students");
    }

    @Transactional
    @Override
    public ImportResponseDTO importTeacher(MultipartFile fileImport) throws IOException {
        // Tạo response mặc định
        ImportResponseDTO response = new ImportResponseDTO();
        response.setStatus(ImportResponseEnum.SUCCESS.getStatus());
        response.setMessage(ImportResponseEnum.SUCCESS.getMessage());
        XSSFWorkbook inputWorkbook = null;
        // Đọc file và import dữ liệu
        try {
            // Validate file sơ bộ
            FileUtils.validateUploadFile(fileImport, Arrays.asList(Excel.XLS, Excel.XLSX));

            // Tạo workbook để đọc file import
            inputWorkbook = new XSSFWorkbook(fileImport.getInputStream());
            XSSFSheet inputSheet = inputWorkbook.getSheetAt(0);
            if (Objects.isNull(inputSheet)) {
                throw exceptionFactory.fileUploadException(FileAttach.FILE_EXCEL_EMPTY_SHEET_ERROR,
                        Resources.FILE_ATTACHED,
                        MessageConst.UPLOAD_FAILED);
            }

            // Validators
            ImportUserValidatorDTO validatorDTO = new ImportUserValidatorDTO(userRepository.getLstCurrentUsername(),
                    userRepository.getListCurrentEmail(),
                    userRepository.getListCurrentCodeByUserType(UserTypeEnum.TEACHER.getType()));
            // map current department
            List<ICommonIdCodeName> allCurrentDepartment = departmentRepository.getAllDepartments();
            Map<String, ICommonIdCodeName> mapDepartments = new HashMap<>();
            allCurrentDepartment.forEach(item -> mapDepartments.put(item.getCode(), item));
            validatorDTO.setMapDepartments(mapDepartments);

            // map subject
            Map<String, Long> mapSubjectCodeId = new HashMap<>();
            subjectRepository.getAllSubjectIdCode()
                    .forEach(subject -> mapSubjectCodeId.put(subject.getCode(), subject.getId()));
            // Tạo các map field
            Map<Integer, String> mapIndexColumnKey = new HashMap<>();
            // students save DB
            List<User> lstNewTeacher = new ArrayList<>();

            // Duyệt file input
            Iterator<Row> rowIterator = inputSheet.rowIterator();
            int numberOfColumns = TeacherImportFieldMapping.values().length;
            while (rowIterator.hasNext()) {
                Row currentRow = rowIterator.next();
                boolean isEmptyRow = true;
                if (currentRow.getRowNum() == 0) { // header row
                    for (Cell cell : currentRow) {
                        mapIndexColumnKey.put(cell.getColumnIndex(), ExcelFileUtils.getStringCellValue(cell));
                    }
                    // Validate thừa thiếu/cột
                    if (currentRow.getLastCellNum() < numberOfColumns) {
                        throw exceptionFactory.fileUploadException(FileAttach.FILE_EXCEL_MISSING_COLUMN_NUMBER_ERROR,
                                Resources.FILE_ATTACHED,
                                MessageConst.UPLOAD_FAILED);
                    }
                    if (currentRow.getLastCellNum() > numberOfColumns) {
                        throw exceptionFactory.fileUploadException(FileAttach.FILE_EXCEL_MISSING_COLUMN_NUMBER_ERROR,
                                Resources.FILE_ATTACHED,
                                MessageConst.UPLOAD_FAILED);
                    }
                    continue;
                }
                // Duyệt các cell trong row
                TeacherImportDTO importDTO = new TeacherImportDTO();
                importDTO.setUserType(UserTypeEnum.TEACHER.getType());
                for (Cell cell : currentRow) {
                    String columnKey = mapIndexColumnKey.get(cell.getColumnIndex());
                    String objectFieldKey = TeacherImportFieldMapping.getObjectFieldByColumnKey(columnKey);
                    String cellValue = ExcelFileUtils.getStringCellValue(cell);
                    if (!org.springframework.util.ObjectUtils.isEmpty(cellValue)) {
                        isEmptyRow = false;
                    }
                    if (!org.springframework.util.ObjectUtils.isEmpty(objectFieldKey)) {
                        BeanUtils.setProperty(importDTO, objectFieldKey, cellValue);
                    }
                }
                // Validate và mapping vào entity
                List<String> causeList = new ArrayList<>();
                validateImportUser(validatorDTO, importDTO, causeList);
                if (!isEmptyRow) {
                    if (org.springframework.util.ObjectUtils.isEmpty(causeList)) {
                        User newTeacher = new User(importDTO);
                        // username or password null/empty -> auto generated
                        if (org.springframework.util.ObjectUtils.isEmpty(importDTO.getUsername())
                                || org.springframework.util.ObjectUtils.isEmpty(importDTO.getPasswordRaw())) {
                            generateUsernamePasswordEmail(newTeacher);
                        } else {
                            newTeacher.setUsername(importDTO.getUsername());
                            newTeacher.setPassword(passwordEncoder.encode(importDTO.getPasswordRaw()));
                        }
                        // add teacher_subject
                        Set<Long> subjectIds = Arrays.stream(importDTO.getSubjectCode().split(","))
                                .map(mapSubjectCodeId::get)
                                .filter(Objects::nonNull).collect(Collectors.toSet());
                        newTeacher.setMetaData(TeacherMetaData.builder().subjectIds(subjectIds).build());
                        newTeacher.setDepartmentId(mapDepartments.get(importDTO.getDepartmentCode()).getId());
                        lstNewTeacher.add(newTeacher);

                        // add username/email/code to validators
                        validatorDTO.getLstExistedUsername().add(newTeacher.getUsername());
                        validatorDTO.getLstExistedEmail().add(newTeacher.getEmail());
                        validatorDTO.getLstExistedCode().add(newTeacher.getCode());
                    } else {
                        response.getErrorRows().add(new RowErrorDTO(currentRow.getRowNum() + 1, importDTO, causeList));
                        response.setMessage(ImportResponseEnum.EXIST_INVALID_DATA.getMessage());
                        response.setStatus(ImportResponseEnum.EXIST_INVALID_DATA.getStatus());
                    }
                }
            }
            inputWorkbook.close(); // closed stream to avoid leaking
            lstNewTeacher = userRepository.saveAll(lstNewTeacher);

            // map user_role
            List<UserRole> lstTeacherUserRole = lstNewTeacher.stream()
                    .map(student -> new UserRole(student.getId(), RoleConstants.ROLE_TEACHER_ID))
                    .collect(Collectors.toList());
            userRoleRepository.saveAll(lstTeacherUserRole);

            // map teacher_subject
            List<TeacherSubject> lstTeacherSubjects = new ArrayList<>();
            lstNewTeacher.forEach(item -> lstTeacherSubjects.addAll(
                    ((TeacherMetaData) item.getMetaData()).getSubjectIds().stream()
                            .map(subjectId -> new TeacherSubject(item.getId(), subjectId,
                                    TeacherSubjectRoleTypeEnum.LECTURER.getType()))
                            .collect(Collectors.toList())));
            teacherSubjectRepository.saveAll(lstTeacherSubjects);

            // map user_department
            List<UserDepartment> lstUserDepartment = lstNewTeacher.stream()
                    .map(teacher -> new UserDepartment(teacher.getId(), teacher.getDepartmentId())).collect(
                            Collectors.toList());
            userDepartmentRepository.saveAll(lstUserDepartment);

            return response;
        } catch (IOException ioException) {
            response.setMessage(ImportResponseEnum.IO_ERROR.getMessage());
            response.setStatus(ImportResponseEnum.IO_ERROR.getStatus());
        } catch (Exception exception) {
            if (Objects.nonNull(inputWorkbook)) {
                inputWorkbook.close();
            }
            response.setMessage(ImportResponseEnum.UNKNOWN_ERROR.getMessage());
            response.setStatus(ImportResponseEnum.UNKNOWN_ERROR.getStatus());
            log.error(MessageConst.EXCEPTION_LOG_FORMAT, exception.getMessage(), exception.getCause(), exception);
        }
        return null;
    }

    @Override
    public InputStreamResource exportTeacher(String search) throws IOException {
        // filter by departments
        List<TeacherExportDTO> lstTeacher;
        if (AuthUtils.isAdmin()) {
            lstTeacher = userRepository.getListTeacher(new BigDecimal[] { BigDecimal.valueOf(-1L) }, search).stream()
                    .map(TeacherExportDTO::new).collect(Collectors.toList());
        } else {
            BigDecimal[] departmentArr = userDepartmentRepository.findAllByUserId(AuthUtils.getCurrentUserId()).stream()
                    .map(item -> BigDecimal.valueOf(item.getDepartmentId())).collect(
                            Collectors.toList())
                    .toArray(BigDecimal[]::new);
            lstTeacher = userRepository.getListTeacher(departmentArr, search).stream().map(TeacherExportDTO::new)
                    .collect(Collectors.toList());
        }
        // Tạo map cấu trúc file excel
        Map<Integer, Pair<String, String>> mapStructure = new LinkedHashMap<>();
        mapStructure.put(1, Pair.create("Họ", "getLastName"));
        mapStructure.put(2, Pair.create("Tên", "getFirstName"));
        mapStructure.put(3, Pair.create("Giới tính", "getGenderName"));
        mapStructure.put(4, Pair.create("Ngày sinh", "getBirthDate"));
        mapStructure.put(5, Pair.create("Email", "getEmail"));
        mapStructure.put(6, Pair.create("Số điện thoại", "getPhoneNumber"));
        mapStructure.put(7, Pair.create("Mã cán bộ", "getCode"));
        mapStructure.put(8, Pair.create("Đơn vị quản lý", "getDepartmentName"));
        return excelFileUtils.createWorkbook(lstTeacher, mapStructure, "teacher");
    }

    /**
     * Hàm check trùng các thông tin
     */
    @Override
    public ValidatedImportUserDTO validateImportUser(ImportUserValidatorDTO validatorDTO, CommonUserImportDTO importDTO,
            List<String> causeList) {
        ValidatedImportUserDTO validatedResult = new ValidatedImportUserDTO();
        // Validate field bắt buộc
        List<String> missingRequiredFields = new ArrayList<>();
        if (org.springframework.util.ObjectUtils.isEmpty(importDTO.getFullNameRaw())) {
            missingRequiredFields.add("fullName");
        }
        if (org.springframework.util.ObjectUtils.isEmpty(importDTO.getGenderRaw())) {
            missingRequiredFields.add("code");
        }
        if (org.springframework.util.ObjectUtils.isEmpty(importDTO.getGenderRaw())) {
            missingRequiredFields.add("gender");
        }
        if (org.springframework.util.ObjectUtils.isEmpty(importDTO.getEmail())) {
            missingRequiredFields.add("email");
        }
        if (org.springframework.util.ObjectUtils.isEmpty(importDTO.getDepartmentCode())) {
            missingRequiredFields.add("departmentCode");
        }
        if (!missingRequiredFields.isEmpty()) {
            validatedResult.setMissedRequiredField(Boolean.TRUE);
            causeList.add(String.format("Missing required fields: %s", String.join(",", missingRequiredFields)));
        }

        // Validate định dạng dữ liệu
        List<String> invalidFormatFields = new ArrayList<>();
        if (!Objects.isNull(ExcelValidationUtils.validatePhoneNumber(importDTO.getPhoneNumber()))) {
            invalidFormatFields.add("phone");
        }
        if (!Objects.isNull(ExcelValidationUtils.validateEmail(importDTO.getEmail()))) {
            invalidFormatFields.add("email");
        }
        if (Objects.isNull(GenderEnum.getGenderByVnName(importDTO.getGenderRaw()))) {
            invalidFormatFields.add("gender");
        }
        if (!invalidFormatFields.isEmpty()) {
            validatedResult.setHasInvalidFormatField(Boolean.TRUE);
            causeList.add(String.format("Invalid formatted fields: %s", String.join(",", invalidFormatFields)));
        }

        // Validate dữ liệu không tồn tại trong hệ thống
        List<String> notFoundFields = new ArrayList<>();
        if (Objects.isNull(validatorDTO.getMapDepartments().get(importDTO.getDepartmentCode()))) {
            notFoundFields.add("departmentCode");
        }
        if (!notFoundFields.isEmpty()) {
            validatedResult.setHasNotFoundField(Boolean.TRUE);
            causeList.add(String.format("Not found fields: %s", String.join(",", notFoundFields)));
        }

        // Validate trùng dữ liệu
        List<String> duplicatedFields = new ArrayList<>();
        if (validatorDTO.getLstExistedUsername().contains(importDTO.getUsername())) {
            duplicatedFields.add("username");
        }
        if (validatorDTO.getLstExistedEmail().contains(importDTO.getEmail())) {
            duplicatedFields.add("email");
        }
        if (validatorDTO.getLstExistedCode().contains(importDTO.getCode())) {
            duplicatedFields.add("code");
        }
        if (!duplicatedFields.isEmpty()) {
            validatedResult.setHasDuplicatedField(Boolean.TRUE);
            causeList.add(String.format("Duplicated fields: %s", String.join(",", duplicatedFields)));
        }

        return validatedResult;
    }

    /**
     * Validate user when create or update
     */
    private void validateCreateUser(UserSaveReqDTO saveReqDTO) {
        // Validate existed email;
        if (userRepository.existsByEmail(saveReqDTO.getEmail())) {
            throw exceptionFactory.resourceExistedException(MessageConst.User.USER_EMAIL_EXISTED_ERROR, Resources.USER,
                    MessageConst.RESOURCE_EXISTED, ErrorKey.User.EMAIL, saveReqDTO.getEmail());
        }

        // Validate existed code with the same userType
        if (userRepository.existsByCodeAndUserType(saveReqDTO.getCode(), saveReqDTO.getUserType())) {
            throw exceptionFactory.resourceExistedException(MessageConst.User.USER_CODE_AND_USER_TYPE_EXISTED_ERROR,
                    Resources.USER,
                    MessageConst.RESOURCE_EXISTED, ErrorKey.User.CODE, saveReqDTO.getCode());
        }

    }

    /**
     * Validate user when create or update
     */
    private void validateUpdateUser(Long userId, UserSaveReqDTO saveReqDTO) {
        // Validate existed email;
        if (userRepository.existsByEmailAndIdNot(saveReqDTO.getEmail(), userId)) {
            throw exceptionFactory.resourceExistedException(MessageConst.User.USER_EMAIL_EXISTED_ERROR, Resources.USER,
                    MessageConst.RESOURCE_EXISTED, ErrorKey.User.EMAIL, saveReqDTO.getEmail());
        }

        // Validate existed code with the same userType
        if (userRepository.existsByCodeAndUserTypeAndIdNot(saveReqDTO.getCode(), saveReqDTO.getUserType(), userId)) {
            throw exceptionFactory.resourceExistedException(MessageConst.User.USER_CODE_AND_USER_TYPE_EXISTED_ERROR,
                    Resources.USER,
                    MessageConst.RESOURCE_EXISTED, ErrorKey.User.CODE, saveReqDTO.getCode());
        }

    }

    /**
     * Find user by id and deleted_flag
     */
    private User findUserById(Long id) {
        return userRepository.findByIdAndDeletedFlag(id, DeletedFlag.NOT_YET_DELETED.getValue())
                .orElseThrow(() -> exceptionFactory.resourceNotFoundException(
                        MessageConst.User.USER_NOT_FOUND_ERROR_CODE, Resources.USER,
                        MessageConst.RESOURCE_NOT_FOUND, ErrorKey.User.ID, String.valueOf(id)));
    }

    /**
     * Generate random user's code
     */
    private String generateUserCode() {
        String baseCode = "";
        Random random = new Random();
        String generatedCode = baseCode + (random.nextInt(900000) + 100000);
        while (userRepository.existsByCode(generatedCode)) {
            generatedCode = baseCode + (random.nextInt(900000) + 100000);
        }
        return generatedCode;
    }

    /**
     * Generate new username and password for user
     */
    @Override
    public void generateUsernamePasswordEmail(User user) {
        StringBuilder usernameBuilder = new StringBuilder();
        String fullName = user.getFullName();
        String[] fullNameArr = Arrays.stream(fullName.trim().split(" "))
                .filter(org.apache.commons.lang3.ObjectUtils::isNotEmpty).toArray(String[]::new);
        if (fullNameArr.length > 0) {
            usernameBuilder
                    .append(StringUtils.convertVietnameseToEng(fullNameArr[fullNameArr.length - 1]).toLowerCase());
            for (int index = 0; index < fullNameArr.length - 1; index++) {
                usernameBuilder.append(StringUtils.convertVietnameseToEng(fullNameArr[index]).toLowerCase().charAt(0));
            }
        }
        // Set username
        // Integer userLikeExisted =
        // userRepository.countByUsername(usernameBuilder.toString());
        // usernameBuilder.append(userLikeExisted == 0 ? "" :
        // String.valueOf(userLikeExisted + 1));
        usernameBuilder.append(".").append(user.getCode());
        user.setUsername(usernameBuilder.toString());

        // password
        String password = user.getUsername() + "@" + DateUtils.asLocalDate(new Date()).getYear();
        user.setPasswordRaw(password);
        user.setPassword(passwordEncoder.encode(password));

        // email
        user.setEmail(usernameBuilder + "@" + defaultMailDomain);
    }

    @Override
    public Boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public void changePassword(ChangePasswordReqDTO reqDTO) {
        User user = findUserById(reqDTO.getUserId());
        // check changeType
        if (Objects.equals(reqDTO.getChangeType(), "RESET")) {
            user.setPassword(passwordEncoder.encode(reqDTO.getNewPassword()));
            user.setModifiedAt(DateUtils.getCurrentDateTime());
        } else if (Objects.equals(reqDTO.getChangeType(), "UPDATE")) {
            if (passwordEncoder.matches(reqDTO.getOldPassword(), user.getPassword())) {
                user.setPassword(passwordEncoder.encode(reqDTO.getNewPassword()));
                user.setModifiedAt(DateUtils.getCurrentDateTime());
            } else {
                throw new BadRequestException(MessageConst.User.USER_OLD_PASSWORD_NOT_MATCH,
                        MessageConst.RESOURCE_NOT_FOUND, ErrorKey.User.PASSWORD);
            }
        }
        // save to db
        userRepository.save(user);
    }
}
