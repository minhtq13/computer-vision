package com.elearning.elearning_support.services.studentTestSet;

import java.io.IOException;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.elearning.elearning_support.dtos.CustomInputStreamResource;
import com.elearning.elearning_support.dtos.test.studentTestSet.ExamClassResultStatisticsDTO;
import com.elearning.elearning_support.dtos.test.studentTestSet.IStudentTestSetDTO;
import com.elearning.elearning_support.dtos.test.studentTestSet.StudentTestSetDetailsDTO;
import com.elearning.elearning_support.dtos.test.studentTestSet.SubmissionDTO;
import com.elearning.elearning_support.dtos.test.studentTestSet.SubmissionUpdateResDTO;
import com.elearning.elearning_support.dtos.test.testQuestion.TestQuestionAnswerResDTO;
import com.elearning.elearning_support.enums.test.StudentTestStatusEnum;
import com.elearning.elearning_support.enums.test.TestTypeEnum;

@Service
public interface StudentTestSetService {

    /**
     * Get student test results by examClassCode
     */
    ExamClassResultStatisticsDTO getListStudentTestSetResult(String examClassCode);

    /**
     * export student test results by examClassCode
     */
    CustomInputStreamResource exportStudentTestSetResult(String examClassCode) throws IOException;

    /**
     * Update data after an interval
     */
    SubmissionUpdateResDTO saveTemporarySubmission(SubmissionDTO submissionDTO);

    /**
     * start attempt test
     */
    SubmissionUpdateResDTO startAttemptTest(SubmissionDTO submissionDTO);


    /**
     * submit test
     */
    SubmissionUpdateResDTO submitTest(SubmissionDTO submissionDTO);

    /**
     * details student test-set
     */
    StudentTestSetDetailsDTO getDetailsStudentTestSet(Long id);

    /**
     * Danh sách bài thi online đang mở
     */
    Page<IStudentTestSetDTO> getListOnlineStudentTestSet(String keyword, Long subjectId, Long semesterId, StudentTestStatusEnum status, TestTypeEnum testType, Pageable pageable);

    /**
     * Danh sách bài thi đã đóng
     */
    Page<IStudentTestSetDTO> getListClosedStudentTestSet(String keyword, Pageable pageable);

    /**
     * Tải đề thi của thí sinh
     */
    List<TestQuestionAnswerResDTO> loadDetailsStudentTestSet(Long studentTestSetId, Long testSetId);

    /**
     * Scan and update due student test set
     */
    void scanDueStudentTestSet();


}
