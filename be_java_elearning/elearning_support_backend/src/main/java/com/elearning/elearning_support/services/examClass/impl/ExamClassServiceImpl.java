package com.elearning.elearning_support.services.examClass.impl;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
import org.apache.commons.lang3.RandomUtils;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import com.elearning.elearning_support.constants.FileConstants.Extension.Excel;
import com.elearning.elearning_support.constants.RoleConstants;
import com.elearning.elearning_support.constants.SystemConstants;
import com.elearning.elearning_support.constants.message.errorKey.ErrorKey;
import com.elearning.elearning_support.constants.message.messageConst.MessageConst;
import com.elearning.elearning_support.constants.message.messageConst.MessageConst.Course;
import com.elearning.elearning_support.constants.message.messageConst.MessageConst.FileAttach;
import com.elearning.elearning_support.constants.message.messageConst.MessageConst.Resources;
import com.elearning.elearning_support.dtos.CustomInputStreamResource;
import com.elearning.elearning_support.dtos.common.ICommonIdCodeName;
import com.elearning.elearning_support.dtos.examClass.ExamClassCreateDTO;
import com.elearning.elearning_support.dtos.examClass.ExamClassSaveReqDTO;
import com.elearning.elearning_support.dtos.examClass.ICommonExamClassDTO;
import com.elearning.elearning_support.dtos.examClass.IExamClassDetailDTO;
import com.elearning.elearning_support.dtos.examClass.IExamClassParticipantDTO;
import com.elearning.elearning_support.dtos.examClass.UserExamClassDTO;
import com.elearning.elearning_support.dtos.fileAttach.importFile.ImportResponseDTO;
import com.elearning.elearning_support.dtos.fileAttach.importFile.RowErrorDTO;
import com.elearning.elearning_support.dtos.mail.MailBaseReqDTO;
import com.elearning.elearning_support.dtos.users.ImportUserValidatorDTO;
import com.elearning.elearning_support.dtos.users.importUser.ValidatedImportUserDTO;
import com.elearning.elearning_support.dtos.users.student.StudentImportDTO;
import com.elearning.elearning_support.entities.course.UserCourse;
import com.elearning.elearning_support.entities.examClass.ExamClass;
import com.elearning.elearning_support.entities.examClass.UserExamClass;
import com.elearning.elearning_support.entities.mail.Mail;
import com.elearning.elearning_support.entities.studentTest.StudentTestSet;
import com.elearning.elearning_support.entities.test.Test;
import com.elearning.elearning_support.entities.users.User;
import com.elearning.elearning_support.entities.users.UserDepartment;
import com.elearning.elearning_support.entities.users.UserRole;
import com.elearning.elearning_support.enums.commons.TimeIntervalEnum;
import com.elearning.elearning_support.enums.course.UserCourseRoleTypeEnum;
import com.elearning.elearning_support.enums.examClass.UserExamClassRoleEnum;
import com.elearning.elearning_support.enums.importFile.ImportResponseEnum;
import com.elearning.elearning_support.enums.importFile.StudentImportFieldMapping;
import com.elearning.elearning_support.enums.mail.MailTemplateEnum;
import com.elearning.elearning_support.enums.test.StudentTestStatusEnum;
import com.elearning.elearning_support.enums.test.TestTypeEnum;
import com.elearning.elearning_support.enums.users.UserTypeEnum;
import com.elearning.elearning_support.exceptions.BadRequestException;
import com.elearning.elearning_support.exceptions.exceptionFactory.ExceptionFactory;
import com.elearning.elearning_support.repositories.postgres.course.CourseRepository;
import com.elearning.elearning_support.repositories.postgres.course.UserCourseRepository;
import com.elearning.elearning_support.repositories.postgres.department.DepartmentRepository;
import com.elearning.elearning_support.repositories.postgres.examClass.ExamClassRepository;
import com.elearning.elearning_support.repositories.postgres.examClass.UserExamClassRepository;
import com.elearning.elearning_support.repositories.postgres.studentTestSet.StudentTestSetRepository;
import com.elearning.elearning_support.repositories.postgres.test.test_set.TestSetRepository;
import com.elearning.elearning_support.repositories.postgres.users.UserDepartmentRepository;
import com.elearning.elearning_support.repositories.postgres.users.UserRepository;
import com.elearning.elearning_support.repositories.postgres.users.UserRoleRepository;
import com.elearning.elearning_support.services.examClass.ExamClassService;
import com.elearning.elearning_support.services.mail.MailService;
import com.elearning.elearning_support.services.studentTestSet.StudentTestSetService;
import com.elearning.elearning_support.services.test.TestService;
import com.elearning.elearning_support.services.users.UserService;
import com.elearning.elearning_support.utils.DateUtils;
import com.elearning.elearning_support.utils.auth.AuthUtils;
import com.elearning.elearning_support.utils.excelFile.ExcelFileUtils;
import com.elearning.elearning_support.utils.file.FileUtils;
import com.elearning.elearning_support.utils.object.ObjectMapperUtils;
import com.elearning.elearning_support.utils.object.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExamClassServiceImpl implements ExamClassService {

        private final ExamClassRepository examClassRepository;

        private final ExceptionFactory exceptionFactory;

        private final UserExamClassRepository userExamClassRepository;

        private final TestService testService;

        private final ExcelFileUtils excelFileUtils;

        private final UserRoleRepository userRoleRepository;

        private final UserRepository userRepository;

        private final UserService userService;

        private final PasswordEncoder passwordEncoder;

        private final UserCourseRepository userCourseRepository;

        private final CourseRepository courseRepository;

        private final StudentTestSetRepository studentTestSetRepository;

        private final TestSetRepository testSetRepository;

        private final DepartmentRepository departmentRepository;

        private final UserDepartmentRepository userDepartmentRepository;

        private final MailService mailService;

        private final StudentTestSetService studentTestSetService;

        @Transactional
        @Override
        public Long createExamClass(ExamClassCreateDTO createDTO) {
                Long currentUserId = AuthUtils.getCurrentUserId();
                // Check exam_class code existed
                if (examClassRepository.existsByCode(createDTO.getCode())) {
                        throw exceptionFactory.resourceExistedException(MessageConst.ExamClass.EXISTED_BY_CODE,
                                        Resources.EXAM_CLASS,
                                        MessageConst.RESOURCE_EXISTED,
                                        ErrorKey.ExamClass.CODE, createDTO.getCode());
                }
                Test test = testService.findTestById(createDTO.getTestId());

                // Tạo exam_class
                ExamClass newExamClass = new ExamClass();
                BeanUtils.copyProperties(createDTO, newExamClass);
                newExamClass.setTestId(test.getId());
                newExamClass.setSubjectId(test.getSubjectId());
                newExamClass.setSemesterId(test.getSemesterId());
                newExamClass.setCreatedBy(currentUserId);
                newExamClass.setCreatedAt(new Date());
                newExamClass.setIsEnabled(Boolean.TRUE);
                newExamClass.setTestType(test.getTestType());
                newExamClass.setSubmitTime(
                                DateUtils.calculateDateByInterval(createDTO.getExamineTime(), test.getDuration(),
                                                TimeIntervalEnum.MINUTE.name()));
                newExamClass = examClassRepository.save(newExamClass);

                // save exam_class participants
                List<UserExamClass> lstParticipant = new ArrayList<>();
                final Long examClassId = newExamClass.getId();
                if (Objects.nonNull(createDTO.getCourseId())) {
                        // Check course's id existed
                        if (!courseRepository.existsById(createDTO.getCourseId())) {
                                throw exceptionFactory.resourceNotFoundException(Course.NOT_FOUND,
                                                MessageConst.RESOURCE_NOT_FOUND, Resources.COURSE,
                                                ErrorKey.Course.ID, String.valueOf(createDTO.getCourseId()));
                        }
                        // add only students
                        List<UserCourse> lstUserCourse = userCourseRepository.findAllByCourseIdAndRoleType(
                                        createDTO.getCourseId(),
                                        UserCourseRoleTypeEnum.STUDENT.getType());
                        lstUserCourse.forEach(
                                        item -> lstParticipant.add(new UserExamClass(item.getUserId(), examClassId,
                                                        UserExamClassRoleEnum.STUDENT.getType())));
                } else {
                        Set<Long> lstAddedStudentId = new HashSet<>();
                        // Add students
                        if (!org.springframework.util.ObjectUtils.isEmpty(createDTO.getLstStudentId())) {
                                lstAddedStudentId.addAll(createDTO.getLstStudentId());
                        }
                        if (Objects.nonNull(createDTO.getFromExamClassId())) {
                                lstAddedStudentId.addAll(examClassRepository.getListExamClassParticipantId(
                                                createDTO.getFromExamClassId(),
                                                UserExamClassRoleEnum.STUDENT.getType()));
                        }
                        lstAddedStudentId.forEach(
                                        studentId -> lstParticipant.add(new UserExamClass(studentId, examClassId,
                                                        UserExamClassRoleEnum.STUDENT.getType())));

                        // Add mapping supervisors, if current is a teacher -> add to the list of
                        // supervisors
                        if (AuthUtils.isBaseTeacher()) {
                                createDTO.getLstSupervisorId().add(currentUserId);
                        }
                        // phân biệt supervisor và lecturer (supervisor++)
                        if (!org.springframework.util.ObjectUtils.isEmpty(createDTO.getLstSupervisorId())) {
                                Set<Long> supervisorIds = createDTO.getLstSupervisorId().stream()
                                                .filter(item -> !createDTO.getLstLecturerId().contains(item)).collect(
                                                                Collectors.toSet());
                                supervisorIds.forEach(
                                                supervisorId -> lstParticipant.add(
                                                                new UserExamClass(supervisorId, examClassId,
                                                                                UserExamClassRoleEnum.SUPERVISOR
                                                                                                .getType())));
                        }
                        if (!org.springframework.util.ObjectUtils.isEmpty(createDTO.getLstLecturerId())) {
                                createDTO.getLstLecturerId().forEach(
                                                lecturerId -> lstParticipant.add(
                                                                new UserExamClass(lecturerId, examClassId,
                                                                                UserExamClassRoleEnum.LECTURER
                                                                                                .getType())));
                        }
                }
                userExamClassRepository.saveAll(lstParticipant);

                return newExamClass.getId();
        }

        @Transactional
        @Override
        public void updateExamClass(Long id, ExamClassSaveReqDTO updateDTO) {
                // Get exam class and test
                ExamClass examClass = findExamClassById(id);
                // check existed another class_code
                if (!Objects.equals(examClass.getCode(), updateDTO.getCode())
                                && examClassRepository.existsByCode(updateDTO.getCode())) {
                        throw exceptionFactory.resourceExistedException(MessageConst.ExamClass.EXISTED_BY_CODE,
                                        Resources.EXAM_CLASS,
                                        MessageConst.RESOURCE_EXISTED, updateDTO.getCode());
                }
                // check existed test_id
                if (!Objects.equals(examClass.getTestId(), updateDTO.getTestId())) {
                        testService.existsById(updateDTO.getTestId());
                }

                // Update exam class
                BeanUtils.copyProperties(updateDTO, examClass);
                examClass.setTestType(updateDTO.getTestType().getType());
                examClass.setModifiedAt(new Date());
                examClass.setModifiedBy(AuthUtils.getCurrentUserId());
                examClassRepository.save(examClass);

                // delete current user-exam class
                userExamClassRepository.deleteAllByExamClassId(id);
                List<UserExamClass> lstUserExamClass = new ArrayList<>();
                // Add student
                if (!org.springframework.util.ObjectUtils.isEmpty(updateDTO.getLstStudentId())) {
                        updateDTO.getLstStudentId().forEach(item -> lstUserExamClass
                                        .add(new UserExamClass(item, id, UserExamClassRoleEnum.STUDENT.getType())));
                }
                // Add lecturer/supervisor
                if (!org.springframework.util.ObjectUtils.isEmpty(updateDTO.getLstSupervisorId())) {
                        Set<Long> supervisorIds = updateDTO.getLstSupervisorId().stream()
                                        .filter(item -> !updateDTO.getLstLecturerId().contains(item)).collect(
                                                        Collectors.toSet());
                        supervisorIds.forEach(
                                        supervisorId -> lstUserExamClass.add(
                                                        new UserExamClass(supervisorId, examClass.getId(),
                                                                        UserExamClassRoleEnum.SUPERVISOR.getType())));
                }
                if (!org.springframework.util.ObjectUtils.isEmpty(updateDTO.getLstLecturerId())) {
                        updateDTO.getLstLecturerId().forEach(
                                        lecturerId -> lstUserExamClass.add(
                                                        new UserExamClass(lecturerId, examClass.getId(),
                                                                        UserExamClassRoleEnum.LECTURER.getType())));
                }
                userExamClassRepository.saveAll(lstUserExamClass);
        }

        @Override
        public Page<ICommonExamClassDTO> getPageExamClass(String code, Long semesterId, Long subjectId, Long testId,
                        Pageable pageable) {
                // if teacher -> get viewableSubjectIds
                String roleBased = AuthUtils.getUserBasedRole();
                Long currentUserId;
                if (RoleConstants.ROLE_ADMIN.equals(roleBased)) {
                        currentUserId = -1L;
                } else {
                        currentUserId = AuthUtils.getCurrentUserId();
                }
                return examClassRepository.getPageExamClass(currentUserId, code, semesterId, subjectId, testId,
                                pageable);
        }

        @Override
        public CustomInputStreamResource exportListExamClass(Long semesterId, Long testId, Long subjectId)
                        throws IOException {
                // get exported data
                Boolean isAdmin = AuthUtils.isAdmin();
                Long currentUserId = isAdmin ? Long.valueOf(-1L) : AuthUtils.getCurrentUserId();
                List<ICommonExamClassDTO> lstExamClass = examClassRepository.getListExamClass(currentUserId, "",
                                semesterId, subjectId, testId);
                // export data
                String fileName = String.format("ExamClass_%s.xlsx", LocalDateTime.now());
                String sheetName = "exam_class";
                if (!org.springframework.util.ObjectUtils.isEmpty(lstExamClass)) {
                        fileName = String.format("ExamClass_%s_%s_%s.xlsx", lstExamClass.get(0).getSemester(),
                                        lstExamClass.get(0).getTestName(),
                                        LocalDateTime.now());
                        sheetName = fileName;
                }
                Map<Integer, Pair<String, String>> structure = new LinkedHashMap<>();
                structure.put(1, Pair.create("Mã lớp thi", "getCode"));
                structure.put(2, Pair.create("Kỳ thi", "getTestName"));
                structure.put(3, Pair.create("Phòng thi", "getRoomName"));
                structure.put(4, Pair.create("Kỳ học", "getSemester"));
                structure.put(5, Pair.create("Môn thi", "getSubjectTitle"));
                structure.put(6, Pair.create("Hình thức thi", "getTestType"));
                structure.put(7, Pair.create("Ngày thi", "getExamineDate"));
                structure.put(8, Pair.create("Thời gian thi", "getExamineTime"));
                structure.put(9, Pair.create("Số lượng thí sinh", "getNumberOfStudents"));
                return new CustomInputStreamResource(fileName,
                                excelFileUtils.createWorkbook(lstExamClass, structure, sheetName));
        }

        @Override
        public IExamClassDetailDTO getExamClassDetail(Long id) {
                IExamClassDetailDTO examClassDetails = examClassRepository.getDetailExamClass(id);
                if (Objects.isNull(examClassDetails)) {
                        throw exceptionFactory.resourceNotFoundException(MessageConst.ExamClass.NOT_FOUND,
                                        Resources.EXAM_CLASS,
                                        MessageConst.RESOURCE_NOT_FOUND, ErrorKey.ExamClass.ID, String.valueOf(id));
                }
                return examClassDetails;
        }

        @Transactional
        @Override
        public void updateParticipantToExamClass(UserExamClassDTO userExamClassDTO) {
                // delete all -> add new
                userExamClassRepository.deleteAllByExamClassId(userExamClassDTO.getExamClassId());
                List<UserExamClass> lstNewParticipant = userExamClassDTO.getLstParticipant().stream()
                                .map(item -> new UserExamClass(item.getUserId(), userExamClassDTO.getExamClassId(),
                                                item.getRole().getType()))
                                .collect(Collectors.toList());
                userExamClassRepository.saveAll(lstNewParticipant);
        }

        @Override
        public List<IExamClassParticipantDTO> getListExamClassParticipant(Long examClassId,
                        UserExamClassRoleEnum roleType) {
                Set<Integer> roleTypes = Objects.equals(roleType, UserExamClassRoleEnum.SUPERVISOR) ? new HashSet<>(
                                Arrays.asList(UserExamClassRoleEnum.SUPERVISOR.getType(),
                                                UserExamClassRoleEnum.LECTURER.getType()))
                                : Collections.singleton(roleType.getType());
                return examClassRepository.getListExamClassParticipant(examClassId, roleTypes);
        }

        @Override
        public CustomInputStreamResource exportExamClassParticipant(Long examClassId, UserExamClassRoleEnum roleType)
                        throws IOException {
                Set<Integer> roleTypes = Objects.equals(roleType, UserExamClassRoleEnum.SUPERVISOR) ? new HashSet<>(
                                Arrays.asList(UserExamClassRoleEnum.SUPERVISOR.getType(),
                                                UserExamClassRoleEnum.LECTURER.getType()))
                                : Collections.singleton(roleType.getType());
                List<IExamClassParticipantDTO> participants = examClassRepository
                                .getListExamClassParticipant(examClassId, roleTypes);
                // Create export structure
                String sheetName = org.springframework.util.ObjectUtils.isEmpty(participants) ? "result"
                                : participants.get(0).getExamClassCode();
                Map<Integer, Pair<String, String>> structure = new LinkedHashMap<>();
                structure.put(1, Pair.create("Họ tên", "getName"));
                structure.put(2, Pair.create("Mã", "getCode"));
                structure.put(3, Pair.create("Vai trò trong lớp thi", "getRoleName"));
                structure.put(4, Pair.create("Mã lớp thi", "getExamClassCode"));
                String exportObject = roleType == UserExamClassRoleEnum.STUDENT ? "student" : "supervisor";
                String fileName = String.format("ExamClass_%s_%s.xlsx", exportObject, LocalDateTime.now());
                return new CustomInputStreamResource(fileName,
                                excelFileUtils.createWorkbook(participants, structure, sheetName));
        }

        /**
         * Find by id and enabled
         */
        private ExamClass findExamClassById(Long id) {
                return examClassRepository.findByIdAndIsEnabled(id, Boolean.TRUE)
                                .orElseThrow(() -> exceptionFactory.resourceNotFoundException(
                                                MessageConst.ExamClass.NOT_FOUND, Resources.EXAM_CLASS,
                                                MessageConst.RESOURCE_NOT_FOUND, ErrorKey.ExamClass.ID,
                                                String.valueOf(id)));
        }

        /**
         * Find by id, test_type and is_enabled
         */
        private ExamClass findExamClassByIdAndTestType(Long id, TestTypeEnum testType) {
                return examClassRepository.findByIdAndTestTypeAndIsEnabled(id, testType.getType(), Boolean.TRUE)
                                .orElseThrow(() -> exceptionFactory.resourceNotFoundException(
                                                MessageConst.ExamClass.NOT_FOUND_BY_TYPE, Resources.EXAM_CLASS,
                                                MessageConst.RESOURCE_NOT_FOUND, ErrorKey.ExamClass.ID,
                                                id + "-" + testType.name()));
        }

        @Transactional
        @Override
        public Set<Long> importStudentExamClass(Long examClassId, MultipartFile fileImport) throws IOException {
                // check existed exam class
                ExamClass examClass = findExamClassById(examClassId);
                Set<Long> lstExamClassParticipantIds = examClassRepository.getListExamClassParticipantId(
                                examClass.getId(),
                                UserExamClassRoleEnum.STUDENT.getType());
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
                        ImportUserValidatorDTO validatorDTO = new ImportUserValidatorDTO(
                                        userRepository.getLstCurrentUsername(),
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
                                                throw exceptionFactory.fileUploadException(
                                                                FileAttach.FILE_EXCEL_MISSING_COLUMN_NUMBER_ERROR,
                                                                Resources.FILE_ATTACHED,
                                                                MessageConst.UPLOAD_FAILED);
                                        }
                                        if (currentRow.getLastCellNum() > numberOfColumns) {
                                                throw exceptionFactory.fileUploadException(
                                                                FileAttach.FILE_EXCEL_MISSING_COLUMN_NUMBER_ERROR,
                                                                Resources.FILE_ATTACHED,
                                                                MessageConst.UPLOAD_FAILED);
                                        }
                                        for (Cell cell : currentRow) {
                                                // Validate tên cột
                                                String columnKey = ExcelFileUtils.getStringCellValue(cell);
                                                if (Objects.isNull(StudentImportFieldMapping
                                                                .getObjectFieldByColumnKey(columnKey))) {
                                                        throw exceptionFactory.fileUploadException(
                                                                        FileAttach.FILE_EXCEL_INVALID_COLUMN_NAME,
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
                                        String objectFieldKey = StudentImportFieldMapping
                                                        .getObjectFieldByColumnKey(columnKey);
                                        String cellValue = ExcelFileUtils.getStringCellValue(cell);
                                        if (!org.springframework.util.ObjectUtils.isEmpty(cellValue)) {
                                                isEmptyRow = false;
                                        }
                                        if (!org.springframework.util.ObjectUtils.isEmpty(objectFieldKey)) {
                                                org.apache.commons.beanutils.BeanUtils.setProperty(importDTO,
                                                                objectFieldKey, cellValue);
                                        }
                                }
                                // Validate và mapping vào entity
                                User newStudent = new User(importDTO);
                                if (!newStudent.getFullName().isEmpty()) {
                                        userService.generateUsernamePasswordEmail(newStudent);
                                        importDTO.setUsername(org.springframework.util.ObjectUtils
                                                        .isEmpty(importDTO.getUsername()) ? newStudent.getUsername()
                                                                        : importDTO.getUsername());
                                        importDTO.setEmail(org.springframework.util.ObjectUtils
                                                        .isEmpty(importDTO.getUsername()) ? newStudent.getEmail()
                                                                        : importDTO.getEmail());
                                        importDTO.setPasswordRaw(
                                                        org.springframework.util.ObjectUtils
                                                                        .isEmpty(importDTO.getPasswordRaw())
                                                                                        ? newStudent.getPasswordRaw()
                                                                                        : importDTO.getPasswordRaw());
                                }
                                if (!isEmptyRow) {
                                        List<String> causeList = new ArrayList<>();
                                        ValidatedImportUserDTO validatedResult = userService
                                                        .validateImportUser(validatorDTO, importDTO, causeList);
                                        if (org.springframework.util.ObjectUtils.isEmpty(causeList)) {
                                                newStudent.setUsername(importDTO.getUsername());
                                                newStudent.setEmail(importDTO.getEmail());
                                                newStudent.setPassword(
                                                                passwordEncoder.encode(importDTO.getPasswordRaw()));
                                                newStudent.setMetaData(ObjectMapperUtils.mapping(
                                                                String.format("{\"courseNum\" : %d}", Integer.valueOf(
                                                                                ObjectUtils.getOrDefault(importDTO
                                                                                                .getCourseRaw(), "0"))),
                                                                Object.class));
                                                newStudent.setDepartmentId(mapDepartments
                                                                .get(importDTO.getDepartmentCode()).getId());
                                                lstNewStudent.add(newStudent);
                                                // add username/email/code to validators
                                                validatorDTO.getLstExistedUsername().add(newStudent.getUsername());
                                                validatorDTO.getLstExistedEmail().add(newStudent.getEmail());
                                                validatorDTO.getLstExistedCode().add(newStudent.getCode());
                                        } else {
                                                // if duplicated data and data has already been valid
                                                if (validatedResult.getHasDuplicatedField()
                                                                && !validatedResult.getMissedRequiredField() &&
                                                                !validatedResult.getHasInvalidFormatField()) {
                                                        Set<Long> existedStudentIds = userRepository
                                                                        .findStudentByUniqueInfo(importDTO.getCode(),
                                                                                        importDTO.getEmail(),
                                                                                        importDTO.getUsername());
                                                        if (!org.springframework.util.ObjectUtils
                                                                        .isEmpty(existedStudentIds)) {
                                                                lstExistedStudentId.addAll(existedStudentIds);
                                                        }
                                                }
                                                response.getErrorRows().add(new RowErrorDTO(currentRow.getRowNum() + 1,
                                                                importDTO, causeList));
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

                        // add to exam class: both new and existed students
                        lstExistedStudentId.removeIf(lstExamClassParticipantIds::contains);
                        List<UserExamClass> lstStudentExamClass = lstNewStudent.stream()
                                        .map(student -> new UserExamClass(student.getId(), examClass.getId(),
                                                        UserExamClassRoleEnum.STUDENT.getType()))
                                        .collect(Collectors.toList());
                        lstExistedStudentId.forEach(
                                        studentId -> lstStudentExamClass.add(new UserExamClass(studentId,
                                                        examClass.getId(), UserExamClassRoleEnum.STUDENT.getType())));
                        userExamClassRepository.saveAll(lstStudentExamClass);

                        // map user_department
                        List<UserDepartment> lstUserDepartment = lstNewStudent.stream()
                                        .map(student -> new UserDepartment(student.getId(), student.getDepartmentId()))
                                        .collect(
                                                        Collectors.toList());
                        userDepartmentRepository.saveAll(lstUserDepartment);

                        // return list studentId
                        return lstStudentExamClass.stream().map(UserExamClass::getUserId).collect(Collectors.toSet());
                } catch (IOException ioException) {
                        response.setMessage(ImportResponseEnum.IO_ERROR.getMessage());
                        response.setStatus(ImportResponseEnum.IO_ERROR.getStatus());
                } catch (Exception exception) {
                        if (Objects.nonNull(inputWorkbook)) {
                                inputWorkbook.close();
                        }
                        response.setMessage(ImportResponseEnum.UNKNOWN_ERROR.getMessage());
                        response.setStatus(ImportResponseEnum.UNKNOWN_ERROR.getStatus());
                        log.error(MessageConst.EXCEPTION_LOG_FORMAT, exception.getMessage(), exception.getCause(),
                                        exception);
                }
                return null;
        }

        @Transactional
        @Override
        public void assignOnlineTestSet(Long examClassId) {
                // get exam_class
                ExamClass examClass = findExamClassByIdAndTestType(examClassId, TestTypeEnum.ONLINE);
                // check if exists a row is not 'OPEN'
                if (studentTestSetRepository.existsByExamClassIdAndStatusNot(examClass.getId(),
                                StudentTestStatusEnum.OPEN.getType())) {
                        throw new BadRequestException(MessageConst.StudentTestSet.EXISTED_NOT_OPEN_IN_EXAM_CLASS,
                                        MessageConst.RESOURCE_EXISTED,
                                        ErrorKey.StudentTestSet.STATUS, "OPEN");
                }

                // get test
                Test test = testService.findTestById(examClass.getTestId());
                // list exam_class's participants (students)
                Set<Long> lstStudentId = examClassRepository.getListExamClassParticipantId(examClassId,
                                UserExamClassRoleEnum.STUDENT.getType());
                if (org.springframework.util.ObjectUtils.isEmpty(lstStudentId)) {
                        return;
                }
                // list test_set
                List<Long> lstTestSetId = new ArrayList<>(
                                testSetRepository.getListTestSetIdByTestId(examClass.getTestId()));
                if (org.springframework.util.ObjectUtils.isEmpty(lstTestSetId)) {
                        return;
                }

                // delete all by exam_class_id and status = open
                studentTestSetRepository.deleteAllByExamClassIdAndStatus(examClass.getId(),
                                StudentTestStatusEnum.OPEN.getType());
                // lst StudentTestSet
                List<StudentTestSet> lstStudentTestSet = new ArrayList<>();
                final Long currentUserId = AuthUtils.getCurrentUserId();
                lstStudentId.forEach(stdId -> {
                        final Long testSetId = lstTestSetId.get(RandomUtils.nextInt(0, lstTestSetId.size() - 1));
                        StudentTestSet item = StudentTestSet.builder()
                                        .studentId(stdId)
                                        .testSetId(testSetId)
                                        .examClassId(examClass.getId())
                                        .isEnabled(Boolean.TRUE)
                                        .isSubmitted(Boolean.FALSE)
                                        .isPublished(Boolean.FALSE)
                                        .status(StudentTestStatusEnum.OPEN.getType())
                                        .allowedStartTime(examClass.getExamineTime())
                                        .allowedSubmitTime(DateUtils.calculateDateByInterval(examClass.getExamineTime(),
                                                        test.getDuration(), TimeIntervalEnum.MINUTE.name()))
                                        .testDate(examClass.getExamineTime())
                                        .testType(examClass.getTestType())
                                        .build();
                        item.setCreatedAt(DateUtils.getCurrentDateTime());
                        item.setCreatedBy(currentUserId);
                        lstStudentTestSet.add(item);
                });
                studentTestSetRepository.saveAll(lstStudentTestSet);
        }

        @Override
        public void publishOnlineTestSet(Long examClassId, Boolean isPublished) {
                studentTestSetRepository.updatePublishedState(examClassId, isPublished);
        }

        @Override
        public void sendEmailExamClassResult(Long examClassId, MailBaseReqDTO mailReqDTO) throws IOException {
                // TODO: design and insert template mail for this action
                Mail mail = new Mail();
                IExamClassDetailDTO examClassDetail = getExamClassDetail(examClassId);
                String exClassName = String.format("%s - %s", examClassDetail.getSubjectTitle(),
                                examClassDetail.getCode());
                CustomInputStreamResource resourceDTO = studentTestSetService
                                .exportStudentTestSetResult(examClassDetail.getCode());
                mail.setLstToAddress(mailReqDTO.getToAddresses());
                mail.setSubject(String.format(MailTemplateEnum.EMAIL_EXAM_CLASS_RESULT.getSubject(), exClassName));
                mail.setContent(String.format(
                                "<div><p>Danh sách và kết quả của lớp thi %s được đính kèm.</p><br><p>Trân trọng!</p></div>",
                                exClassName));
                mail.setIsHasAttachments(Boolean.TRUE);
                mail.setCreatedBy(AuthUtils.getCurrentUserId());
                mail.setCreatedAt(DateUtils.getCurrentDateTime());
                // attachments
                File attachmentFile = FileUtils.copyInputStreamToFile(
                                String.format("%s/%s", SystemConstants.FILE_UPLOAD_LOCATION, resourceDTO.getFileName()),
                                resourceDTO.getResource().getInputStream());
                if (Objects.nonNull(attachmentFile)) {
                        mail.setLstAttachedFiles(Collections.singletonList(attachmentFile));
                        mailService.sendMail(mail);
                        // delete after sending
                        org.apache.commons.io.FileUtils.delete(attachmentFile);
                }
        }
}