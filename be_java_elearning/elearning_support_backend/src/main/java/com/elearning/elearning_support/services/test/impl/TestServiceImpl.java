package com.elearning.elearning_support.services.test.impl;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import com.elearning.elearning_support.constants.message.errorKey.ErrorKey;
import com.elearning.elearning_support.constants.message.messageConst.MessageConst;
import com.elearning.elearning_support.constants.message.messageConst.MessageConst.CommonError;
import com.elearning.elearning_support.constants.message.messageConst.MessageConst.Resources;
import com.elearning.elearning_support.dtos.question.QuestionListDTO;
import com.elearning.elearning_support.dtos.question.QuestionListResDTO;
import com.elearning.elearning_support.dtos.test.ITestListDTO;
import com.elearning.elearning_support.dtos.test.TestDetailDTO;
import com.elearning.elearning_support.dtos.test.TestReqDTO;
import com.elearning.elearning_support.dtos.test.TestUpdateDTO;
import com.elearning.elearning_support.entities.chapter.Chapter;
import com.elearning.elearning_support.entities.subject.Subject;
import com.elearning.elearning_support.entities.test.Test;
import com.elearning.elearning_support.entities.test.TestQuestion;
import com.elearning.elearning_support.enums.commons.StatusEnum;
import com.elearning.elearning_support.enums.question.QuestionLevelEnum;
import com.elearning.elearning_support.enums.test.TestTypeEnum;
import com.elearning.elearning_support.exceptions.BadRequestException;
import com.elearning.elearning_support.exceptions.exceptionFactory.ExceptionFactory;
import com.elearning.elearning_support.repositories.postgres.examClass.ExamClassRepository;
import com.elearning.elearning_support.repositories.postgres.question.QuestionRepository;
import com.elearning.elearning_support.repositories.postgres.studentTestSet.StudentTestSetRepository;
import com.elearning.elearning_support.repositories.postgres.test.TestRepository;
import com.elearning.elearning_support.repositories.postgres.test.test_question.TestQuestionRepository;
import com.elearning.elearning_support.repositories.postgres.test.test_set.TestSetQuestionRepository;
import com.elearning.elearning_support.repositories.postgres.test.test_set.TestSetRepository;
import com.elearning.elearning_support.services.subject.SubjectService;
import com.elearning.elearning_support.services.test.TestService;
import com.elearning.elearning_support.utils.auth.AuthUtils;
import com.elearning.elearning_support.utils.object.ObjectMapperUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class TestServiceImpl implements TestService {

    private final QuestionRepository questionRepository;

    private final TestQuestionRepository testQuestionRepository;

    private final SubjectService subjectService;

    private final ExceptionFactory exceptionFactory;

    private final TestRepository testRepository;

    private final ExamClassRepository examClassRepository;

    private final StudentTestSetRepository studentTestSetRepository;

    private final TestSetRepository testSetRepository;

    private final TestSetQuestionRepository testSetQuestionRepository;

    @Transactional
    @Override
    public Long createRandomTest(TestReqDTO createDTO) {
        // Check môn học và chương
        Subject subject = subjectService.findById(createDTO.getSubjectId());

        // Check input chapterIds
        Set<Long> lstChapterIdInSubject = subject.getLstChapter().stream().map(Chapter::getId)
                .collect(Collectors.toSet());
        if (!lstChapterIdInSubject.containsAll(createDTO.getChapterIds())) {
            throw exceptionFactory.resourceNotFoundException(MessageConst.Chapter.NOT_FOUND,
                    MessageConst.RESOURCE_NOT_FOUND, Resources.CHAPTER,
                    ErrorKey.Chapter.ID);
        }

        // Lấy các câu hỏi trong môn học và các chương đã chọn
        if (!ObjectUtils.isEmpty(createDTO.getChapterIds())) {
            lstChapterIdInSubject.removeIf(item -> !createDTO.getChapterIds().contains(item));
        }
        Set<Long> lstQuestionIdInChapters = questionRepository.getListQuestionIdByChapterIn(lstChapterIdInSubject);
        // Tạo test
        Test newTest = new Test();
        BeanUtils.copyProperties(createDTO, newTest);
        newTest.setGenTestConfig(ObjectMapperUtils.toJsonString(createDTO.getGenerateConfig()));
        newTest.setIsEnabled(Boolean.TRUE);
        newTest.setCreatedAt(new Date());
        newTest.setTestType(createDTO.getTestType().getType());
        newTest.setCreatedBy(AuthUtils.getCurrentUserId());
        Long testId = testRepository.save(newTest).getId();
        // Tạo test_question
        List<TestQuestion> lstNewTestQuestion = lstQuestionIdInChapters.stream()
                .map(item -> new TestQuestion(testId, item)).collect(
                        Collectors.toList());
        testQuestionRepository.saveAll(lstNewTestQuestion);

        return testId;
    }

    @Transactional
    @Override
    public Long createTest(TestReqDTO createDTO) {
        Subject subject = subjectService.findById(createDTO.getSubjectId());

        Set<Long> lstChapterInSubject = subject.getLstChapter().stream().map(Chapter::getId)
                .collect(Collectors.toSet());
        // Get all questions in a subject
        Set<Long> lstQuestionInSubject = questionRepository.getListQuestionIdByChapterIn(lstChapterInSubject);
        if (!lstQuestionInSubject.containsAll(createDTO.getQuestionIds())) {
            throw exceptionFactory.resourceNotFoundException(MessageConst.Question.NOT_FOUND,
                    MessageConst.RESOURCE_NOT_FOUND,
                    Resources.QUESTION, ErrorKey.Question.ID);
        }

        // Remove not be chosen question in request dto
        if (!ObjectUtils.isEmpty(createDTO.getQuestionIds())) {
            lstQuestionInSubject.removeIf(item -> !createDTO.getQuestionIds().contains(item));
        }
        // Tạo test
        Test newTest = new Test();
        BeanUtils.copyProperties(createDTO, newTest);
        newTest.setTestType(createDTO.getTestType().getType());
        newTest.setGenTestConfig(ObjectMapperUtils.toJsonString(createDTO.getGenerateConfig()));
        newTest.setIsEnabled(Boolean.TRUE);
        newTest.setCreatedAt(new Date());
        newTest.setCreatedBy(AuthUtils.getCurrentUserId());
        Long testId = testRepository.save(newTest).getId();
        // Tạo test_question
        List<TestQuestion> lstNewTestQuestion = lstQuestionInSubject.stream()
                .map(item -> new TestQuestion(testId, item)).collect(
                        Collectors.toList());
        testQuestionRepository.saveAll(lstNewTestQuestion);

        return testId;
    }

    @Transactional
    @Override
    public void updateTest(Long testId, TestUpdateDTO updateDTO) {
        // Check test whether existed
        Test test = testRepository.findByIdAndIsEnabled(testId, Boolean.TRUE).orElseThrow(
                () -> exceptionFactory.resourceNotFoundException(MessageConst.Test.NOT_FOUND,
                        MessageConst.RESOURCE_NOT_FOUND, Resources.TEST,
                        ErrorKey.Test.ID, String.valueOf(testId)));

        // check update permission: Admin/Creator
        if (!AuthUtils.isAdmin() && !Objects.equals(AuthUtils.getCurrentUserId(), test.getCreatedBy())) {
            throw exceptionFactory.permissionDeniedException(CommonError.PERMISSIONS_DENIED, Resources.TEST,
                    MessageConst.PERMISSIONS_DENIED);
        }

        // Update test info
        BeanUtils.copyProperties(updateDTO, test);
        test.setModifiedBy(AuthUtils.getCurrentUserId());
        test.setModifiedAt(new Date());
        test.setGenTestConfig(ObjectMapperUtils.toJsonString(updateDTO.getGenerateConfig()));
        testRepository.save(test);

        // Update test_question
        Set<Long> lstCurrentTestQuestionId = testQuestionRepository.findAllByTestId(testId).stream()
                .map(TestQuestion::getQuestionId).collect(
                        Collectors.toSet());

        Set<Long> lstAddQuestionId = updateDTO.getQuestionIds().stream()
                .filter(item -> !lstCurrentTestQuestionId.contains(item)).collect(
                        Collectors.toSet());
        List<TestQuestion> lstNewTestQuestion = lstAddQuestionId.stream().map(item -> new TestQuestion(testId, item))
                .collect(
                        Collectors.toList());
        // Removed question
        Set<Long> lstRemovedQuestionId = lstCurrentTestQuestionId.stream()
                .filter(item -> !updateDTO.getQuestionIds().contains(item)).collect(
                        Collectors.toSet());

        // Perform transaction to DB
        testQuestionRepository.deleteAllByTestIdAndQuestionIdIn(test.getId(), lstRemovedQuestionId);
        testQuestionRepository.saveAll(lstNewTestQuestion);
    }

    @Override
    public Page<TestDetailDTO> getListTest(Long subjectId, String subjectCode, Date startTime, Date endTime,
            Long semesterId,
            String semesterCode, TestTypeEnum testType, Pageable pageable) {
        return testRepository.getListTest(subjectService.getListViewableSubjectIds(), subjectId, subjectCode, startTime,
                endTime, semesterId,
                semesterCode, testType.getType(), pageable).map(TestDetailDTO::new);
    }

    @Override
    public TestDetailDTO getTestDetail(Long testId) {
        ITestListDTO iTestDetails = testRepository.getTestDetails(testId);
        if (Objects.isNull(iTestDetails)) {
            throw exceptionFactory.resourceNotFoundException(MessageConst.Test.NOT_FOUND,
                    MessageConst.RESOURCE_NOT_FOUND, Resources.TEST,
                    ErrorKey.Test.ID, String.valueOf(testId));
        }
        return new TestDetailDTO(iTestDetails);
    }

    @Override
    public Test findTestById(Long testId) {
        return testRepository.findByIdAndIsEnabled(testId, Boolean.TRUE)
                .orElseThrow(() -> exceptionFactory.resourceNotFoundException(MessageConst.Test.NOT_FOUND,
                        Resources.TEST, MessageConst.RESOURCE_NOT_FOUND,
                        ErrorKey.Test.ID, String.valueOf(testId)));
    }

    @Override
    public Test findTestByIdAndType(Long testId, TestTypeEnum type) {
        return testRepository.findByIdAndTestTypeAndIsEnabled(testId, type.getType(), Boolean.TRUE)
                .orElseThrow(() -> exceptionFactory.resourceNotFoundException(MessageConst.Test.NOT_FOUND_BY_TYPE,
                        Resources.TEST,
                        MessageConst.RESOURCE_NOT_FOUND, ErrorKey.Test.ID, testId + "-" + type.name()));
    }

    @Override
    public Boolean existsById(Long testId) {
        if (!testRepository.existsByIdAndIsEnabled(testId, Boolean.TRUE)) {
            throw exceptionFactory.resourceNotFoundException(MessageConst.Test.NOT_FOUND, Resources.TEST,
                    MessageConst.RESOURCE_NOT_FOUND,
                    ErrorKey.Test.ID, String.valueOf(testId));
        }
        return Boolean.TRUE;
    }

    @Override
    public void switchTestStatus(Long testId, StatusEnum statusEnum) {
        Boolean newStatus = Objects.equals(statusEnum, StatusEnum.ENABLED) ? Boolean.TRUE : Boolean.FALSE;
        testRepository.switchTestStatus(testId, newStatus);
    }

    @Transactional
    @Override
    public void deleteTest(Long id) {
        if (!testRepository.existsByIdAndIsEnabled(id, Boolean.TRUE)) {
            throw exceptionFactory.resourceNotFoundException(MessageConst.Test.NOT_FOUND, Resources.TEST,
                    MessageConst.RESOURCE_NOT_FOUND,
                    ErrorKey.Test.ID, String.valueOf(id));
        }

        // check delete permission: ADMIN/createdUser
        if (!(AuthUtils.isAdmin() || testRepository.existsByIdAndCreatedBy(id, AuthUtils.getCurrentUserId()))) {
            throw new AccessDeniedException("Not allowed to delete this resource");
        }

        // check if test is assigned to any exam class
        if (examClassRepository.existsByTestId(id)) {
            throw new BadRequestException(MessageConst.Test.USED_IN_EXAM_CLASSES, Resources.TEST,
                    MessageConst.RESOURCE_EXISTED,
                    ErrorKey.Test.ID, String.valueOf(id));
        }
        // check if test is assigned or handled by any students
        if (studentTestSetRepository.existsByTestId(id)) {
            throw new BadRequestException(MessageConst.Test.ASSIGNED_OR_HANDLED_BY_STUDENTS, Resources.TEST,
                    MessageConst.RESOURCE_EXISTED,
                    ErrorKey.Test.ID, String.valueOf(id));
        }

        // delete
        testRepository.deleteById(id);
        // delete all test_set_question
        testQuestionRepository.deleteAllByTestId(id);
        // delete test_question
        Set<Long> lstTestSetInTest = testSetRepository.findByTestId(id);
        testSetRepository.deleteAllByTestId(id);
        // delete all test_set_question
        testSetQuestionRepository.deleteAllByTestSetIdIn(lstTestSetInTest);
    }

    @Override
    public QuestionListResDTO getListQuestionAllowedUsingInTest(Long testId, String search, QuestionLevelEnum level) {
        List<QuestionListDTO> questions = questionRepository
                .getListQuestionAllowedUsingInTest(testId, search, level.getLevel()).stream()
                .map(QuestionListDTO::new).sorted(Comparator.comparing(QuestionListDTO::getModifiedAt).reversed())
                .collect(Collectors.toList());
        return new QuestionListResDTO(questions.size(), questions.size(), questions);
    }

    @Override
    public Page<QuestionListDTO> getPageQuestionAllowedUsingInTest(Long testId, String search, QuestionLevelEnum level,
            Pageable pageable) {
        return questionRepository.getPageQuestionAllowedUsingInTest(testId, search, level.getLevel(), pageable)
                .map(QuestionListDTO::new);
    }
}
