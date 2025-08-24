package com.elearning.elearning_support.services.studentTestSet.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.math3.util.Pair;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.elearning.elearning_support.annotations.dataSource.DataSource;
import com.elearning.elearning_support.constants.message.errorKey.ErrorKey;
import com.elearning.elearning_support.constants.message.messageConst.MessageConst;
import com.elearning.elearning_support.constants.message.messageConst.MessageConst.Resources;
import com.elearning.elearning_support.dtos.CustomInputStreamResource;
import com.elearning.elearning_support.dtos.common.CommonNameValueDTO;
import com.elearning.elearning_support.dtos.examClass.IExamClassParticipantDTO;
import com.elearning.elearning_support.dtos.notification.NotificationFCMReqDTO;
import com.elearning.elearning_support.dtos.test.studentTestSet.ExamClassResultStatisticsDTO;
import com.elearning.elearning_support.dtos.test.studentTestSet.IStudentTestSetDTO;
import com.elearning.elearning_support.dtos.test.studentTestSet.IStudentTestSetResultDTO;
import com.elearning.elearning_support.dtos.test.studentTestSet.StdTestSetDetailItemDTO;
import com.elearning.elearning_support.dtos.test.studentTestSet.StudentTestSetDetailsDTO;
import com.elearning.elearning_support.dtos.test.studentTestSet.StudentTestSetResultDTO;
import com.elearning.elearning_support.dtos.test.studentTestSet.SubmissionDTO;
import com.elearning.elearning_support.dtos.test.studentTestSet.SubmissionDataItem;
import com.elearning.elearning_support.dtos.test.studentTestSet.SubmissionUpdateResDTO;
import com.elearning.elearning_support.dtos.test.testQuestion.TestQuestionAnswerResDTO;
import com.elearning.elearning_support.dtos.test.testSet.ITestQuestionCorrectAnsDTO;
import com.elearning.elearning_support.dtos.test.testSet.ITestSetResDTO;
import com.elearning.elearning_support.entities.examClass.ExamClass;
import com.elearning.elearning_support.entities.studentTest.StudentTestSet;
import com.elearning.elearning_support.enums.commons.TimeIntervalEnum;
import com.elearning.elearning_support.enums.dataSource.DataSourceRouteEnum;
import com.elearning.elearning_support.enums.examClass.UserExamClassRoleEnum;
import com.elearning.elearning_support.enums.notification.NotificationContentEnum;
import com.elearning.elearning_support.enums.notification.NotificationObjectTypeEnum;
import com.elearning.elearning_support.enums.test.StudentTestStatusEnum;
import com.elearning.elearning_support.enums.test.TestTypeEnum;
import com.elearning.elearning_support.exceptions.exceptionFactory.ExceptionFactory;
import com.elearning.elearning_support.repositories.postgres.examClass.ExamClassRepository;
import com.elearning.elearning_support.repositories.postgres.studentTestSet.StudentTestSetRepository;
import com.elearning.elearning_support.repositories.postgres.test.test_set.TestSetRepository;
import com.elearning.elearning_support.repositories.postgres.users.UserRepository;
import com.elearning.elearning_support.services.notification.NotificationService;
import com.elearning.elearning_support.services.studentTestSet.StudentTestSetService;
import com.elearning.elearning_support.utils.CollectionUtils;
import com.elearning.elearning_support.utils.DateUtils;
import com.elearning.elearning_support.utils.StringUtils;
import com.elearning.elearning_support.utils.auth.AuthUtils;
import com.elearning.elearning_support.utils.excelFile.ExcelFileUtils;
import com.elearning.elearning_support.utils.object.ObjectUtils;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StudentTestSetServiceImpl implements StudentTestSetService {

    private final StudentTestSetRepository studentTestSetRepository;

    private final ExamClassRepository examClassRepository;

    private final ExceptionFactory exceptionFactory;

    private final TestSetRepository testSetRepository;

    private final ExcelFileUtils excelFileUtils;

    private final UserRepository userRepository;

    private final NotificationService notificationService;

    @Override
    public ExamClassResultStatisticsDTO getListStudentTestSetResult(String examClassCode) {
        // Check examClass exists
        ExamClass examClass = examClassRepository.findByCodeAndIsEnabled(examClassCode, Boolean.TRUE).orElseThrow(
            () -> exceptionFactory.resourceNotFoundException(MessageConst.ExamClass.NOT_FOUND, MessageConst.RESOURCE_NOT_FOUND,
                Resources.EXAM_CLASS, ErrorKey.ExamClass.CODE, examClassCode));

        // Get student test results
        List<StudentTestSetResultDTO> results = studentTestSetRepository.getStudentTestSetResult(examClass.getId()).stream()
            .map(item -> new StudentTestSetResultDTO(item, examClass.getId(), examClass.getCode())).collect(
                Collectors.toList());
        List<Double> studentResultPoints = results.stream().map(StudentTestSetResultDTO::getTotalPoints).sorted(Comparator.comparing(Double::doubleValue)).collect(
            Collectors.toList());

        // calculate pie chart
        List<Double> rangePoints = Arrays.asList(0.0, 3.0, 4.0, 5.5, 6.5, 7.0, 8.0, 8.5, 9.5, 10.0); // fixed
        List<CommonNameValueDTO> pieChart = new ArrayList<>();
        for (int idx = 0; idx < rangePoints.size() - 1; idx++) {
            final int currentIdx = idx;
            Long value = studentResultPoints.stream().filter(stdPoint -> {
                double roundedPoint = Math.round(stdPoint * 2) / 2.0;
                return roundedPoint >= rangePoints.get(currentIdx) && (
                    roundedPoint < rangePoints.get(currentIdx + 1) || (currentIdx + 1 == rangePoints.size() - 1));
            }).count();
            pieChart.add(new CommonNameValueDTO(String.format("%.1f - %.1f", rangePoints.get(currentIdx), rangePoints.get(currentIdx + 1)), value));
        }

        // calculate column chart
        double pointStep = 0.5;
        List<Double> gradePoints = CollectionUtils.generateDoubleSequenceWithStep(0.0, 10.0, pointStep);
        List<CommonNameValueDTO> columChart = new ArrayList<>();
        gradePoints.forEach(point -> {
            Long value = studentResultPoints.stream().filter(stdPoint -> Math.round(stdPoint * 2) / 2.0 == point).count();
            columChart.add(new CommonNameValueDTO(String.format("%.1f", point), value));
        });

        Boolean isPublishedAll = Objects.equals(examClass.getTestType(), TestTypeEnum.ONLINE.getType()) &&
            !studentTestSetRepository.existedNotPublishedInExamClass(examClass.getId());
        Boolean isHandled = isPublishedAll && studentTestSetRepository.existsByExamClassIdAndStatusIn(examClass.getId(), Arrays.asList(StudentTestStatusEnum.SUBMITTED.getType(), StudentTestStatusEnum.DUE.getType()));
        return ExamClassResultStatisticsDTO.builder()
            .results(results)
            .isPublishedAll(isPublishedAll)
            .isHandled(isHandled)
            .pieChart(pieChart)
            .columnChart(columChart)
            .build();
    }

    @Override
    public CustomInputStreamResource exportStudentTestSetResult(String examClassCode) throws IOException {
        String fileName = String.format("Result_ExamClass_%s_%s.xlsx", examClassCode,
            DateUtils.formatDateWithPattern(new Date(), DateUtils.FORMAT_DATE_YYYY_MMDD_HHMMSS));
        ExamClass examClass = examClassRepository.findByCodeAndIsEnabled(examClassCode, Boolean.TRUE).orElseThrow(
            () -> exceptionFactory.resourceNotFoundException(MessageConst.ExamClass.NOT_FOUND, MessageConst.RESOURCE_NOT_FOUND,
                Resources.EXAM_CLASS, ErrorKey.ExamClass.CODE, examClassCode));

        // exam_class studentId
        List<IExamClassParticipantDTO> examClassParticipants = examClassRepository.getListExamClassParticipant(examClass.getId(),
            Collections.singleton(UserExamClassRoleEnum.STUDENT.getType()));

        // Get map student test results
        Map<Long, IStudentTestSetResultDTO> mapResult = new LinkedHashMap<>();
        List<IStudentTestSetResultDTO> lstHandledResults = studentTestSetRepository.getStudentTestSetResult(examClass.getId());
        lstHandledResults.forEach(item -> mapResult.put(item.getStudentId(), item));
        // add to result
        List<StudentTestSetResultDTO> results = examClassParticipants.stream().map(participant -> {
            IStudentTestSetResultDTO studentResult = mapResult.get(participant.getId());
            StudentTestSetResultDTO studentResultItem;
            if (Objects.nonNull(studentResult)) {
                studentResultItem = new StudentTestSetResultDTO(studentResult, examClass.getId(), examClass.getCode());
            } else {
                studentResultItem = new StudentTestSetResultDTO(participant.getId(), participant.getName(), participant.getCode());
                studentResultItem.setExamClassCode(examClass.getCode());
            }
            return studentResultItem;
        }).collect(Collectors.toList());

        // map structure
        Map<Integer, Pair<String, String>> mapStructure = new LinkedHashMap<>();
        mapStructure.put(1, Pair.create("Tên thí sinh", "getStudentName"));
        mapStructure.put(2, Pair.create("MSSV", "getStudentCode"));
        mapStructure.put(3, Pair.create("Mã lớp thi", "getExamClassCode"));
        mapStructure.put(4, Pair.create("Mã đề thi", "getTestSetCode"));
        mapStructure.put(5, Pair.create("Trạng thái", "getStatusLabel"));
        mapStructure.put(6, Pair.create("Số câu hỏi trong đề", "getNumTestSetQuestions"));
        mapStructure.put(7, Pair.create("Số câu trả lời đúng", "getNumCorrectAnswers"));
        mapStructure.put(8, Pair.create("Câu đúng", "getCorrectAnswersStr"));
        mapStructure.put(9, Pair.create("Tổng điểm", "getTotalPoints"));
        return new CustomInputStreamResource(fileName, excelFileUtils.createWorkbook(results, mapStructure, examClassCode));
    }

    @Override
    public Page<IStudentTestSetDTO> getListOnlineStudentTestSet(String keyword, Long subjectId, Long semesterId, StudentTestStatusEnum status, TestTypeEnum testType, Pageable pageable) {
        return studentTestSetRepository.getListOnlineStudentTestSet(AuthUtils.getCurrentUserId(), keyword, subjectId, semesterId, status.getType(), testType.getType(), pageable);
    }

    @DataSource(value = DataSourceRouteEnum.SLAVE)
    @Override
    public Page<IStudentTestSetDTO> getListClosedStudentTestSet(String keyword, Pageable pageable) {
        return studentTestSetRepository.getListClosedStudentTestSet(AuthUtils.getCurrentUserId(), keyword, pageable);
    }

    @Override
    public StudentTestSetDetailsDTO getDetailsStudentTestSet(Long id) {
        StudentTestSet studentTestSet = findByIdAndStatusIn(id, Arrays.asList(StudentTestStatusEnum.values()));
        // common data of a test set
        ITestSetResDTO testSetCommon = testSetRepository.getTestSetDetail(studentTestSet.getTestSetId());

        // score
        Double totalPoints = 0.0;
        if (Arrays.asList(StudentTestStatusEnum.SUBMITTED.getType(), StudentTestStatusEnum.DUE.getType()).contains(studentTestSet.getStatus())) {
            totalPoints = studentTestSetRepository.getStudentTestSetResultById(studentTestSet.getId());
        }

        return StudentTestSetDetailsDTO.builder()
            .studentTestSetId(studentTestSet.getId())
            .studentId(studentTestSet.getStudentId())
            .testSetId(studentTestSet.getTestSetId())
            .startedTime(studentTestSet.getStartedTime())
            .allowedStartTime(studentTestSet.getAllowedStartTime())
            .allowedSubmitTime(studentTestSet.getAllowedSubmitTime())
            .savedTime(studentTestSet.getModifiedAt())
            .status(studentTestSet.getStatus())
            .statusTag(StudentTestStatusEnum.valueOf(studentTestSet.getStatus()))
            .questionQuantity(testSetCommon.getQuestionQuantity())
            .temporarySubmission(ObjectUtils.getOrDefault(studentTestSet.getTemporarySubmissions(), new ArrayList<>()))
            .totalPoints(totalPoints)
            .testName(testSetCommon.getTestName())
            .duration(testSetCommon.getDuration())
            .subjectTitle(testSetCommon.getSubjectTitle())
            .subjectCode(testSetCommon.getSubjectCode())
            .semester(testSetCommon.getSemester())
            .savedTime(studentTestSet.getModifiedAt())
            .build();
    }

    @Override
    public List<TestQuestionAnswerResDTO> loadDetailsStudentTestSet(Long studentTestSetId, Long testSetId) {
        return testSetRepository.getListTestSetQuestion(testSetId).stream().map(item -> new TestQuestionAnswerResDTO(item, Boolean.TRUE))
            .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public SubmissionUpdateResDTO startAttemptTest(SubmissionDTO submissionDTO) {
        // update only if status = 'OPEN'
        StudentTestSet studentTestSet = findByIdAndStatus(submissionDTO.getStudentTestSetId(), StudentTestStatusEnum.OPEN);
        // TODO: Implement logic check test is available (prevent temporary in Frontend)
        studentTestSet.setModifiedAt(submissionDTO.getSaveTime());
        studentTestSet.setModifiedBy(AuthUtils.getCurrentUserId()); // save by system
        studentTestSet.setTemporarySubmissions(submissionDTO.getSubmissionData());
        studentTestSet.setStatus(StudentTestStatusEnum.IN_PROGRESS.getType());
        studentTestSet.setStartedTime(submissionDTO.getStartedTime());
        studentTestSetRepository.save(studentTestSet);
        return new SubmissionUpdateResDTO(studentTestSet.getId(), submissionDTO.getStartedTime());
    }

    @Transactional
    @Override
    public SubmissionUpdateResDTO submitTest(SubmissionDTO submissionDTO) {
        Long currentUserId = AuthUtils.getCurrentUserId();
        // submit only if status = 'IN_PROGRESS'
        StudentTestSet studentTestSet = findByIdAndStatus(submissionDTO.getStudentTestSetId(), StudentTestStatusEnum.IN_PROGRESS);
        // update data in student_test_set
        Date paddedSubmissionTime = DateUtils.calculateDateByInterval(submissionDTO.getSubmittedTime(), 1,
            TimeIntervalEnum.SECOND.name()); // padding 1s to avoid delay in connectivity
        studentTestSet.setModifiedAt(paddedSubmissionTime);
        studentTestSet.setModifiedBy(currentUserId); // save by system
        studentTestSet.setTemporarySubmissions(submissionDTO.getSubmissionData());
        studentTestSet.setFinalSubmissions(submissionDTO.getSubmissionData());
        StudentTestStatusEnum status =
            Objects.nonNull(submissionDTO.getSubmittedTime()) && studentTestSet.getAllowedSubmitTime().after(paddedSubmissionTime)
                ? StudentTestStatusEnum.SUBMITTED : StudentTestStatusEnum.DUE;
        studentTestSet.setStatus(status.getType());
        studentTestSet.setIsSubmitted(StudentTestStatusEnum.SUBMITTED.equals(status) ? Boolean.TRUE : Boolean.FALSE);
        studentTestSet.setSubmittedTime(studentTestSet.getIsSubmitted() ? paddedSubmissionTime : null);
        studentTestSet.setSynchronizedStatus(0); // not synchronized

        // check submitting conditions
        if (StudentTestStatusEnum.SUBMITTED.equals(status)) {
            Set<ITestQuestionCorrectAnsDTO> correctAnswers = testSetRepository.getListTestQuestionCorrectAns(studentTestSet.getTestSetId());
            List<StdTestSetDetailItemDTO> lstDetails = new ArrayList<>();

            // Scoring submission
            int numNotMarkedQuestions = 0;
            // get correct answers of test_set
            Map<Integer, ITestQuestionCorrectAnsDTO> mapQuestionCorrectAns = new HashMap<>();
            correctAnswers.forEach(item -> mapQuestionCorrectAns.put(item.getQuestionNo(), item));
            for (SubmissionDataItem submissionItem : submissionDTO.getSubmissionData()) {
                // Get selected answers and check if not marked
                Set<Integer> selectedAnsNo = submissionItem.getSelectedAnswers();
                if (org.apache.commons.lang3.ObjectUtils.isEmpty(selectedAnsNo)) {
                    numNotMarkedQuestions++;
                }
                // Get correct answer of question in this test set
                ITestQuestionCorrectAnsDTO correctAnswerDTO = mapQuestionCorrectAns.get(submissionItem.getQuestionNo());
                if (Objects.isNull(correctAnswerDTO)) {
                    continue;
                }
                Set<Integer> correctAnswerNo = StringUtils.convertStrIntegerToSet(correctAnswerDTO.getCorrectAnswerNo());
                // Create new StudentTestSetDetails instance
                StdTestSetDetailItemDTO studentAnswerDetail = new StdTestSetDetailItemDTO();
                studentAnswerDetail.setTestSetQuestionId(correctAnswerDTO.getId());
                studentAnswerDetail.setSelectedAnswers(selectedAnsNo.toArray(Integer[]::new));
                studentAnswerDetail.setCorrectAnswers(correctAnswerNo.toArray(Integer[]::new));
                studentAnswerDetail.setIsEnabled(Boolean.TRUE);
                studentAnswerDetail.setCreatedAt(new Date());
                studentAnswerDetail.setCreatedBy(currentUserId);
                if (!org.apache.commons.lang3.ObjectUtils.isEmpty(correctAnswerNo) && !org.apache.commons.lang3.ObjectUtils.isEmpty(selectedAnsNo) &&
                    correctAnswerNo.size() == selectedAnsNo.size() &&
                    org.apache.commons.collections4.CollectionUtils.containsAll(correctAnswerNo, selectedAnsNo)) {
                    // true answer
                    studentAnswerDetail.setIsCorrected(Boolean.TRUE);
                } else { // false answer
                    studentAnswerDetail.setIsCorrected(Boolean.FALSE);
                }
                lstDetails.add(studentAnswerDetail);
            }
            studentTestSet.setMarked(correctAnswers.size() - numNotMarkedQuestions);
            studentTestSet.setMarkerRate(((double) (studentTestSet.getMarked()) / (correctAnswers.size())) * 100.0);
            studentTestSet.setStdTestSetDetail(lstDetails);

            // save to db
            studentTestSetRepository.save(studentTestSet);

            // notify after scoring
            String userFcmToken = userRepository.getFCMTokenByUserId(studentTestSet.getStudentId());
            if (org.apache.commons.lang3.ObjectUtils.isNotEmpty(userFcmToken)) {
                List<String> examClassNames = examClassRepository.getListExamClassNameByIdsIn(
                    Collections.singleton(studentTestSet.getExamClassId()));
                String content = String.format(NotificationContentEnum.SAVED_ONLINE_SCORING_RESULT_SUCCESSFULLY.getContent(),
                    examClassNames);
                // save a notification record
                notificationService.saveNotification(AuthUtils.getCurrentUserId(), studentTestSet.getStudentId(), content,
                    NotificationContentEnum.SAVED_ONLINE_SCORING_RESULT_SUCCESSFULLY, String.valueOf(studentTestSet.getId()),
                    NotificationObjectTypeEnum.ONLINE_TEST_DETAIL);
                // push FCM
                notificationService.sendFCMNotification(NotificationFCMReqDTO.builder()
                    .fcmToken(userFcmToken)
                    .title(NotificationContentEnum.SAVED_ONLINE_SCORING_RESULT_SUCCESSFULLY.getTitle())
                    .content(content)
                    .targetUserId(studentTestSet.getStudentId())
                    .build()
                );
            }
        }
        return new SubmissionUpdateResDTO(studentTestSet.getId(), submissionDTO.getSubmittedTime());
    }

    @Transactional
    @Override
    public SubmissionUpdateResDTO saveTemporarySubmission(SubmissionDTO submissionDTO) {
        // update only if status = 'IN_PROGRESS'
        StudentTestSet studentTestSet = findByIdAndStatus(submissionDTO.getStudentTestSetId(), StudentTestStatusEnum.IN_PROGRESS);
        studentTestSet.setModifiedAt(submissionDTO.getSaveTime());
        studentTestSet.setModifiedBy(AuthUtils.getCurrentUserId());
        studentTestSet.setTemporarySubmissions(submissionDTO.getSubmissionData());
        studentTestSetRepository.save(studentTestSet);
        return new SubmissionUpdateResDTO(studentTestSet.getId(), studentTestSet.getModifiedAt());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void scanDueStudentTestSet() {
        studentTestSetRepository.updateAllStudentTestSetDueStatus(DateUtils.getCurrentDateTime());
    }

    /**
     * find by id and status
     */
    private StudentTestSet findByIdAndStatus(Long id, StudentTestStatusEnum status) {
        return studentTestSetRepository.findByIdAndStatusAndIsEnabled(id, status.getType(), Boolean.TRUE)
            .orElseThrow(() -> exceptionFactory.resourceNotFoundException(MessageConst.StudentTestSet.NOT_FOUND, Resources.STUDENT_TEST_SET,
                MessageConst.RESOURCE_NOT_FOUND, ErrorKey.StudentTestSet.ID + "-" + ErrorKey.StudentTestSet.STATUS, id + "-" + status.name()));
    }

    /**
     * find by id and many status
     */
    private StudentTestSet findByIdAndStatusIn(Long id, List<StudentTestStatusEnum> statusEnums) {
        return studentTestSetRepository.findByIdAndStatusInAndIsEnabled(id, statusEnums.stream().map(StudentTestStatusEnum::getType).collect(
                Collectors.toSet()), Boolean.TRUE)
            .orElseThrow(() -> exceptionFactory.resourceNotFoundException(MessageConst.StudentTestSet.NOT_FOUND, Resources.STUDENT_TEST_SET,
                MessageConst.RESOURCE_NOT_FOUND, ErrorKey.StudentTestSet.ID + "-" + ErrorKey.StudentTestSet.STATUS,
                id + "-" + statusEnums.stream().map(StudentTestStatusEnum::toString).collect(Collectors.joining(","))));
    }
}
