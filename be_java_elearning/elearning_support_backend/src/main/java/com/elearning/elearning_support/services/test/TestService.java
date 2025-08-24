package com.elearning.elearning_support.services.test;

import java.util.Date;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.elearning.elearning_support.dtos.question.QuestionListDTO;
import com.elearning.elearning_support.dtos.question.QuestionListResDTO;
import com.elearning.elearning_support.dtos.test.TestDetailDTO;
import com.elearning.elearning_support.dtos.test.TestReqDTO;
import com.elearning.elearning_support.dtos.test.TestUpdateDTO;
import com.elearning.elearning_support.entities.test.Test;
import com.elearning.elearning_support.enums.commons.StatusEnum;
import com.elearning.elearning_support.enums.question.QuestionLevelEnum;
import com.elearning.elearning_support.enums.test.TestTypeEnum;

@Service
public interface TestService {

    /**
     * Tạo test từ môn học/chương
     */
    Long createRandomTest(TestReqDTO createDTO);


    /**
     * Tạo test từ bộ câu hỏi chọn sẵn
     */
    Long createTest(TestReqDTO createDTO);


    /**
     * Cập nhật test
     */
    void updateTest(Long testId, TestUpdateDTO updateDTO);

    /**
     * Get list test
     */
    Page<TestDetailDTO> getListTest(Long subjectId, String subjectCode, Date startTime, Date endTime, Long semesterId, String semesterCode, TestTypeEnum testType,
        Pageable pageable);

    /**
     * Test's detail
     */
    TestDetailDTO getTestDetail(Long testId);

    /**
     * Switch test status
     */
    void switchTestStatus(Long testId, StatusEnum status);

    /**
     * find test by id and enabled
     */
    Test findTestById(Long testId);

    /**
     * find test by id, type and enabled
     */
    Test findTestByIdAndType(Long testId, TestTypeEnum type);

    /**
     * check test existed by id and enabled
     */
    Boolean existsById(Long testId);


    /**
     * Delete test by id
     */
    void deleteTest(Long id);

    QuestionListResDTO getListQuestionAllowedUsingInTest(Long testId, String search, QuestionLevelEnum level);

    Page<QuestionListDTO> getPageQuestionAllowedUsingInTest(Long testId, String search, QuestionLevelEnum level, Pageable pageable);


}
