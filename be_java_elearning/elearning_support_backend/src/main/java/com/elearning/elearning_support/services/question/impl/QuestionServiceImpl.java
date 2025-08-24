package com.elearning.elearning_support.services.question.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;
import com.elearning.elearning_support.constants.FileConstants.Extension.Excel;
import com.elearning.elearning_support.constants.message.errorKey.ErrorKey;
import com.elearning.elearning_support.constants.message.messageConst.MessageConst;
import com.elearning.elearning_support.constants.message.messageConst.MessageConst.FileAttach;
import com.elearning.elearning_support.constants.message.messageConst.MessageConst.Resources;
import com.elearning.elearning_support.dtos.answer.AnswerReqDTO;
import com.elearning.elearning_support.dtos.chapter.ISubjectChapterDTO;
import com.elearning.elearning_support.dtos.common.ICommonIdCode;
import com.elearning.elearning_support.dtos.fileAttach.importFile.ImportResponseDTO;
import com.elearning.elearning_support.dtos.fileAttach.importFile.RowErrorDTO;
import com.elearning.elearning_support.dtos.question.IQuestionDetailsDTO;
import com.elearning.elearning_support.dtos.question.QuestionDetailsDTO;
import com.elearning.elearning_support.dtos.question.QuestionListCreateDTO;
import com.elearning.elearning_support.dtos.question.QuestionListDTO;
import com.elearning.elearning_support.dtos.question.QuestionListResDTO;
import com.elearning.elearning_support.dtos.question.QuestionUpdateDTO;
import com.elearning.elearning_support.dtos.question.importQuestion.ImportQuestionValidatorDTO;
import com.elearning.elearning_support.dtos.question.importQuestion.QuestionImportDTO;
import com.elearning.elearning_support.entities.answer.Answer;
import com.elearning.elearning_support.entities.question.Question;
import com.elearning.elearning_support.enums.importFile.ImportResponseEnum;
import com.elearning.elearning_support.enums.importFile.QuestionImportFieldMapping;
import com.elearning.elearning_support.enums.importFile.StudentImportFieldMapping;
import com.elearning.elearning_support.enums.question.QuestionLevelEnum;
import com.elearning.elearning_support.enums.system.SpringProfileEnum;
import com.elearning.elearning_support.exceptions.exceptionFactory.ExceptionFactory;
import com.elearning.elearning_support.repositories.postgres.answer.AnswerRepository;
import com.elearning.elearning_support.repositories.postgres.chapter.ChapterRepository;
import com.elearning.elearning_support.repositories.postgres.question.QuestionRepository;
import com.elearning.elearning_support.repositories.postgres.subject.SubjectRepository;
import com.elearning.elearning_support.repositories.postgres.test.test_question.TestQuestionRepository;
import com.elearning.elearning_support.repositories.postgres.test.test_set.TestSetQuestionRepository;
import com.elearning.elearning_support.services.question.QuestionService;
import com.elearning.elearning_support.services.subject.SubjectService;
import com.elearning.elearning_support.utils.DateUtils;
import com.elearning.elearning_support.utils.StringUtils;
import com.elearning.elearning_support.utils.auth.AuthUtils;
import com.elearning.elearning_support.utils.excelFile.ExcelFileUtils;
import com.elearning.elearning_support.utils.file.FileUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class QuestionServiceImpl implements QuestionService {

    private final QuestionRepository questionRepository;

    private final AnswerRepository answerRepository;

    private final ExceptionFactory exceptionFactory;

    private final SubjectRepository subjectRepository;

    private final ChapterRepository chapterRepository;

    private final TestQuestionRepository testQuestionRepository;

    private final TestSetQuestionRepository testSetQuestionRepository;

    private final SubjectService subjectService;

    @Value("${spring.profiles.active}")
    private String activeProfile;

    @Transactional
    @Override
    public void createListQuestion(QuestionListCreateDTO createDTO) {
        // Tạo các list để lưu question
        List<Question> lstQuestion = new ArrayList<>();
        createDTO.getLstQuestion().forEach(questionDTO -> {
            boolean isMultipleAnswers = questionDTO.getIsMultipleAnswers()  || (questionDTO.getLstAnswer().stream().filter(
                AnswerReqDTO::getIsCorrect).count() > 1);
            Question question = Question.builder()
                .imageIds(questionDTO.getLstImageId())
                .level(questionDTO.getLevel().getLevel())
                .isMultipleAnswers(isMultipleAnswers)
                .chapterId(createDTO.getChapterId())
                .content(questionDTO.getContent())
                .code(generateQuestionCode())
                .isNewest(Boolean.TRUE)
                .isEnabled(Boolean.TRUE)
                .tagIds(questionDTO.getTagIds().toArray(Long[]::new))
                .build();
            question.setCreatedAt(new Date());
            question.setCreatedBy(AuthUtils.getCurrentUserId());
            // Save question
            lstQuestion.add(question);
            // Tạo các answer của question
            question.setLstAnswer(questionDTO.getLstAnswer().stream().map(Answer::new).collect(Collectors.toList()));
        });
        // save question
        questionRepository.saveAll(lstQuestion);
    }

    @Transactional
    @Override
    public void updateQuestion(Long questionId, QuestionUpdateDTO updateDTO) {
        Long currentUserId = AuthUtils.getCurrentUserId();
        Question sourceQuestion = questionRepository.findById(questionId).orElseThrow(
            () -> exceptionFactory.resourceExistedException(MessageConst.Question.NOT_FOUND, Resources.QUESTION,
                MessageConst.RESOURCE_NOT_FOUND, ErrorKey.Question.ID, String.valueOf(questionId)));
        // isChanged from FE
        if (updateDTO.getIsChanged()) {
            // temporarily 09/07/2024
            boolean checkMpecProdApplyCondByEnv = Arrays.asList(SpringProfileEnum.PRODUCTION.getName(), SpringProfileEnum.DEV_MPEC.getName()).contains(activeProfile) ||
                !questionRepository.existsInUsedTestSet(questionId);
            // if the question is not in any used test-set
            if (checkMpecProdApplyCondByEnv) {

                // update question data
                sourceQuestion.setChapterId(updateDTO.getChapterId());
                sourceQuestion.setContent(updateDTO.getContent());
                sourceQuestion.setLevel(updateDTO.getLevel().getLevel());
                boolean isMultipleAnswers = updateDTO.getIsMultipleAnswers()  || (updateDTO.getLstAnswer().stream().filter(
                    AnswerReqDTO::getIsCorrect).count() > 1);
                sourceQuestion.setIsMultipleAnswers(isMultipleAnswers);
                sourceQuestion.setModifiedAt(DateUtils.getCurrentDateTime());
                sourceQuestion.setModifiedBy(currentUserId);
                sourceQuestion.setTagIds(updateDTO.getTagIds().toArray(Long[]::new));

                List<Long> currentAnswerIds = sourceQuestion.getLstAnswer().stream().sorted(Comparator.comparing(Answer::getId))
                    .map(Answer::getId)
                    .collect(Collectors.toList());
                List<Answer> lstNewAnswer = new ArrayList<>();
                List<Answer> lstRemovedAnswer = new ArrayList<>();
                int numCurrentAnswer = currentAnswerIds.size();
                int numUpdatedAnswer = updateDTO.getLstAnswer().size();
                if (numCurrentAnswer <= numUpdatedAnswer) {
                    int numNewAnswer = numUpdatedAnswer - numCurrentAnswer;
                    // update current answer
                    for (int i = 0; i < numCurrentAnswer; i++) {
                        org.springframework.beans.BeanUtils.copyProperties(updateDTO.getLstAnswer().get(i),
                            sourceQuestion.getLstAnswer().get(i));
                        sourceQuestion.getLstAnswer().get(i).setModifiedAt(new Date());
                        sourceQuestion.getLstAnswer().get(i).setModifiedBy(AuthUtils.getCurrentUserId());
                    }
                    // add new answer
                    if (numNewAnswer > 0) {
                        for (int i = numCurrentAnswer; i < numUpdatedAnswer; i++) {
                            lstNewAnswer.add(new Answer(sourceQuestion.getId(), updateDTO.getLstAnswer().get(i)));
                        }
                    }
                } else {
                    // update current answer
                    for (int i = 0; i < numUpdatedAnswer; i++) {
                        org.springframework.beans.BeanUtils.copyProperties(updateDTO.getLstAnswer().get(i),
                            sourceQuestion.getLstAnswer().get(i));
                        sourceQuestion.getLstAnswer().get(i).setModifiedAt(new Date());
                        sourceQuestion.getLstAnswer().get(i).setModifiedBy(AuthUtils.getCurrentUserId());
                    }
                    // remove answer
                    for (int i = numUpdatedAnswer; i < numCurrentAnswer; i++) {
                        lstRemovedAnswer.add(sourceQuestion.getLstAnswer().remove(i));
                    }
                }
                answerRepository.deleteAll(lstRemovedAnswer);
                answerRepository.saveAll(lstNewAnswer);

                // save sourceQuestion
                questionRepository.save(sourceQuestion);
            } else {
                // Check thay đổi dữ liệu => thay đổi thì clone newQuestion
                if (!checkQuestionChange(sourceQuestion, updateDTO).isEmpty()) {
                    Question newQuestion = new Question();
                    newQuestion.setIsEnabled(Boolean.TRUE);
                    newQuestion.setIsNewest(Boolean.TRUE);
                    newQuestion.setPreviousId(sourceQuestion.getId());
                    newQuestion.setBaseId(Objects.isNull(sourceQuestion.getBaseId()) ? sourceQuestion.getId() : sourceQuestion.getBaseId());
                    newQuestion.setCode(generateQuestionCode());
                    newQuestion.setContent(updateDTO.getContent());
                    boolean isMultipleAnswers = updateDTO.getIsMultipleAnswers()  || (updateDTO.getLstAnswer().stream().filter(
                        AnswerReqDTO::getIsCorrect).count() > 1);
                    newQuestion.setIsMultipleAnswers(isMultipleAnswers);
                    newQuestion.setLevel(updateDTO.getLevel().getLevel());
                    newQuestion.setChapterId(updateDTO.getChapterId());
                    newQuestion.setCreatedAt(new Date());
                    newQuestion.setCreatedBy(AuthUtils.getCurrentUserId());
                    newQuestion.setLstAnswer(updateDTO.getLstAnswer().stream().map(Answer::new).collect(Collectors.toList()));
                    newQuestion.setImageIds(updateDTO.getLstImageId());
                    newQuestion.setTagIds(updateDTO.getTagIds().toArray(Long[]::new));
                    questionRepository.save(newQuestion);

                    // set is_newest của question hiện tại = false
                    sourceQuestion.setIsNewest(Boolean.FALSE);
                } else {
                    sourceQuestion.setModifiedAt(new Date());
                    sourceQuestion.setModifiedBy(currentUserId);
                }
                questionRepository.save(sourceQuestion);
            }
        }
    }

    @Override
    public QuestionListResDTO getListQuestion(Long subjectId, String subjectCode, Set<Long> chapterIds, String chapterCode,
        QuestionLevelEnum level, String search, Long testId, Long tagId, Integer fetchSize) {
        Set<Long> viewableSubjectIds = subjectService.getListViewableSubjectIds();
        // if search by testId
        if (Objects.nonNull(testId) && !Objects.equals(testId, -1L)) {
            // fetch all
            List<QuestionListDTO> questions = questionRepository.getListQuestionInTest(testId, tagId, search, level.getLevel()).stream().map(QuestionListDTO::new).collect(Collectors.toList());
            return new QuestionListResDTO(questions.size(), questions.size(), questions);
        }
        if (!Objects.equals(subjectId, -1L) && !viewableSubjectIds.contains(-1L) && !viewableSubjectIds.contains(subjectId)) {
            return new QuestionListResDTO(0, 0, Collections.emptyList());
        }
        // fetch with specified size to reduce lag
        Integer limitSize = fetchSize != -1 ? fetchSize : Integer.MAX_VALUE;
        Set<Long> questionIds = questionRepository.getListQuestionIdsWithLimit(viewableSubjectIds, subjectId, subjectCode, chapterIds, chapterCode, level.getLevel(), tagId, search, limitSize);
        Integer totalSize = questionRepository.countListQuestion(viewableSubjectIds, subjectId, subjectCode, chapterIds, chapterCode, level.getLevel(), tagId, search);
        List<QuestionListDTO> questions = questionRepository.getListQuestion(questionIds).stream().map(QuestionListDTO::new).collect(Collectors.toList());
        return new QuestionListResDTO(questions.size(), totalSize, questions);
    }

    @Override
    public Page<QuestionListDTO> getPageQuestion(Long subjectId, String subjectCode, Set<Long> chapterIds, String chapterCode,
        QuestionLevelEnum level, String search, Long testId, Long tagId, Pageable pageable) {
        Set<Long> viewableSubjectIds = subjectService.getListViewableSubjectIds();
        // if search by testId
        if (Objects.nonNull(testId) && !Objects.equals(testId, -1L)) {
            return questionRepository.getPageQuestionInTest(testId, tagId, search, level.getLevel(), pageable).map(QuestionListDTO::new);
        }
        if (!Objects.equals(subjectId, -1L) && !viewableSubjectIds.contains(-1L) && !viewableSubjectIds.contains(subjectId)) {
            return Page.empty();
        }
        return questionRepository.getPageQuestion(viewableSubjectIds, subjectId, subjectCode, chapterIds, chapterCode, level.getLevel(), tagId,
            search, pageable).map(QuestionListDTO::new);
    }

    @Override
    public ImportResponseDTO importQuestion(MultipartFile fileImport) {
        Long currentUserId = AuthUtils.getCurrentUserId();
        // Tạo response mặc định
        ImportResponseDTO response = new ImportResponseDTO();
        response.setStatus(ImportResponseEnum.SUCCESS.getStatus());
        response.setMessage(ImportResponseEnum.SUCCESS.getMessage());

        // Đọc file và import dữ liệu
        try {
            // Validate file sơ bộ
            FileUtils.validateUploadFile(fileImport, Arrays.asList(Excel.XLS, Excel.XLSX));

            // Tạo workbook để đọc file import
            XSSFWorkbook inputWorkbook = new XSSFWorkbook(fileImport.getInputStream());
            XSSFSheet inputSheet = inputWorkbook.getSheetAt(0);
            if (Objects.isNull(inputSheet)) {
                throw exceptionFactory.fileUploadException(FileAttach.FILE_EXCEL_EMPTY_SHEET_ERROR, Resources.FILE_ATTACHED,
                    MessageConst.UPLOAD_FAILED);
            }

            // Map info validators
            Map<String, Long> mapSubjectCodeId = subjectRepository.getAllSubjectIdCode().stream()
                .collect(Collectors.toMap(ICommonIdCode::getCode, ICommonIdCode::getId));
            Map<Pair<Long, Integer>, Long> mapSubjectChapters = new HashMap<>();
            List<ISubjectChapterDTO> lstSubjectChapterMappings = chapterRepository.getAllSubjectChapterMappings();
            lstSubjectChapterMappings.forEach(
                mapping -> mapSubjectChapters.put(Pair.create(mapping.getSubjectId(), mapping.getChapterNo()), mapping.getChapterId()));
            ImportQuestionValidatorDTO validatorDTO = new ImportQuestionValidatorDTO(mapSubjectCodeId, mapSubjectChapters);

            // Tạo các map field
            Map<Integer, String> mapIndexColumnKey = new HashMap<>();
            // questions save to db
            List<Question> lstQuestions = new ArrayList<>();

            // Duyệt file input
            Iterator<Row> rowIterator = inputSheet.rowIterator();
            int numberOfColumns = QuestionImportFieldMapping.values().length;
            while (rowIterator.hasNext()) {
                Row currentRow = rowIterator.next();
                boolean isEmptyRow = true;
                if (currentRow.getRowNum() == 0) { // header row
                    // Validate thừa thiếu/cột
                    if (currentRow.getLastCellNum() < numberOfColumns) {
                        throw exceptionFactory.fileUploadException(FileAttach.FILE_EXCEL_MISSING_COLUMN_NUMBER_ERROR, Resources.FILE_ATTACHED,
                            MessageConst.UPLOAD_FAILED);
                    }
                    if (currentRow.getLastCellNum() > numberOfColumns) {
                        throw exceptionFactory.fileUploadException(FileAttach.FILE_EXCEL_MISSING_COLUMN_NUMBER_ERROR, Resources.FILE_ATTACHED,
                            MessageConst.UPLOAD_FAILED);
                    }
                    for (Cell cell : currentRow) {
                        // Validate tên cột
                        String columnKey = ExcelFileUtils.getStringCellValue(cell);
                        if (Objects.isNull(StudentImportFieldMapping.getObjectFieldByColumnKey(columnKey))){
                            throw exceptionFactory.fileUploadException(FileAttach.FILE_EXCEL_INVALID_COLUMN_NAME, Resources.FILE_ATTACHED,
                                MessageConst.UPLOAD_FAILED);
                        }
                        mapIndexColumnKey.put(cell.getColumnIndex(), columnKey);
                    }
                    continue;
                }
                // Duyệt các cell trong row
                QuestionImportDTO importDTO = new QuestionImportDTO();
                for (Cell cell : currentRow) {
                    String columnKey = mapIndexColumnKey.get(cell.getColumnIndex());
                    String objectFieldKey = QuestionImportFieldMapping.getObjectFieldByColumnKey(columnKey);
                    String cellValue = ExcelFileUtils.getStringCellValue(cell);
                    if (!ObjectUtils.isEmpty(cellValue)) {
                        isEmptyRow = false;
                    }
                    if (!ObjectUtils.isEmpty(objectFieldKey)) {
                        BeanUtils.setProperty(importDTO, objectFieldKey, cellValue);
                    }
                }
                // Validate và mapping vào entity
                List<String> causeList = new ArrayList<>();
                validateImportQuestion(importDTO, validatorDTO, causeList);
                if (!isEmptyRow) {
                    if (ObjectUtils.isEmpty(causeList)) {
                        // create new question
                        Question newQuestion = new Question();
                        newQuestion.setContent(String.format("<p>%s</p>", importDTO.getContent()));
                        Long subjectId = mapSubjectCodeId.get(importDTO.getSubjectCode());
                        Long chapterId = mapSubjectChapters.get(Pair.create(subjectId, Integer.valueOf(importDTO.getChapterNo())));
                        newQuestion.setChapterId(chapterId);
                        newQuestion.setCode(generateQuestionCode());
                        newQuestion.setLevel(QuestionLevelEnum.getQuestionLevelByVnName(importDTO.getLevelRaw()).getLevel());
                        newQuestion.setIsMultipleAnswers(Boolean.TRUE);
                        newQuestion.setIsNewest(Boolean.TRUE);
                        newQuestion.setCreatedBy(currentUserId);
                        newQuestion.setCreatedAt(new Date());
                        newQuestion.setTagIds(new Long[]{});
                        // create new answers (default has 4 answers/question)
                        List<Answer> lstAnswer = new ArrayList<>();
                        Set<Integer> correctAnswerNo = StringUtils.convertStrIntegerToSet(String.format("{%s}", importDTO.getCorrectAnswers()));
                        // Set answers
                        lstAnswer.add(new Answer(new AnswerReqDTO(String.format("<p>%s</p>", importDTO.getFirstAnswer()), correctAnswerNo.contains(1), null)));
                        lstAnswer.add(new Answer(new AnswerReqDTO(String.format("<p>%s</p>", importDTO.getSecondAnswer()), correctAnswerNo.contains(2), null)));
                        lstAnswer.add(new Answer(new AnswerReqDTO(String.format("<p>%s</p>", importDTO.getThirdAnswer()), correctAnswerNo.contains(3), null)));
                        lstAnswer.add(new Answer(new AnswerReqDTO(String.format("<p>%s</p>", importDTO.getFourthAnswer()), correctAnswerNo.contains(4), null)));
                        newQuestion.setLstAnswer(lstAnswer);
                        // Add question to list
                        lstQuestions.add(newQuestion);
                    } else {
                        response.getErrorRows().add(new RowErrorDTO(currentRow.getRowNum() + 1, importDTO, causeList));
                        response.setMessage(ImportResponseEnum.EXIST_INVALID_DATA.getMessage());
                        response.setStatus(ImportResponseEnum.EXIST_INVALID_DATA.getStatus());
                    }
                }
            }
            questionRepository.saveAll(lstQuestions);
            // save list entity
            inputWorkbook.close();
            // Set status and message response
            return response;
        } catch (IOException ioException) {
            response.setMessage(ImportResponseEnum.IO_ERROR.getMessage());
            response.setStatus(ImportResponseEnum.IO_ERROR.getStatus());
            log.error(MessageConst.EXCEPTION_LOG_FORMAT, ioException.getMessage(), ioException.getCause(), ioException);
        } catch (Exception exception) {
            response.setMessage(ImportResponseEnum.UNKNOWN_ERROR.getMessage());
            response.setStatus(ImportResponseEnum.UNKNOWN_ERROR.getStatus());
            log.error(MessageConst.EXCEPTION_LOG_FORMAT, exception.getMessage(), exception.getCause(), exception);
        }
        return null;
    }

    /**
     * Validate question import
     */
    private void validateImportQuestion(QuestionImportDTO importDTO, ImportQuestionValidatorDTO validatorDTO, List<String> causeList) {
        // Validate level
        if (Objects.isNull(QuestionLevelEnum.getQuestionLevelByVnName(importDTO.getLevelRaw()))) {
            causeList.add("Invalid question level");
        }
        // Validate subjectCode
        String notFoundMessage = "Not found ";
        List<String> errorFields = new ArrayList<>();
        try {
            if (ObjectUtils.isEmpty(importDTO.getSubjectCode())) {
                causeList.add("Missing subjectCode");
            } else {
                Long subjectId = validatorDTO.getMapSubjectCodeId().get(importDTO.getSubjectCode());
                if (Objects.isNull(subjectId)) {
                    errorFields.add("subjectCode");
                } else {
                    if (!validatorDTO.getMapSubjectChapters().containsKey(Pair.create(subjectId, Integer.valueOf(importDTO.getChapterNo())))) {
                        errorFields.add("chapterNo");
                    }
                }
                if (!errorFields.isEmpty()) {
                    causeList.add(notFoundMessage + String.join(",", errorFields));
                }

                // Validate correct answers
                if (ObjectUtils.isEmpty(importDTO.getCorrectAnswers())) {
                    causeList.add("Missing correctAnswers");
                }
            }
        } catch (NumberFormatException exception) {
            causeList.add("Invalid correctAnswers");
        }
    }

    @Override
    public QuestionDetailsDTO getQuestionDetails(Long questionId) {
        IQuestionDetailsDTO questionDetails = questionRepository.getQuestionDetails(questionId);
        if (Objects.isNull(questionDetails)) {
            throw exceptionFactory.resourceExistedException(MessageConst.Question.NOT_FOUND, Resources.QUESTION, MessageConst.RESOURCE_NOT_FOUND,
                ErrorKey.Question.ID, String.valueOf(questionId));
        }
        return new QuestionDetailsDTO(questionDetails);
    }

    @Transactional
    @Override
    public void deleteQuestion(Long questionId) {
        if (!questionRepository.existsById(questionId)) {
            throw exceptionFactory.resourceExistedException(MessageConst.Question.NOT_FOUND, Resources.QUESTION, MessageConst.RESOURCE_NOT_FOUND,
                ErrorKey.Question.ID, String.valueOf(questionId));
        } else if (testSetQuestionRepository.existsByQuestionId(questionId)) {
            throw exceptionFactory.badRequestException(MessageConst.Question.USED_IN_TEST_SET, Resources.QUESTION, MessageConst.RESOURCE_EXISTED,
                ErrorKey.Question.ID);
        } else {
            questionRepository.deleteById(questionId);
            // delete answer
            answerRepository.deleteAllByQuestionId(questionId);
            // delete test question
            testQuestionRepository.deleteAllByQuestionId(questionId);
            // delete testSetQuestion
            testSetQuestionRepository.deleteAllByQuestionId(questionId);
        }
    }

    /**
     * Generate question code
     */
    private String generateQuestionCode() {
        String baseCode = "Q";
        Random random = new Random();
        String generatedCode = baseCode + (random.nextInt(900000) + 100000);
        while (questionRepository.existsByCode(generatedCode)) {
            generatedCode = baseCode + (random.nextInt(900000) + 100000);
        }
        return generatedCode;
    }

    /**
     * Check question content changed
     */
    private List<String> checkQuestionChange(Question currentQuestion, QuestionUpdateDTO updateDTO) {
        List<String> changedList = new ArrayList<>();
        // question isMultipleAnswer
        if (!Objects.equals(currentQuestion.getChapterId(), updateDTO.getChapterId())) {
            changedList.add("chapterId");
        }
        // question content
        if (!Objects.equals(currentQuestion.getContent(), updateDTO.getContent())) {
            changedList.add("content");
        }
        // question level
        if (!Objects.equals(currentQuestion.getLevel(), updateDTO.getLevel().getLevel())) {
            changedList.add("level");
        }
        // question isMultipleAnswer
        if (!Objects.equals(currentQuestion.getIsMultipleAnswers(), updateDTO.getIsMultipleAnswers())) {
            changedList.add("isMultipleAnswers");
        }
        // check answer changes
        boolean isAnswerChanged = false;
        List<Answer> lstCurrentAnswer = currentQuestion.getLstAnswer();
        lstCurrentAnswer.sort(Comparator.comparing(Answer::getId));
        List<AnswerReqDTO> lstAnswerDTO = updateDTO.getLstAnswer();
        lstAnswerDTO.sort(Comparator.comparing(AnswerReqDTO::getCurrentId));
        if (!Objects.equals(lstCurrentAnswer.size(), updateDTO.getLstAnswer().size())) {
            isAnswerChanged = true;
        } else {
            for (int i = 0; i < Math.min(lstCurrentAnswer.size(), updateDTO.getLstAnswer().size()); i++) {
                Answer answer = lstCurrentAnswer.get(i);
                AnswerReqDTO answerDTO = lstAnswerDTO.get(i);
                if (!Objects.equals(answer.getContent(), answerDTO.getContent()) ||
                    !Objects.equals(answer.getIsCorrect(), answerDTO.getIsCorrect())) {
                    isAnswerChanged = true;
                    break;
                }
            }
        }
        if (isAnswerChanged) {
            changedList.add("answers");
        }
        return changedList;
    }
}
