package com.elearning.elearning_support.services.test.impl;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.elearning.elearning_support.entities.users.User;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.compress.utils.FileNameUtils;
import org.apache.commons.compress.utils.IOUtils;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.math3.util.Pair;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import com.elearning.elearning_support.constants.CharacterConstants;
import com.elearning.elearning_support.constants.FileConstants.Extension.Image;
import com.elearning.elearning_support.constants.SystemConstants;
import com.elearning.elearning_support.constants.message.messageConst.MessageConst.CommonError;
import com.elearning.elearning_support.dtos.CustomInputStreamResource;
import com.elearning.elearning_support.dtos.examClass.IExamClassParticipantDTO;
import com.elearning.elearning_support.dtos.fileAttach.FileAttachDTO;
import com.elearning.elearning_support.dtos.fileAttach.FileUploadResDTO;
import com.elearning.elearning_support.dtos.notification.NotificationFCMReqDTO;
import com.elearning.elearning_support.dtos.test.studentTestSet.AIExtractionReqDTO;
import com.elearning.elearning_support.dtos.test.studentTestSet.HandledImagesDeleteDTO;
import com.elearning.elearning_support.dtos.test.studentTestSet.StdTestSetDetailItemDTO;
import com.elearning.elearning_support.dtos.test.testSet.ITestSetPreviewDTO;
import com.elearning.elearning_support.dtos.test.testSet.ITestSetScoringDTO;
import com.elearning.elearning_support.dtos.test.testSet.ScoringImageReqDTO;
import com.elearning.elearning_support.dtos.test.testSet.ScoringPreviewItemDTO;
import com.elearning.elearning_support.dtos.test.testSet.ScoringPreviewResDTO;
import com.elearning.elearning_support.dtos.test.testSet.TestQuestionAnswerUpdateDTO;
import com.elearning.elearning_support.dtos.test.testSet.TestSetCreateDTO;
import com.elearning.elearning_support.dtos.test.testSet.TestSetPreviewDTO;
import com.elearning.elearning_support.entities.examClass.ExamClass;
import com.elearning.elearning_support.entities.fileAttach.FileAttach;
import com.elearning.elearning_support.entities.studentTest.StudentTestSet;
import com.elearning.elearning_support.enums.examClass.UserExamClassRoleEnum;
import com.elearning.elearning_support.enums.fileAttach.FileExtensionEnum;
import com.elearning.elearning_support.enums.fileAttach.FileStoredTypeEnum;
import com.elearning.elearning_support.enums.fileAttach.FileTypeEnum;
import com.elearning.elearning_support.enums.notification.NotificationContentEnum;
import com.elearning.elearning_support.enums.notification.NotificationObjectTypeEnum;
import com.elearning.elearning_support.enums.test.StudentTestStatusEnum;
import com.elearning.elearning_support.enums.test.TestTypeEnum;
import com.elearning.elearning_support.exceptions.BadRequestException;
import com.elearning.elearning_support.repositories.postgres.examClass.ExamClassRepository;
import com.elearning.elearning_support.repositories.postgres.examClass.UserExamClassRepository;
import com.elearning.elearning_support.repositories.postgres.studentTestSet.StudentTestSetRepository;
import com.elearning.elearning_support.services.fileAttach.FileAttachService;
import com.elearning.elearning_support.services.notification.NotificationService;
import com.elearning.elearning_support.utils.StringUtils;
import com.elearning.elearning_support.utils.file.FileUtils;
import com.elearning.elearning_support.utils.object.ObjectUtils;
import com.elearning.elearning_support.utils.springCustom.SpringContextUtils;
import com.elearning.elearning_support.utils.tests.TestUtils;
import com.elearning.elearning_support.constants.message.errorKey.ErrorKey;
import com.elearning.elearning_support.constants.message.messageConst.MessageConst;
import com.elearning.elearning_support.constants.message.messageConst.MessageConst.Resources;
import com.elearning.elearning_support.dtos.question.QuestionAnswerDTO;
import com.elearning.elearning_support.dtos.studentTestSet.HandledAnswerDTO;
import com.elearning.elearning_support.dtos.studentTestSet.StudentHandledTestDTO;
import com.elearning.elearning_support.dtos.test.GenTestConfigDTO;
import com.elearning.elearning_support.dtos.test.testQuestion.TestQuestionAnswerResDTO;
import com.elearning.elearning_support.dtos.test.testSet.ITestQuestionCorrectAnsDTO;
import com.elearning.elearning_support.dtos.test.testSet.ITestSetResDTO;
import com.elearning.elearning_support.dtos.test.testSet.TestQuestionAnswer;
import com.elearning.elearning_support.dtos.test.testSet.TestSetDetailDTO;
import com.elearning.elearning_support.dtos.test.testSet.TestSetGenerateReqDTO;
import com.elearning.elearning_support.dtos.test.testSet.TestSetQuestionMapDTO;
import com.elearning.elearning_support.dtos.test.testSet.TestSetSearchReqDTO;
import com.elearning.elearning_support.dtos.test.testSet.TestSetUpdateDTO;
import com.elearning.elearning_support.entities.test.Test;
import com.elearning.elearning_support.entities.test.TestSet;
import com.elearning.elearning_support.entities.test.TestSetQuestion;
import com.elearning.elearning_support.enums.question.QuestionLevelEnum;
import com.elearning.elearning_support.exceptions.exceptionFactory.ExceptionFactory;
import com.elearning.elearning_support.repositories.postgres.question.QuestionRepository;
import com.elearning.elearning_support.repositories.postgres.test.TestRepository;
import com.elearning.elearning_support.repositories.postgres.test.test_set.TestSetQuestionRepository;
import com.elearning.elearning_support.repositories.postgres.test.test_set.TestSetRepository;
import com.elearning.elearning_support.services.test.TestSetService;
import com.elearning.elearning_support.utils.auth.AuthUtils;
import com.elearning.elearning_support.utils.object.ObjectMapperUtils;
import com.elearning.elearning_support.utils.word.WordUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class TestSetServiceImpl implements TestSetService {

    public static final String ANSWERED_SHEETS = "AnsweredSheets";

    public static final String HANDLED_SHEETS = "HandledSheets";

    public static final String SCORED_SHEETS = "ScoredSheets";

    public static final String MAY_BE_WRONG = "MayBeWrong/may_be_wrong.txt";

    public static final String FILE_TEMP_SCORED_RESULTS_DATA = "temp_results_%s.json";

    public static final String FILE_TEMP_SCORED_DATA_RESPONSE = "temp_scored_data.json";

    private static final int MAX_NUM_ANSWERS_PER_QUESTION = 6;

    private final TestSetRepository testSetRepository;

    private final TestRepository testRepository;

    private final ExceptionFactory exceptionFactory;

    private final QuestionRepository questionRepository;

    private final TestSetQuestionRepository testSetQuestionRepository;

    private final StudentTestSetRepository studentTestSetRepository;

    private final ExamClassRepository examClassRepository;

    private final FileAttachService fileAttachService;

    private final RestTemplate restTemplate;

    private final NotificationService notificationService;

    private final UserExamClassRepository userExamClassRepository;

    @Value("${python-ai-server.domain}")
    private String pythonAIDomain;

    @Value("${python-ai-server.apis.ai-scoring}")
    private String scoringAIApiPath;

    @Value("${python-ai-server.api-key}")
    private String pythonModuleApiKey;

    @Value("${app.file-storage.default-stored-type}")
    private String defaultStoredType;

    @Transactional
    @Override
    public List<TestSetPreviewDTO> generateTestSet(TestSetGenerateReqDTO generateReqDTO) {
        Long currentUserId = AuthUtils.getCurrentUserId();
        Test test = testRepository.findByIdAndIsEnabled(generateReqDTO.getTestId(), Boolean.TRUE).orElseThrow(
            () -> exceptionFactory.resourceNotFoundException(MessageConst.Test.NOT_FOUND,
                MessageConst.RESOURCE_NOT_FOUND, Resources.TEST,
                ErrorKey.Test.ID, String.valueOf(generateReqDTO.getTestId())));

        // Lấy cấu hình tạo đề thi
        GenTestConfigDTO genTestConfig = ObjectMapperUtils.objectMapper(test.getGenTestConfig(),
            GenTestConfigDTO.class);
        if (Objects.isNull(genTestConfig)) {
            return null;
        }

        // Lấy các câu hỏi trong bộ câu hỏi của đề thi
        List<QuestionAnswerDTO> lstQuestionInTest = questionRepository
            .getListQuestionIdInTest(generateReqDTO.getTestId()).stream()
            .map(QuestionAnswerDTO::new).collect(Collectors.toList());
        // Lọc theo mức độ: dễ/trung bình/ khó
        List<QuestionAnswerDTO> lstEasyQuestion = lstQuestionInTest.stream()
            .filter(question -> Objects.equals(question.getLevel(),
                QuestionLevelEnum.EASY.getLevel()))
            .collect(Collectors.toList());
        List<QuestionAnswerDTO> lstMediumQuestion = lstQuestionInTest.stream()
            .filter(question -> Objects.equals(question.getLevel(),
                QuestionLevelEnum.MEDIUM.getLevel()))
            .collect(Collectors.toList());
        List<QuestionAnswerDTO> lstHardQuestion = lstQuestionInTest.stream()
            .filter(question -> Objects.equals(question.getLevel(),
                QuestionLevelEnum.HARD.getLevel()))
            .collect(Collectors.toList());

        // Tạo đề thi
        List<TestSet> lstTestSet = new ArrayList<>();
        List<String> lstTestCode = new ArrayList<>(
            randomTestSetCode(generateReqDTO.getTestId(), generateReqDTO.getNumOfTestSet()));
        for (int i = 0; i < generateReqDTO.getNumOfTestSet(); i++) {
            TestSet newTestSet = TestSet.builder()
                .testId(test.getId())
                .testNo(String.valueOf(i + 1))
                .code(lstTestCode.get(i))
                .totalPoint(test.getTotalPoint())
                .questionMark(calculateQuestionMark(test.getTotalPoint(), test.getQuestionQuantity()))
                .isEnabled(Boolean.TRUE).build();
            newTestSet.setCreatedBy(currentUserId);
            newTestSet.setCreatedAt(new Date());
            lstTestSet.add(newTestSet);
        }
        // Tạo đề
        lstTestSet = testSetRepository.saveAll(lstTestSet);
        List<TestSetQuestion> lstTestSetQuestion = new ArrayList<>();
        List<TestSetQuestionMapDTO> lstMapTestQuestionDTO = new ArrayList<>();

        // Map TestSet và Question
        for (TestSet testSet : lstTestSet) {
            TestSetQuestionMapDTO mapDTO = new TestSetQuestionMapDTO();
            mapDTO.setTestSetId(testSet.getId());
            mapDTO.setTotalPoint(test.getTotalPoint());
            int missingNumOfQuestion = 0;

            // Trộn các câu hỏi ở mức độ dễ và đưa vào đề
            int numberEasyQuestion = Math.min(genTestConfig.getNumEasyQuestion(), lstEasyQuestion.size());
            if (numberEasyQuestion > 0) {
                Collections.shuffle(lstEasyQuestion);
                mapDTO.getLstQuestionAnswer().addAll(lstEasyQuestion.subList(0, numberEasyQuestion));
            }
            if (numberEasyQuestion < genTestConfig.getNumEasyQuestion()) {
                missingNumOfQuestion = genTestConfig.getNumEasyQuestion() - numberEasyQuestion;
            }

            // Trộn các câu hỏi mức trung bình và đưa vào đề
            int numberMediumQuestion = Math.min(genTestConfig.getNumMediumQuestion() + missingNumOfQuestion,
                lstMediumQuestion.size());
            if (numberMediumQuestion > 0) {
                Collections.shuffle(lstMediumQuestion);
                mapDTO.getLstQuestionAnswer().addAll(lstMediumQuestion.subList(0, numberMediumQuestion));
            }
            if (numberMediumQuestion < genTestConfig.getNumMediumQuestion() + missingNumOfQuestion) {
                missingNumOfQuestion = genTestConfig.getNumMediumQuestion() + missingNumOfQuestion
                    - numberMediumQuestion;
            } else {
                missingNumOfQuestion = 0;
            }

            // Trộn các câu hỏi mức khó và đưa vào đề
            int numberHardQuestion = Math.min(genTestConfig.getNumHardQuestion() + missingNumOfQuestion,
                lstHardQuestion.size());
            if (numberHardQuestion > 0) {
                Collections.shuffle(lstHardQuestion);
                mapDTO.getLstQuestionAnswer().addAll(lstHardQuestion.subList(0, numberHardQuestion));
            }
            if (numberMediumQuestion < genTestConfig.getNumHardQuestion() + missingNumOfQuestion) {
                missingNumOfQuestion = genTestConfig.getNumHardQuestion() + missingNumOfQuestion - numberHardQuestion;
            } else {
                missingNumOfQuestion = 0;
            }

            if (missingNumOfQuestion > 0) {
                log.info("TEST SET {} IS MISSING {}", testSet.getId(), missingNumOfQuestion);
            }
            lstMapTestQuestionDTO.add(mapDTO);
        }

        // convert sang entity để save vào db
        for (TestSetQuestionMapDTO mapDTO : lstMapTestQuestionDTO) {
            for (int i = 0; i < mapDTO.getLstQuestionAnswer().size(); i++) {
                QuestionAnswerDTO questionAnswer = mapDTO.getLstQuestionAnswer().get(i);
                lstTestSetQuestion.add(TestSetQuestion.builder()
                    .testSetId(mapDTO.getTestSetId())
                    .questionId(questionAnswer.getId())
                    .questionNo(i + 1)
                    .questionMark(
                        calculateQuestionMark(mapDTO.getTotalPoint(), mapDTO.getLstQuestionAnswer().size()))
                    .lstAnswer(randomTestQuestionAnswer(questionAnswer.getLstAnswerId()))
                    .build());
            }
        }
        testSetQuestionRepository.saveAll(lstTestSetQuestion);
        return lstTestSet.stream()
            .map(testSet -> new TestSetPreviewDTO(testSet.getId(), testSet.getCode(), testSet.getTestNo(),
                testSet.getTestId()))
            .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public TestSetPreviewDTO createTestSet(TestSetCreateDTO createDTO) {
        Long currentUserId = AuthUtils.getCurrentUserId();
        // check test_id
        Test test = testRepository.findByIdAndIsEnabled(createDTO.getTestId(), Boolean.TRUE).orElseThrow(
            () -> exceptionFactory.resourceNotFoundException(MessageConst.Test.NOT_FOUND,
                MessageConst.RESOURCE_NOT_FOUND, Resources.TEST,
                ErrorKey.Test.ID, String.valueOf(createDTO.getTestId())));

        // tạo testSetCode: thủ công/tự động
        String testSetCode;
        if (!org.apache.commons.lang3.ObjectUtils.isEmpty(createDTO.getTestSetCode())) {
            if (testSetRepository.existsByTestIdAndCode(createDTO.getTestId(), createDTO.getTestSetCode())) {
                throw exceptionFactory.resourceExistedException(MessageConst.TestSet.EXISTED_BY_CODE,
                    Resources.TEST_SET,
                    MessageConst.RESOURCE_EXISTED, ErrorKey.TestSet.CODE, createDTO.getTestSetCode());
            } else {
                testSetCode = createDTO.getTestSetCode();
            }
        } else {
            Set<String> randomTestSetCodes = randomTestSetCode(test.getId(), 1);
            testSetCode = !org.apache.commons.lang3.ObjectUtils.isEmpty(randomTestSetCodes)
                ? randomTestSetCodes.toArray()[0].toString()
                : "";
        }
        // tạo test_set
        TestSet newTestSet = TestSet.builder()
            .testId(test.getId())
            .testNo(String.valueOf(
                ObjectUtils.getOrDefault(testSetRepository.getMaxCurrentTestNoByTestId(test.getId()), 0) + 1))
            .code(testSetCode)
            .totalPoint(test.getTotalPoint())
            .questionMark(calculateQuestionMark(test.getTotalPoint(), test.getQuestionQuantity()))
            .isEnabled(Boolean.TRUE)
            .build();
        newTestSet.setCreatedBy(currentUserId);
        newTestSet.setCreatedAt(new Date());
        newTestSet = testSetRepository.save(newTestSet);
        // tạo test_set_question
        List<TestSetQuestion> lstTestSetQuestion = new ArrayList<>();
        for (TestQuestionAnswerUpdateDTO question : createDTO.getQuestions()) {
            lstTestSetQuestion.add(TestSetQuestion.builder()
                .testSetId(newTestSet.getId())
                .questionId(question.getQuestionId())
                .questionNo(question.getQuestionNo())
                .questionMark(calculateQuestionMark(test.getTotalPoint(), createDTO.getQuestions().size()))
                .lstAnswer(question.getAnswers())
                .build());
        }
        testSetQuestionRepository.saveAll(lstTestSetQuestion);
        return new TestSetPreviewDTO(newTestSet.getId(), newTestSet.getCode(), newTestSet.getTestNo(), test.getId());
    }

    @Override
    public TestSetDetailDTO getTestSetDetail(TestSetSearchReqDTO searchReqDTO) {
        // Lấy thông tin đề thi
        ITestSetResDTO testSetDetail = testSetRepository.getTestSetDetail(searchReqDTO.getTestId(),
            searchReqDTO.getCode());
        if (Objects.isNull(testSetDetail.getTestSetId())) {
            throw exceptionFactory.resourceNotFoundException(MessageConst.TestSet.NOT_FOUND,
                MessageConst.RESOURCE_NOT_FOUND, Resources.TEST_SET,
                ErrorKey.TestSet.TEST_ID_AND_CODE, String.valueOf(searchReqDTO.getTestId()),
                searchReqDTO.getCode());
        }
        // Lấy thông tin câu hỏi và đáp án trong đề;
        List<TestQuestionAnswerResDTO> lstQuestions = testSetRepository
            .getListTestSetQuestion(testSetDetail.getTestSetId()).stream()
            .map(item -> new TestQuestionAnswerResDTO(item, Boolean.FALSE)).collect(Collectors.toList());
        return new TestSetDetailDTO(testSetDetail, lstQuestions);
    }

    @Override
    public InputStreamResource exportTestSet(TestSetSearchReqDTO searchReqDTO) {
        TestSetDetailDTO testSetDetail = getTestSetDetail(searchReqDTO);
        try {
            ByteArrayInputStream inputStream = WordUtils.exportTestToWord(testSetDetail);
            if (Objects.nonNull(inputStream)) {
                return new InputStreamResource(inputStream);
            }
        } catch (IOException e) {
            log.error(MessageConst.EXCEPTION_LOG_FORMAT, e.getMessage(), e.getCause(), e);
        }
        return null;
    }

    @Transactional
    @Override
    public void updateTestSet(TestSetUpdateDTO updateDTO) {
        // if the test_set is used => exception
        if (studentTestSetRepository.existsByTestSetIdAndStatusNot(updateDTO.getTestSetId(),
            StudentTestStatusEnum.OPEN.getType())) {
            throw new BadRequestException(MessageConst.TestSet.USED, MessageConst.RESOURCE_EXISTED, Resources.TEST_SET,
                String.valueOf(updateDTO.getTestSetId()));
        }

        TestSet testSet = testSetRepository.findByIdAndIsEnabled(updateDTO.getTestSetId(), Boolean.TRUE)
            .orElseThrow(() -> exceptionFactory.resourceNotFoundException(MessageConst.TestSet.NOT_FOUND,
                MessageConst.RESOURCE_NOT_FOUND, Resources.TEST_SET,
                ErrorKey.TestSet.ID, String.valueOf(updateDTO.getTestSetId())));
        // Xoá các bản ghi testSetQuestion hiện tại và lưu mới
        testSetQuestionRepository.deleteAllByTestSetId(updateDTO.getTestSetId());

        // Lưu các bản ghi mới
        Integer numOfQuestion = updateDTO.getQuestions().size();
        List<TestSetQuestion> lstNewTestSetQuestion = updateDTO.getQuestions().stream()
            .map(question -> new TestSetQuestion(updateDTO.getTestSetId(),
                calculateQuestionMark(testSet.getTotalPoint(), numOfQuestion),
                question))
            .collect(Collectors.toList());
        testSetQuestionRepository.saveAll(lstNewTestSetQuestion);
    }

    @Override
    public List<ITestSetPreviewDTO> getListTestSetPreview(Long testId) {
        return testSetRepository.getListTestSetPreviewByTestId(testId);
    }

    @Override
    public void deleteTestSetById(Long testSetId) {
        // check exist
        if (!testSetRepository.existsByIdAndIsEnabled(testSetId, Boolean.TRUE)) {
            throw exceptionFactory.resourceNotFoundException(MessageConst.TestSet.NOT_FOUND,
                MessageConst.RESOURCE_NOT_FOUND, Resources.TEST_SET,
                ErrorKey.TestSet.ID, String.valueOf(testSetId));
        }
        // if is used => exception
        if (studentTestSetRepository.existsByTestSetId(testSetId)) {
            throw new BadRequestException(MessageConst.TestSet.USED, MessageConst.RESOURCE_EXISTED, Resources.TEST_SET,
                String.valueOf(testSetId));
        }

        // delete test_set
        testSetRepository.deleteById(testSetId);

    }

    /**
     * Sinh tự động mã đề trong kỳ thi
     */
    private Set<String> randomTestSetCode(Long testId, Integer length) {
        Set<String> lstRandomCode = new HashSet<>();
        do {
            String newCode = RandomStringUtils.randomNumeric(3);
            if (!testSetRepository.existsByTestIdAndCode(testId, newCode)) {
                lstRandomCode.add(newCode);
            }
        } while (lstRandomCode.size() < length);
        return lstRandomCode;
    }

    /**
     * Random thứ tự đáp án 1 câu hỏi trong đề
     */
    private List<TestQuestionAnswer> randomTestQuestionAnswer(List<Long> lstAnswerId) {
        if (org.apache.commons.lang3.ObjectUtils.isEmpty(lstAnswerId)) {
            return new ArrayList<>();
        }
        List<TestQuestionAnswer> lstTestQuestionAnswer = new ArrayList<>();
        List<Integer> answerOrder = IntStream.range(1, Math.min(lstAnswerId.size(), MAX_NUM_ANSWERS_PER_QUESTION) + 1)
            .boxed()
            .collect(Collectors.toList());
        Collections.shuffle(answerOrder);
        for (int i = 0; i < lstAnswerId.size(); i++) {
            lstTestQuestionAnswer.add(new TestQuestionAnswer(lstAnswerId.get(i), answerOrder.get(i)));
        }
        return lstTestQuestionAnswer;
    }

    /**
     * Calculate questionMark
     */
    private Double calculateQuestionMark(Integer totalPoint, Integer questionQuantity) {
        try {
            return (double) totalPoint / questionQuantity;
        } catch (Exception e) {
            log.error("==== ERROR {1} ====", e.getCause());
            return null;
        }
    }

    /**
     * ======================== TEST SET SCORING SERVICES ====================
     */

    @Transactional
    @Override
    public ScoringPreviewResDTO scoreExamClassTestSet(String examClassCode, ScoringImageReqDTO scoringImagesReqDTO) {
        // check scoring permission (only for admin and lecturer)
        ExamClass examClass = examClassRepository.findByCodeAndIsEnabled(examClassCode, Boolean.TRUE)
            .orElseThrow(() -> exceptionFactory.resourceNotFoundException(MessageConst.ExamClass.NOT_FOUND,
                MessageConst.RESOURCE_NOT_FOUND,
                Resources.EXAM_CLASS, ErrorKey.ExamClass.CODE, examClassCode));
        if (!AuthUtils.isAdmin() &&
            !userExamClassRepository.existsByExamClassIdAndUserIdAndRoleType(examClass.getId(),
                AuthUtils.getCurrentUserId(),
                UserExamClassRoleEnum.LECTURER.getType())) {
            throw exceptionFactory.permissionDeniedException(CommonError.PERMISSIONS_DENIED, Resources.STUDENT_TEST_SET,
                MessageConst.PERMISSIONS_DENIED);
        }
        callAIModelProcessingUsingAPI(examClassCode, scoringImagesReqDTO.getMode(),
            scoringImagesReqDTO.getNumberAnswers(), scoringImagesReqDTO.getSelectedImages()); // call AI model using
        // API (must run
        // python server)
        // callAIModelProcessingUsingCMD(examClassCode); // call AI model using command
        // in OS
        List<String> warningMessages = new ArrayList<>();
        List<ScoringPreviewItemDTO> scoredPreviewItems = scoreStudentTestSet(examClassCode,
            loadListStudentScoredSheets(examClassCode, warningMessages, scoringImagesReqDTO.getSelectedImages()));
        ScoringPreviewResDTO response = new ScoringPreviewResDTO();
        response.setPreviews(scoredPreviewItems);
        response.setWarningMessages(warningMessages);
        if (scoringImagesReqDTO.getSelectedImages().contains("ALL")) {
            saveExamClassTempScoredData(examClassCode, response);
        } else {
            mergeExamClassTempScoredData(examClassCode, response);
        }
        return response;
    }

    @Transactional
    @Override
    public List<ScoringPreviewItemDTO> scoreStudentTestSet(String examClassCode,
        List<StudentHandledTestDTO> handledTestSets) {
        long startTimeMillis = System.currentTimeMillis();
        log.info("============== STARTED SCORING HANDLED ANSWER SHEET AT {} =================", startTimeMillis);
        Long currentUserId = AuthUtils.getCurrentUserId();
        // find -> student, test-set
        // Map exam_class + testCode -> testSetId
        Map<Pair<String, String>, ITestSetScoringDTO> mapGeneralHandledData = new HashMap<>();
        // Sử dụng logic chấm thi theo lớp
        // Set<String> examClassCodes =
        // handledTestSets.stream().map(StudentHandledTestDTO::getExamClassCode).collect(Collectors.toSet());
        Set<String> testCodes = handledTestSets.stream().map(StudentHandledTestDTO::getTestSetCode)
            .collect(Collectors.toSet());
        List<ITestSetScoringDTO> generalScoringData = testSetRepository
            .getTestSetGeneralScoringData(Collections.singleton(examClassCode), testCodes);
        generalScoringData
            .forEach(item -> mapGeneralHandledData.put(Pair.create(examClassCode, item.getTestSetCode()), item));

        // map studentCode -> studentId
        // get list student in exam class
        Long examClassId = org.springframework.util.ObjectUtils.isEmpty(generalScoringData) ? -1L : generalScoringData.get(0).getExamClassId();
        Map<String, Long> mapUserCodeId = examClassRepository
            .getListExamClassParticipant(examClassId,
                Collections.singleton(UserExamClassRoleEnum.STUDENT.getType()))
            .stream().collect(Collectors.toMap(IExamClassParticipantDTO::getCode, IExamClassParticipantDTO::getId));

        // map testSetId -> query test_set_question
        Map<Long, Set<ITestQuestionCorrectAnsDTO>> mapQueriedTestSetQuestions = new HashMap<>();
        List<ScoringPreviewItemDTO> lstScoringPreview = new ArrayList<>();
        for (StudentHandledTestDTO handledItem : handledTestSets) {
            // init map
            ITestSetScoringDTO handledData = mapGeneralHandledData
                .get(Pair.create(examClassCode, handledItem.getTestSetCode()));
            // if test_set_code not used in this test or ex_class
            // if (Objects.isNull(handledData)) {
            // throw
            // exceptionFactory.resourceNotFoundException(MessageConst.TestSet.NOT_FOUND,
            // MessageConst.RESOURCE_NOT_FOUND,
            // Resources.TEST_SET, ErrorKey.TestSet.CODE, handledItem.getTestSetCode());
            // }
            // if the student_code is not in the exam_class
            Long studentId = mapUserCodeId.get(handledItem.getStudentCode());
            // if (Objects.isNull(studentId)) {
            // throw
            // exceptionFactory.resourceNotFoundException(UserExamClass.STUDENT_NOT_FOUND,
            // MessageConst.RESOURCE_NOT_FOUND,
            // Resources.USER_EXAM_CLASS, User.CODE, handledItem.getStudentCode());
            // }
            Long testSetId = Objects.nonNull(handledData) ? handledData.getTestSetId() : -1;

            // create a studentTestSet row
            StudentTestSet studentTestSet = new StudentTestSet(studentId, testSetId);
            studentTestSet.setCreatedAt(new Date());
            studentTestSet.setCreatedBy(currentUserId);
            studentTestSet.setIsEnabled(Boolean.TRUE);
            studentTestSet.setIsSubmitted(Boolean.TRUE);
            studentTestSet.setTestType(TestTypeEnum.OFFLINE.getType());
            studentTestSet.setStatus(StudentTestStatusEnum.SUBMITTED.getType());
            studentTestSet.setIsPublished(Boolean.TRUE);
            studentTestSet.setExamClassId(examClassId);
            studentTestSet.setSynchronizedStatus(0); // not synchronized to table "student_test_set_details"

            // get handled details
            List<StdTestSetDetailItemDTO> lstDetails = new ArrayList<>();
            Map<Integer, ITestQuestionCorrectAnsDTO> mapQuestionCorrectAns = new HashMap<>();
            Set<ITestQuestionCorrectAnsDTO> correctAnswers = mapQueriedTestSetQuestions.get(testSetId);
            // Check if test set has queried
            if (Objects.isNull(correctAnswers)) {
                correctAnswers = testSetRepository.getListTestQuestionCorrectAns(testSetId);
                mapQueriedTestSetQuestions.put(testSetId, correctAnswers);
            }
            correctAnswers.forEach(item -> mapQuestionCorrectAns.put(item.getQuestionNo(), item));
            // scoring
            int numCorrectAns = 0;
            int numNotMarkedQuestions = 0;
            double totalScore = 0.0;
            for (HandledAnswerDTO handledAnswer : handledItem.getAnswers()) {
                // Get selected answers and check if not marked
                Set<Integer> selectedAnsNo = TestUtils.getSelectedAnswerNo(handledAnswer.getSelectedAnswers());
                if (org.apache.commons.lang3.ObjectUtils.isEmpty(selectedAnsNo)) {
                    numNotMarkedQuestions++;
                }
                // Get correct answer of question in this test set
                ITestQuestionCorrectAnsDTO correctAnswerDTO = mapQuestionCorrectAns.get(handledAnswer.getQuestionNo());
                if (Objects.isNull(correctAnswerDTO)) {
                    continue;
                }
                Set<Integer> correctAnswerNo = StringUtils
                    .convertStrIntegerToSet(correctAnswerDTO.getCorrectAnswerNo());
                handledAnswer.setCorrectAnswers(TestUtils.getSelectedAnswerChar(correctAnswerNo));
                // Create new studentTestSetDetails
                StdTestSetDetailItemDTO studentAnswerDetail = new StdTestSetDetailItemDTO();
                studentAnswerDetail.setCorrectAnswers(correctAnswerNo.toArray(Integer[]::new));
                studentAnswerDetail.setTestSetQuestionId(correctAnswerDTO.getId());
                studentAnswerDetail.setSelectedAnswers(selectedAnsNo.toArray(Integer[]::new));
                studentAnswerDetail.setIsEnabled(Boolean.TRUE);
                studentAnswerDetail.setCreatedAt(new Date());
                studentAnswerDetail.setCreatedBy(currentUserId);
                if (!org.springframework.util.ObjectUtils.isEmpty(correctAnswerNo) &&
                    !org.springframework.util.ObjectUtils.isEmpty(selectedAnsNo) &&
                    correctAnswerNo.size() == selectedAnsNo.size()
                    && CollectionUtils.containsAll(correctAnswerNo, selectedAnsNo)) {
                    studentAnswerDetail.setIsCorrected(Boolean.TRUE);
                    totalScore += correctAnswerDTO.getQuestionMark();
                    numCorrectAns++;
                } else {
                    studentAnswerDetail.setIsCorrected(Boolean.FALSE);
                }
                lstDetails.add(studentAnswerDetail);
            }

            // upload scored image
            String handledImgPath = handledItem.getHandledScoredImg();
            FileUploadResDTO handledUploadFile = new FileUploadResDTO();
            if (org.apache.commons.lang3.ObjectUtils.isNotEmpty(handledImgPath)) {
                File handledFile = new File(handledItem.getHandledScoredImg());
                String previewPath = SystemConstants.SHARED_DIR_SERVER_PATH +
                    String.format("/data/%s/%s/%s/%s", ANSWERED_SHEETS, examClassCode, HANDLED_SHEETS,
                        handledItem.getHandledScoredFileName());
                handledUploadFile.setFilePath(previewPath);
                handledUploadFile.setStoredTypeEnum(FileStoredTypeEnum.INTERNAL_SERVER);
                handledUploadFile.setFileAttachDB(
                    FileAttach.builder()
                        .extension(FileExtensionEnum.JPG.getFileExt())
                        .type(FileTypeEnum.IMAGE.getType())
                        .name(handledItem.getHandledScoredFileName())
                        .filePath(previewPath)
                        .storedType(FileStoredTypeEnum.INTERNAL_SERVER.getType())
                        .size(handledFile.getTotalSpace())
                        .createdAt(new Date())
                        .createdBy(AuthUtils.getCurrentUserId())
                        .build());
            }
            // create student-test-set
            studentTestSet.setHandedTestFile(handledUploadFile.getFileAttachDB());
            studentTestSet.setHandledFileAbsolutePath(handledItem.getHandledScoredImg());
            studentTestSet.setMarked(handledItem.getAnswers().size() - numNotMarkedQuestions);
            studentTestSet
                .setMarkerRate(((double) (studentTestSet.getMarked()) / (handledItem.getAnswers().size())) * 100.0);
            studentTestSet.setStdTestSetDetail(lstDetails);

            // create scoring preview for each handled answer-sheet
            ScoringPreviewItemDTO scoringPreviewItem = new ScoringPreviewItemDTO(handledItem);
            scoringPreviewItem.setNumTestSetQuestions(correctAnswers.size()); // Number questions of a test set
            scoringPreviewItem.setNumMarkedAnswers(handledItem.getAnswers().size() - numNotMarkedQuestions);
            scoringPreviewItem.setNumCorrectAnswers(numCorrectAns);
            scoringPreviewItem.setNumWrongAnswers(mapQueriedTestSetQuestions.get(testSetId).size() - numCorrectAns);
            scoringPreviewItem.setTotalScore(totalScore);
            scoringPreviewItem.setHandledScoredImg(handledUploadFile.getFilePath());
            scoringPreviewItem.setStoredType(FileStoredTypeEnum.INTERNAL_SERVER);
            scoringPreviewItem.setOriginalImgFileName(handledItem.getOriginalImgFileName());
            scoringPreviewItem.setOriginalImg(
                String.format("%s/data/%s/%s/%s", SystemConstants.SHARED_DIR_SERVER_PATH, ANSWERED_SHEETS,
                    examClassCode, handledItem.getOriginalImgFileName()));
            // map preview item với db item -> thuận tiện cho việc merge sau này
            if (Objects.nonNull(studentTestSet.getStudentId()) && Objects.nonNull(studentTestSet.getTestSetId())
                && !Objects.equals(studentTestSet.getTestSetId(), -1L)) {
                scoringPreviewItem.setStudentTestSet(studentTestSet);
            }
            lstScoringPreview.add(scoringPreviewItem);
        }

        log.info("============== ENDED SCORING HANDLED ANSWER SHEET AFTER {} ms =================",
            System.currentTimeMillis() - startTimeMillis);
        return lstScoringPreview;
    }

    @Override
    public void uploadStudentHandledAnswerSheet(String examClassCode, MultipartFile[] handledFiles) throws IOException {
        // Check existed exam_class
        ExamClass examClass = examClassRepository.findByCodeAndIsEnabled(examClassCode, Boolean.TRUE)
            .orElseThrow(() -> exceptionFactory.resourceNotFoundException(MessageConst.ExamClass.NOT_FOUND,
                Resources.EXAM_CLASS,
                MessageConst.RESOURCE_NOT_FOUND, ErrorKey.ExamClass.CODE, String.valueOf(examClassCode)));

        // Check system os
        File sharedAppDataDir;
        String sharedAppDataPath = FileUtils.getSharedAppDirectoryDataPath();
        sharedAppDataDir = new File(sharedAppDataPath);
        if (!sharedAppDataDir.exists()) {
            log.info("Make sharedAppDataDir {}", sharedAppDataDir.mkdirs() ? "successfully" : "fail");
        }

        // upload handled answer sheet's images
        String examClassStoredPath = String.format("%s/%s/%s/", sharedAppDataPath, ANSWERED_SHEETS,
            examClass.getCode());
        File examClassStoredDir = new File(examClassStoredPath);
        if (examClassStoredDir.exists()) {
            // Delete old data before upload
            for (File item : Objects.requireNonNull(examClassStoredDir.listFiles())) {
                // Clear result folder only
                if (item.isDirectory()) {
                    org.apache.commons.io.FileUtils.cleanDirectory(item);
                }
            }
        } else {
            log.info("Make examClassStoredDir {}", examClassStoredDir.mkdirs() ? "successfully" : "fail");
        }
        // Write file to storage directory
        try {
            for (MultipartFile handledFile : handledFiles) {
                FileUtils.validateUploadFile(handledFile, Arrays.asList(Image.JPG, Image.PNG, Image.JPEG));
                FileUtils.covertMultipartToFile(examClassStoredPath, handledFile);
            }
        } catch (Exception exception) {
            log.error(MessageConst.EXCEPTION_LOG_FORMAT, exception.getMessage(), exception.getCause(), exception);
        }
    }

    @Override
    public CustomInputStreamResource downloadStudentHandledAnswerSheet(String examClassCode, String type) {
        // Check existed exam_class
        ExamClass examClass = examClassRepository.findByCodeAndIsEnabled(examClassCode, Boolean.TRUE)
            .orElseThrow(() -> exceptionFactory.resourceNotFoundException(MessageConst.ExamClass.NOT_FOUND,
                Resources.EXAM_CLASS,
                MessageConst.RESOURCE_NOT_FOUND, ErrorKey.ExamClass.CODE, String.valueOf(examClassCode)));

        // open exam-class folder
        File sharedAppDataDir;
        String sharedAppDataPath = FileUtils.getSharedAppDirectoryDataPath();
        sharedAppDataDir = new File(sharedAppDataPath);
        if (!sharedAppDataDir.exists()) {
            log.info("Make sharedAppDataDir {}", sharedAppDataDir.mkdirs() ? "successfully" : "fail");
        }
        // zip exam-class folder
        String outputFileName = (Objects.equals(type, "PROCESSED") ? "ProcessedHandledSheets"
            : "UnProcessedHandledSheets") +
            CharacterConstants.UNDERLINED + examClass.getCode() + ".zip";
        String folderByType = Objects.equals(type, "UNPROCESSED") ? "" : "HandledSheets";
        String exClassTargetDirectoryPath = String.format("%s/%s/%s/%s/", sharedAppDataPath, ANSWERED_SHEETS,
            examClass.getCode(), folderByType);
        // TODO: Write generic method to zip all directories (both parent and child)
        File targetDir = new File(exClassTargetDirectoryPath);
        ByteArrayOutputStream byteArrOs = new ByteArrayOutputStream();
        ZipOutputStream zipOs = new ZipOutputStream(byteArrOs);
        // iterate folders -> filter if img file
        List<String> filePaths = Arrays.stream(Objects.requireNonNull(targetDir.listFiles()))
            .filter(file -> file.isFile() && Objects.equals(
                FileUtils.getFileType(FileUtils.getFileExt(file.getName())), FileUtils.IMAGES_FILE_TYPE))
            .map(file -> exClassTargetDirectoryPath + file.getName()).collect(
                Collectors.toList());
        try {
            for (String filePath : filePaths) {
                File file = new File(filePath);
                zipOs.putNextEntry(new ZipEntry(file.getName()));
                FileInputStream fis = new FileInputStream(file);
                IOUtils.copy(fis, zipOs);
                fis.close();
                zipOs.closeEntry();
            }
            zipOs.finish();
            zipOs.flush();
            IOUtils.closeQuietly(zipOs);
            IOUtils.closeQuietly(byteArrOs);
            InputStreamResource outputResource = new InputStreamResource(
                new ByteArrayInputStream(byteArrOs.toByteArray()));
            return new CustomInputStreamResource(outputFileName, outputResource);
        } catch (Exception e) {
            log.error(MessageConst.EXCEPTION_LOG_FORMAT, e.getMessage(), e.getCause(), e);
            IOUtils.closeQuietly(zipOs);
            IOUtils.closeQuietly(byteArrOs);
            return new CustomInputStreamResource(outputFileName, null);
        }
    }

    @Override
    public void deleteImagesInClassFolder(HandledImagesDeleteDTO deleteDTO) throws IOException {
        String sharedAppDataPath = FileUtils.getSharedAppDirectoryDataPath();
        File examClassStoredDir = new File(
            String.format("%s/%s/%s/", sharedAppDataPath, ANSWERED_SHEETS, deleteDTO.getExamClassCode()));
        int numFileDeleted = 0;
        if (examClassStoredDir.exists()) {
            for (File item : Objects.requireNonNull(examClassStoredDir.listFiles())) {
                if (item.isFile() && deleteDTO.getLstFileName().contains(item.getName())) {
                    boolean isDeleted = item.delete();
                    numFileDeleted += (isDeleted ? 1 : 0);
                }
            }
            // if any files deleted -> clean result folders -> scoring again
            if (numFileDeleted > 0) {
                for (File item : Objects.requireNonNull(examClassStoredDir.listFiles())) {
                    if (item.isDirectory()) {
                        org.apache.commons.io.FileUtils.cleanDirectory(item);
                    }
                }
            }
        }
    }

    @Override
    public List<FileAttachDTO> getListFileInExClassFolder(String examClassCode) {
        String sharedAppDataPath = FileUtils.getSharedAppDirectoryDataPath();
        File examClassStoredDir = new File(
            String.format("%s/%s/%s/", sharedAppDataPath, ANSWERED_SHEETS, examClassCode));
        List<FileAttachDTO> lstFileInFolder = new ArrayList<>();
        if (examClassStoredDir.exists()) {
            String serverPath = String.format("%s/data/%s/%s", SystemConstants.SHARED_DIR_SERVER_PATH, ANSWERED_SHEETS,
                examClassCode);
            for (File item : Objects.requireNonNull(examClassStoredDir.listFiles())) {
                if (item.isFile() && Objects.equals(FileUtils.getFileType(FileUtils.getFileExt(item)),
                    FileUtils.IMAGES_FILE_TYPE)) {
                    FileAttachDTO fileDTO = FileAttachDTO.builder()
                        .fileName(item.getName())
                        .filePath(String.format("%s/%s", serverPath, item.getName()))
                        .fileExt(FileNameUtils.getExtension(item.getName()))
                        .storedType(FileStoredTypeEnum.INTERNAL_SERVER.getType())
                        .build();
                    lstFileInFolder.add(fileDTO);
                }
            }
        }
        return lstFileInFolder;
    }

    @Transactional
    @Override
    public void saveScoringResults(String examClassCode, String tempFileCode, String option) throws IOException {
        User currentUser = ObjectUtils.getOrDefault(AuthUtils.getCurrentUser(),
            new User());
        // Lấy item -> lưu vào db từ temp data
        // Mở và lấy data từ file temp_scored_data.json
        String sharedAppDataPath = FileUtils.getSharedAppDirectoryDataPath();
        File sharedAppDataDir = new File(sharedAppDataPath);
        if (!sharedAppDataDir.exists()) {
            log.info("Make sharedAppDataDir {}", sharedAppDataDir.mkdirs() ? "successfully" : "fail");
        }
        File examClassTempScoredDataFile = new File(
            String.format("%s/%s/%s/%s", sharedAppDataPath, ANSWERED_SHEETS, examClassCode,
                FILE_TEMP_SCORED_DATA_RESPONSE));
        if (!examClassTempScoredDataFile.exists()) {
            throw exceptionFactory.resourceNotFoundException(MessageConst.RESOURCE_NOT_FOUND,
                MessageConst.RESOURCE_NOT_FOUND,
                "tempScoredData", FILE_TEMP_SCORED_DATA_RESPONSE, examClassCode);
        }
        if (Objects.equals(option, "SAVE")) {
            String json = org.apache.commons.io.FileUtils.readFileToString(examClassTempScoredDataFile,
                StandardCharsets.UTF_8);
            ScoringPreviewResDTO tempScoredDataRes = ObjectMapperUtils.mapping(json, ScoringPreviewResDTO.class);
            List<StudentTestSet> saveResults = Objects.requireNonNull(tempScoredDataRes).getPreviews().stream()
                .map(ScoringPreviewItemDTO::getStudentTestSet).filter(Objects::nonNull)
                .collect(Collectors.toList());
            // check if student_test_set_exists -> overwrite
            List<StudentTestSet> existedStudentTestSet = studentTestSetRepository.findAllByStudentIdInAndExamClassIdIn(
                saveResults.stream().map(StudentTestSet::getStudentId).collect(Collectors.toSet()),
                saveResults.stream().map(StudentTestSet::getExamClassId).collect(Collectors.toSet()));
            // remove if not contained both student_id and test_set_id
            Map<Pair<Long, Long>, Boolean> mapStdExamClassKeyPair = new LinkedHashMap<>();
            saveResults.forEach(item -> mapStdExamClassKeyPair
                .put(Pair.create(item.getStudentId(), item.getExamClassId()), Boolean.TRUE));
            existedStudentTestSet.removeIf(
                item -> !mapStdExamClassKeyPair.get(Pair.create(item.getStudentId(), item.getExamClassId())));
            studentTestSetRepository.deleteAllInBatch(existedStudentTestSet);

            // save new results in another thread
            Executor executor = SpringContextUtils.getBean("dbSaveAsyncTaskExecutor", Executor.class);
            executor.execute(() -> {
                saveListStudentTestSet(saveResults);
                Set<Long> examClassIds = saveResults.stream().map(StudentTestSet::getExamClassId)
                    .collect(Collectors.toSet());
                List<String> examClassNames = examClassRepository.getListExamClassNameByIdsIn(examClassIds);
                // save and push a notification
                final String content = String.format(
                    NotificationContentEnum.SAVED_EXAM_CLASS_SCORING_RESULT_SUCCESSFULLY.getContent(),
                    String.join(",", examClassNames));
                notificationService.saveNotification(null, currentUser.getId(), content,
                    NotificationContentEnum.SAVED_EXAM_CLASS_SCORING_RESULT_SUCCESSFULLY,
                    String.join(",", examClassIds.stream().map(String::valueOf).collect(Collectors.toSet())),
                    NotificationObjectTypeEnum.EXAM_CLASS);
                // Push notification to user through FCM
                notificationService.sendFCMNotification(
                    NotificationFCMReqDTO.builder()
                        .fcmToken(currentUser.getFcmToken())
                        .title(NotificationContentEnum.SAVED_EXAM_CLASS_SCORING_RESULT_SUCCESSFULLY.getTitle())
                        .content(content)
                        .targetUserId(currentUser.getId())
                        .build());
            });
            // xóa cả temp-file
            // boolean deletedFile = examClassTempScoredDataFile.delete();
        } else if (Objects.equals(option, "DELETE")) {
            // xóa temp-file
            // temp-saving-file-> hiện tại không dùng (currently unused)
            // File tempSavingDataFile = new File(SystemConstants.RESOURCE_PATH +
            // FileUtils.DOCUMENTS_STORED_LOCATION +
            // String.format(FILE_TEMP_SCORED_RESULTS_DATA, tempFileCode));
            // org.apache.commons.io.FileUtils.delete(tempSavingDataFile);
            // boolean deletedFile = examClassTempScoredDataFile.delete();
            boolean deletedFile = Boolean.TRUE;
        } else {
            throw new BadRequestException("option invalid", "option", "option");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveListStudentTestSet(List<StudentTestSet> saveResults) {
        long startedMillis = System.currentTimeMillis();
        Map<String, File> mapFiles = new LinkedHashMap<>();
        saveResults.forEach(
            result -> mapFiles.put(result.getHandedTestFile().getName(),
                new File(result.getHandledFileAbsolutePath())));
        Map<String, FileUploadResDTO> mapUploadResults;
        // check profile to choose upload strategy
        if (Objects.equals(defaultStoredType, FileStoredTypeEnum.INTERNAL_SERVER.toString())) {
            mapUploadResults = fileAttachService.uploadListFileToInternalServer(mapFiles, FileTypeEnum.IMAGE);
        } else { // external server: Cloudinary/ AWS S3
            mapUploadResults = fileAttachService.uploadListFileToCloudinary(mapFiles, FileTypeEnum.IMAGE);
        }
        for (StudentTestSet resultItem : saveResults) {
            FileUploadResDTO uploadResult = mapUploadResults.get(resultItem.getHandedTestFile().getName());
            if (Objects.nonNull(uploadResult)) {
                if (Objects.equals(uploadResult.getStoredTypeEnum(), FileStoredTypeEnum.EXTERNAL_SERVER)) {
                    resultItem.getHandedTestFile().setFilePath(null);
                    resultItem.getHandedTestFile().setExternalLink(uploadResult.getFilePath());
                } else { // INTERNAL_SERVER
                    resultItem.getHandedTestFile().setFilePath(uploadResult.getFilePath());
                    resultItem.getHandedTestFile().setExternalLink(null);
                }
                resultItem.getHandedTestFile().setStoredType(uploadResult.getStoredTypeEnum().getType());
            }
        }
        studentTestSetRepository.saveAll(saveResults);
        log.info("====== SAVED RESULTS SUCCESSFULLY AFTER {} ======", System.currentTimeMillis() - startedMillis);
    }

    /**
     * Load scored student's answer sheets from shared folder
     */
    private List<StudentHandledTestDTO> loadListStudentScoredSheets(String exClassCode, List<String> warningMessages,
        Set<String> selectedImages) {
        List<StudentHandledTestDTO> lstScoredData = new ArrayList<>();
        Set<String> selectedFileNames = selectedImages.contains("ALL") ? selectedImages
            : selectedImages.stream()
                .map(item -> FileUtils.getFileBodyName(item) + "_data.json").collect(Collectors.toSet());
        File scoredSheetsDir;
        String sharedAppDataPath = FileUtils.getSharedAppDirectoryDataPath();
        scoredSheetsDir = new File(
            String.format("%s/%s/%s/%s", sharedAppDataPath, ANSWERED_SHEETS, exClassCode, SCORED_SHEETS));
        if (scoredSheetsDir.exists() && scoredSheetsDir.isDirectory()) {
            for (File scoredDataFile : Objects.requireNonNull(scoredSheetsDir.listFiles())) {
                if (selectedImages.contains("ALL") || selectedFileNames.contains(scoredDataFile.getName())) {
                    try {
                        String jsonData = org.apache.commons.io.FileUtils.readFileToString(scoredDataFile, "UTF-8");
                        StudentHandledTestDTO scoredData = ObjectMapperUtils.objectMapper(jsonData,
                            StudentHandledTestDTO.class);
                        if (Objects.nonNull(scoredData)) {
                            lstScoredData.add(scoredData);
                        }
                    } catch (IOException e) {
                        log.error(MessageConst.EXCEPTION_LOG_FORMAT, e.getMessage(), e.getCause(), e);
                    }
                }
            }
        }
        // read warning messages
        File warningFile = new File(
            String.format("%s/%s/%s/%s", sharedAppDataPath, ANSWERED_SHEETS, exClassCode, MAY_BE_WRONG));
        if (warningFile.exists() && warningFile.isFile()) {
            try {
                Scanner scanner = new Scanner(warningFile);
                // scanner.useDelimiter("\n");
                while (scanner.hasNextLine()) {
                    warningMessages.add(scanner.nextLine());
                }
                scanner.close();
            } catch (Exception e) {
                log.error(MessageConst.EXCEPTION_LOG_FORMAT, e.getMessage(), e.getCause(), e);
            }
        }
        return lstScoredData;
    }

    /**
     * Call Python AI model to process input images using OS command
     */
    private void callAIModelProcessingUsingCMD(String examClassCode) {
        try {
            // invoke cmd through a process
            ProcessBuilder processBuilder = new ProcessBuilder();
            processBuilder.redirectErrorStream(true);
            String[] commands;
            String sharedAppAISrc;
            if (SystemConstants.IS_WINDOWS) {
                sharedAppAISrc = SystemConstants.WINDOWS_SHARED_DIR + "/source/be_python";
                commands = new String[]{"cmd.exe", "/c", String.format("python main.py %s", examClassCode)};
            } else {
                sharedAppAISrc = SystemConstants.LINUX_SHARED_DIR + "/source/be_python";
                commands = new String[]{"sh", "-c", String.format("python main.py %s", examClassCode)};
            }
            processBuilder.command(commands);
            // point directory to python src (create and clone before using)
            processBuilder.directory(new File(sharedAppAISrc));
            Process aiProcess = processBuilder.start();
            aiProcess.supportsNormalTermination();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(aiProcess.getInputStream()));
            long currentTimeMillis = System.currentTimeMillis();
            log.info("========== AI PROCESS STARTED ========");
            String logLine;
            while ((logLine = bufferedReader.readLine()) != null) {
                System.out.println(logLine);
            }
            log.info("========= AI PROCESS ENDED AFTER : {} ms ==========",
                System.currentTimeMillis() - currentTimeMillis);
        } catch (Exception exception) {
            log.error(MessageConst.EXCEPTION_LOG_FORMAT, exception.getMessage(), exception.getCause(), exception);
        }
    }

    /**
     * Call Python AI model to process input images using RestAPI service
     */
    private void callAIModelProcessingUsingAPI(String examClassCode, String mode, Integer numberAnswers,
        Set<String> selectedImages) {
        try {
            String requestURL = pythonAIDomain + scoringAIApiPath;
            long currentTimeMillis = System.currentTimeMillis();
            log.info("========== STARTED CALLING API MODULE PYTHON: {} ========", requestURL);
            MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
            headers.put("apiKey", Collections.singletonList(pythonModuleApiKey));
            HttpEntity<Object> httpEntity = new HttpEntity<>(
                new AIExtractionReqDTO(examClassCode, mode, numberAnswers, selectedImages), headers);
            Object response = null;
            restTemplate.exchange(requestURL, HttpMethod.POST, httpEntity, Object.class);
            log.info("========= ENDED CALLING API MODULE PYTHON : {} ms ==========",
                System.currentTimeMillis() - currentTimeMillis);
            log.info("========== RESPONSE {} ========", ObjectMapperUtils.toJsonString(response));
        } catch (Exception exception) {
            log.error(MessageConst.EXCEPTION_LOG_FORMAT, exception.getMessage(), exception.getCause(), exception);
        }
    }

    /**
     * Save temporary scored data of an exam class
     */
    private void saveExamClassTempScoredData(String examClassCode, ScoringPreviewResDTO tempDataResponse) {
        log.info("========== STARTED saveExamClassTempScoredData ==========");
        long currentTimeMillis = System.currentTimeMillis();
        tempDataResponse.setTmpFileCode(
            String.format("%sTMP-%s-%s", AuthUtils.getCurrentUserId(), examClassCode, System.currentTimeMillis()));
        // store temp_data in folder of the exam_class
        try {
            String tempFilePath = String.format("%s/%s/%s/%s", FileUtils.getSharedAppDirectoryDataPath(),
                ANSWERED_SHEETS, examClassCode,
                FILE_TEMP_SCORED_DATA_RESPONSE);
            File tempDataFile = new File(tempFilePath);
            String data = ObjectMapperUtils.toJsonString(tempDataResponse);
            FileOutputStream fos = new FileOutputStream(tempDataFile);
            fos.write(data.getBytes(StandardCharsets.UTF_8));
            fos.close();
        } catch (Exception exception) {
            log.error(MessageConst.EXCEPTION_LOG_FORMAT, exception.getMessage(), exception.getCause(), exception);
        }
        log.info("========= ENDED saveExamClassTempScoredData AFTER : {} ms ==========",
            System.currentTimeMillis() - currentTimeMillis);
    }

    /**
     * Merge kết quả chấm mới với hiện tại theo key original file name (chấm 1 số bài)
     */
    public void mergeExamClassTempScoredData(String examClassCode, ScoringPreviewResDTO newScoredData) {
        String tempFilePath = String.format("%s/%s/%s/%s", FileUtils.getSharedAppDirectoryDataPath(), ANSWERED_SHEETS,
            examClassCode,
            FILE_TEMP_SCORED_DATA_RESPONSE);
        File tempDataFile = new File(tempFilePath);
        try {
            if (tempDataFile.exists()) {
                String json = org.apache.commons.io.FileUtils.readFileToString(tempDataFile, "UTF-8");
                ScoringPreviewResDTO currentTempData = ObjectMapperUtils.mapping(json, ScoringPreviewResDTO.class);
                // mapping các item kết quả mới theo key là originalFileName
                Map<String, ScoringPreviewItemDTO> mapCurrentResultItem = new LinkedHashMap<>();
                Objects.requireNonNull(currentTempData).getPreviews()
                    .forEach(item -> mapCurrentResultItem.put(item.getOriginalImgFileName(), item));
                // replace kết quả cũ
                for (ScoringPreviewItemDTO newItem : newScoredData.getPreviews()) {
                    ScoringPreviewItemDTO existedCurrentItem = mapCurrentResultItem
                        .get(newItem.getOriginalImgFileName());
                    // nếu image được chấm lại -> replace kết quả theo key là originalFileName
                    if (Objects.nonNull(existedCurrentItem)) {
                        BeanUtils.copyProperties(newItem, existedCurrentItem);
                    } else { // nếu chưa tồn tại -> add
                        currentTempData.getPreviews().add(newItem);
                    }
                }
                // ghi đè lại kết quả
                String data = ObjectMapperUtils.toJsonString(currentTempData);
                FileOutputStream fos = new FileOutputStream(tempDataFile);
                fos.write(data.getBytes(StandardCharsets.UTF_8));
                fos.close();
            } else {
                String data = ObjectMapperUtils.toJsonString(newScoredData);
                FileOutputStream fos = new FileOutputStream(tempDataFile);
                fos.write(data.getBytes(StandardCharsets.UTF_8));
                fos.close();
            }
        } catch (IOException exception) {
            log.error(MessageConst.EXCEPTION_LOG_FORMAT, exception.getMessage(), exception.getCause(), exception);
        }
    }

    /**
     *
     * temporary scored data of an exam class
     */
    @Override
    public ScoringPreviewResDTO loadExamClassTempScoredData(String examClassCode, Set<String> studentCodes) {
        ExamClass examClass = examClassRepository.findByCodeAndIsEnabled(examClassCode, Boolean.TRUE)
            .orElseThrow(() -> exceptionFactory.resourceNotFoundException(MessageConst.ExamClass.NOT_FOUND,
                Resources.EXAM_CLASS,
                MessageConst.RESOURCE_NOT_FOUND, ErrorKey.ExamClass.CODE, String.valueOf(examClassCode)));
        String tempFilePath = String.format("%s/%s/%s/%s", FileUtils.getSharedAppDirectoryDataPath(), ANSWERED_SHEETS,
            examClassCode,
            FILE_TEMP_SCORED_DATA_RESPONSE);
        try {
            File tempDataFile = new File(tempFilePath);
            if (tempDataFile.exists()) {
                String jsonData = org.apache.commons.io.FileUtils.readFileToString(tempDataFile, "UTF-8");
                ScoringPreviewResDTO response = ObjectMapperUtils.mapping(jsonData, ScoringPreviewResDTO.class);
                if (!studentCodes.contains("ALL")) {
                    Objects.requireNonNull(response).getPreviews()
                        .removeIf(item -> !studentCodes.contains(item.getStudentCode()));
                }
                Set<String> allStudentCodes = examClassRepository
                    .getListExamClassParticipant(examClass.getId(),
                        Collections.singleton(UserExamClassRoleEnum.STUDENT.getType()))
                    .stream().map(IExamClassParticipantDTO::getCode).collect(
                        Collectors.toSet());
                Objects.requireNonNull(response).setStudentCodes(allStudentCodes);
                return response;
            } else {
                return new ScoringPreviewResDTO();
            }
        } catch (Exception e) {
            log.error(MessageConst.EXCEPTION_LOG_FORMAT, e.getMessage(), e.getCause(), e);
            return new ScoringPreviewResDTO();
        }
    }
}
