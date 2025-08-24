package com.elearning.elearning_support.services.course.impl;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import org.apache.commons.math3.util.Pair;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.elearning.elearning_support.constants.FileConstants.Extension.Excel;
import com.elearning.elearning_support.constants.RoleConstants;
import com.elearning.elearning_support.constants.message.errorKey.ErrorKey;
import com.elearning.elearning_support.constants.message.messageConst.MessageConst;
import com.elearning.elearning_support.constants.message.messageConst.MessageConst.FileAttach;
import com.elearning.elearning_support.constants.message.messageConst.MessageConst.Resources;
import com.elearning.elearning_support.dtos.CustomInputStreamResource;
import com.elearning.elearning_support.dtos.common.ICommonIdCodeName;
import com.elearning.elearning_support.dtos.course.CourseSaveReqDTO;
import com.elearning.elearning_support.dtos.course.ICommonCourseDTO;
import com.elearning.elearning_support.dtos.course.ICourseDetailDTO;
import com.elearning.elearning_support.dtos.course.ICourseParticipantDTO;
import com.elearning.elearning_support.dtos.course.UserCourseDTO;
import com.elearning.elearning_support.dtos.fileAttach.importFile.ImportResponseDTO;
import com.elearning.elearning_support.dtos.fileAttach.importFile.RowErrorDTO;
import com.elearning.elearning_support.dtos.users.ImportUserValidatorDTO;
import com.elearning.elearning_support.dtos.users.importUser.ValidatedImportUserDTO;
import com.elearning.elearning_support.dtos.users.student.StudentImportDTO;
import com.elearning.elearning_support.entities.course.Course;
import com.elearning.elearning_support.entities.course.UserCourse;
import com.elearning.elearning_support.entities.users.User;
import com.elearning.elearning_support.entities.users.UserDepartment;
import com.elearning.elearning_support.entities.users.UserRole;
import com.elearning.elearning_support.enums.course.UserCourseRoleTypeEnum;
import com.elearning.elearning_support.enums.importFile.ImportResponseEnum;
import com.elearning.elearning_support.enums.importFile.StudentImportFieldMapping;
import com.elearning.elearning_support.enums.users.UserTypeEnum;
import com.elearning.elearning_support.exceptions.exceptionFactory.ExceptionFactory;
import com.elearning.elearning_support.repositories.postgres.course.CourseRepository;
import com.elearning.elearning_support.repositories.postgres.course.UserCourseRepository;
import com.elearning.elearning_support.repositories.postgres.department.DepartmentRepository;
import com.elearning.elearning_support.repositories.postgres.users.UserDepartmentRepository;
import com.elearning.elearning_support.repositories.postgres.users.UserRepository;
import com.elearning.elearning_support.repositories.postgres.users.UserRoleRepository;
import com.elearning.elearning_support.services.course.CourseService;
import com.elearning.elearning_support.services.users.UserService;
import com.elearning.elearning_support.utils.auth.AuthUtils;
import com.elearning.elearning_support.utils.excelFile.ExcelFileUtils;
import com.elearning.elearning_support.utils.file.FileUtils;
import com.elearning.elearning_support.utils.object.ObjectMapperUtils;
import com.elearning.elearning_support.utils.object.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;

    private final ExceptionFactory exceptionFactory;

    private final UserCourseRepository userCourseRepository;

    private final ExcelFileUtils excelFileUtils;

    private final UserRoleRepository userRoleRepository;

    private final UserRepository userRepository;

    private final UserService userService;

    private final PasswordEncoder passwordEncoder;

    private final DepartmentRepository departmentRepository;

    private final UserDepartmentRepository userDepartmentRepository;

    @Override
    public Long createCourse(CourseSaveReqDTO createDTO) {
        // Check course code existed
        if (courseRepository.existsByCode(createDTO.getCode())) {
            throw exceptionFactory.resourceExistedException(MessageConst.Course.EXISTED_BY_CODE, Resources.COURSE,
                    MessageConst.RESOURCE_EXISTED, ErrorKey.Course.CODE, createDTO.getCode());
        }

        // Tạo course
        Course course = new Course();
        BeanUtils.copyProperties(createDTO, course);
        course.setSubjectId(createDTO.getSubjectId());
        course.setSemesterId(createDTO.getSemesterId());
        course.setCreatedBy(AuthUtils.getCurrentUserId());
        course.setCreatedAt(new Date());
        course.setIsEnabled(Boolean.TRUE);
        course = courseRepository.save(course);

        // save course participants
        List<UserCourse> lstParticipant = new ArrayList<>();
        final Long courseId = course.getId();
        // Add students
        if (!org.springframework.util.ObjectUtils.isEmpty(createDTO.getLstStudentId())) {
            createDTO.getLstStudentId()
                    .forEach(studentId -> lstParticipant
                            .add(new UserCourse(studentId, courseId, UserCourseRoleTypeEnum.STUDENT.getType())));
        }
        // Add teachers
        if (!org.springframework.util.ObjectUtils.isEmpty(createDTO.getLstTeacherId())) {
            createDTO.getLstTeacherId().forEach(
                    supervisorId -> lstParticipant
                            .add(new UserCourse(supervisorId, courseId, UserCourseRoleTypeEnum.TEACHER.getType())));
        }
        // check lstParticipant is not empty -> save
        if (!org.springframework.util.ObjectUtils.isEmpty(lstParticipant)) {
            userCourseRepository.saveAll(lstParticipant);
        }

        return course.getId();
    }

    @Transactional
    @Override
    public void updateCourse(Long id, CourseSaveReqDTO updateDTO) {
        // Get course and test
        Course course = findCourseById(id);
        // check existed another class_code
        if (!Objects.equals(course.getCode(), updateDTO.getCode())
                && courseRepository.existsByCode(updateDTO.getCode())) {
            throw exceptionFactory.resourceExistedException(MessageConst.Course.EXISTED_BY_CODE, Resources.COURSE,
                    MessageConst.RESOURCE_EXISTED, updateDTO.getCode());
        }

        // Update course
        BeanUtils.copyProperties(updateDTO, course);
        course.setModifiedAt(new Date());
        course.setModifiedBy(AuthUtils.getCurrentUserId());
        courseRepository.save(course);

        // delete current user-course
        userCourseRepository.deleteAllByCourseId(id);
        List<UserCourse> lstUserCourse = new ArrayList<>();
        updateDTO.getLstStudentId().forEach(item -> lstUserCourse
                .add(new UserCourse(item, course.getId(), UserCourseRoleTypeEnum.STUDENT.getType())));
        updateDTO.getLstTeacherId().forEach(item -> lstUserCourse
                .add(new UserCourse(item, course.getId(), UserCourseRoleTypeEnum.TEACHER.getType())));
        userCourseRepository.saveAll(lstUserCourse);
    }

    @Override
    public Page<ICommonCourseDTO> getPageCourse(String code, Long semesterId, Long subjectId, Pageable pageable) {
        Long currentUserId = AuthUtils.isAdmin() ? Long.valueOf(-1L) : AuthUtils.getCurrentUserId();
        return courseRepository.getPageCourse(currentUserId, code, subjectId, semesterId, pageable);
    }

    @Override
    public CustomInputStreamResource exportListCourse(String code, Long semesterId, Long subjectId) throws IOException {
        Long currentUserId = AuthUtils.isAdmin() ? Long.valueOf(-1L) : AuthUtils.getCurrentUserId();
        List<ICommonCourseDTO> lstCourse = courseRepository.getListCourse(currentUserId, code, semesterId, subjectId);
        String fileName = String.format("Course_%s.xlsx", LocalDateTime.now());
        String sheetName = "course";
        if (!org.springframework.util.ObjectUtils.isEmpty(lstCourse)) {
            fileName = String.format("Course_%s_%s_%s.xlsx", lstCourse.get(0).getSemester(),
                    lstCourse.get(0).getSubjectCode(),
                    LocalDateTime.now());
            sheetName = fileName;
        }
        Map<Integer, Pair<String, String>> structure = new LinkedHashMap<>();
        structure.put(1, Pair.create("Mã lớp học", "getCode"));
        structure.put(2, Pair.create("Phòng học", "getRoomName"));
        structure.put(3, Pair.create("Kỳ học", "getSemester"));
        structure.put(4, Pair.create("Môn học", "getSubjectTitle"));
        structure.put(5, Pair.create("Giờ học", "getCourseTime"));
        structure.put(6, Pair.create("Tuần học", "getCourseWeeks"));
        structure.put(7, Pair.create("Số lượng sinh viên", "getNumberOfStudents"));
        return new CustomInputStreamResource(fileName, excelFileUtils.createWorkbook(lstCourse, structure, sheetName));
    }

    @Override
    public ICourseDetailDTO getCourseDetails(Long id) {
        ICourseDetailDTO courseDetails = courseRepository.getCourseDetails(id);
        if (Objects.isNull(courseDetails)) {
            throw exceptionFactory.resourceNotFoundException(MessageConst.Course.NOT_FOUND, Resources.COURSE,
                    MessageConst.RESOURCE_NOT_FOUND, ErrorKey.Course.ID, String.valueOf(id));
        }
        return courseDetails;
    }

    @Override
    public void updateCourseParticipant(UserCourseDTO userCourseDTO) {
        // delete all -> add new
        userCourseRepository.deleteAllByCourseId(userCourseDTO.getCourseId());
        List<UserCourse> lstNewParticipant = userCourseDTO.getLstParticipant().stream()
                .map(item -> new UserCourse(item.getUserId(), userCourseDTO.getCourseId(), item.getRole().getType()))
                .collect(Collectors.toList());
        userCourseRepository.saveAll(lstNewParticipant);
    }

    @Override
    public List<ICourseParticipantDTO> getListCourseParticipant(Long courseId, UserCourseRoleTypeEnum roleType) {
        return courseRepository.getListCourseParticipants(courseId, roleType.getType());
    }

    @Override
    public CustomInputStreamResource exportCourseParticipant(Long courseId, UserCourseRoleTypeEnum roleType)
            throws IOException {
        List<ICourseParticipantDTO> participants = courseRepository.getListCourseParticipants(courseId,
                roleType.getType());
        // Create export structure
        String sheetName = org.springframework.util.ObjectUtils.isEmpty(participants) ? "result"
                : participants.get(0).getCourseCode();
        Map<Integer, Pair<String, String>> structure = new LinkedHashMap<>();
        structure.put(1, Pair.create("Họ tên", "getName"));
        structure.put(2, Pair.create("Mã", "getCode"));
        structure.put(3, Pair.create("Vai trò trong lớp học", "getRoleName"));
        structure.put(4, Pair.create("Mã lớp học", "getCourseCode"));
        String exportObject = roleType == UserCourseRoleTypeEnum.STUDENT ? "student" : "teacher";
        String fileName = String.format("Course_%s_%s.xlsx", exportObject, LocalDateTime.now());
        return new CustomInputStreamResource(fileName,
                excelFileUtils.createWorkbook(participants, structure, sheetName));
    }

    @Override
    public Set<Long> importStudentCourse(Long courseId, MultipartFile fileImport) throws IOException {
        // check existed course
        Course course = findCourseById(courseId);
        Set<Long> lstCourseParticipantIds = courseRepository.getListCourseParticipantId(course.getId(),
                UserCourseRoleTypeEnum.STUDENT.getType());
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
            Set<Long> lstExistedStudentId = new HashSet<>();

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
                        org.apache.commons.beanutils.BeanUtils.setProperty(importDTO, objectFieldKey, cellValue);
                    }
                }
                // Validate và mapping vào entity
                User newStudent = new User(importDTO);
                if (!newStudent.getFullName().isEmpty()) {
                    userService.generateUsernamePasswordEmail(newStudent);
                    importDTO.setUsername(org.springframework.util.ObjectUtils.isEmpty(importDTO.getUsername()) ? newStudent.getUsername()
                            : importDTO.getUsername());
                    importDTO.setEmail(org.springframework.util.ObjectUtils.isEmpty(importDTO.getUsername()) ? newStudent.getEmail()
                            : importDTO.getEmail());
                    importDTO.setPasswordRaw(
                            org.springframework.util.ObjectUtils.isEmpty(importDTO.getPasswordRaw()) ? newStudent.getPasswordRaw()
                                    : importDTO.getPasswordRaw());
                }
                List<String> causeList = new ArrayList<>();
                ValidatedImportUserDTO validatedResult = userService.validateImportUser(validatorDTO, importDTO,
                        causeList);
                if (!isEmptyRow) {
                    if (org.springframework.util.ObjectUtils.isEmpty(causeList)) {
                        newStudent.setUsername(importDTO.getUsername());
                        newStudent.setEmail(importDTO.getEmail());
                        newStudent.setPassword(passwordEncoder.encode(importDTO.getPasswordRaw()));
                        newStudent.setMetaData(ObjectMapperUtils.mapping(
                                String.format("{\"courseNum\" : %d}",
                                        Integer.valueOf(ObjectUtils.getOrDefault(importDTO.getCourseRaw(), "0"))),
                                Object.class));
                        lstNewStudent.add(newStudent);
                        // add username/email/code to validators
                        validatorDTO.getLstExistedUsername().add(newStudent.getUsername());
                        validatorDTO.getLstExistedEmail().add(newStudent.getEmail());
                        validatorDTO.getLstExistedCode().add(newStudent.getCode());
                    } else {
                        // if duplicated data and data has already been valid
                        if (validatedResult.getHasDuplicatedField() && !validatedResult.getMissedRequiredField()
                                && !validatedResult.getHasInvalidFormatField()) {
                            Set<Long> existedStudentIds = userRepository.findStudentByUniqueInfo(importDTO.getCode(),
                                    importDTO.getEmail(), importDTO.getUsername());
                            if (! org.springframework.util.ObjectUtils.isEmpty(existedStudentIds)) {
                                lstExistedStudentId.addAll(existedStudentIds);
                            }
                        }
                        response.getErrorRows().add(new RowErrorDTO(currentRow.getRowNum() + 1, importDTO, causeList));
                        response.setMessage(ImportResponseEnum.EXIST_INVALID_DATA.getMessage());
                        response.setStatus(ImportResponseEnum.EXIST_INVALID_DATA.getStatus());
                    }
                }
            }

            // closed stream to avoid leaking
            inputWorkbook.close();

            // List new students imported
            lstNewStudent = userRepository.saveAll(lstNewStudent);
            List<UserRole> lstStudentUserRole = lstNewStudent.stream()
                    .map(student -> new UserRole(student.getId(), RoleConstants.ROLE_STUDENT_ID))
                    .collect(Collectors.toList());
            userRoleRepository.saveAll(lstStudentUserRole);

            // add to course: both new and existed students
            lstExistedStudentId.removeIf(lstCourseParticipantIds::contains);
            List<UserCourse> lstStudentCourse = lstNewStudent.stream()
                    .map(student -> new UserCourse(student.getId(), course.getId(),
                            UserCourseRoleTypeEnum.STUDENT.getType()))
                    .collect(Collectors.toList());
            lstExistedStudentId.forEach(studentId -> lstStudentCourse
                    .add(new UserCourse(studentId, course.getId(), UserCourseRoleTypeEnum.STUDENT.getType())));
            userCourseRepository.saveAll(lstStudentCourse);

            // map user_department
            List<UserDepartment> lstUserDepartment = lstNewStudent.stream()
                    .map(student -> new UserDepartment(student.getId(), student.getDepartmentId())).collect(
                            Collectors.toList());
            userDepartmentRepository.saveAll(lstUserDepartment);

            // return list studentId
            return lstStudentCourse.stream().map(UserCourse::getUserId).collect(Collectors.toSet());
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

    /**
     * Find by id and enabled
     */
    private Course findCourseById(Long id) {
        return courseRepository.findByIdAndIsEnabled(id, Boolean.TRUE)
                .orElseThrow(() -> exceptionFactory.resourceNotFoundException(MessageConst.Course.NOT_FOUND,
                        Resources.COURSE,
                        MessageConst.RESOURCE_NOT_FOUND, ErrorKey.Course.ID, String.valueOf(id)));
    }
}
