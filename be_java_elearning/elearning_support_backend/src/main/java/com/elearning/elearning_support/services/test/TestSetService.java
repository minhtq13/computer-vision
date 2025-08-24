package com.elearning.elearning_support.services.test;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.elearning.elearning_support.dtos.CustomInputStreamResource;
import com.elearning.elearning_support.dtos.fileAttach.FileAttachDTO;
import com.elearning.elearning_support.dtos.studentTestSet.StudentHandledTestDTO;
import com.elearning.elearning_support.dtos.test.studentTestSet.HandledImagesDeleteDTO;
import com.elearning.elearning_support.dtos.test.testSet.ITestSetPreviewDTO;
import com.elearning.elearning_support.dtos.test.testSet.ScoringImageReqDTO;
import com.elearning.elearning_support.dtos.test.testSet.ScoringPreviewItemDTO;
import com.elearning.elearning_support.dtos.test.testSet.ScoringPreviewResDTO;
import com.elearning.elearning_support.dtos.test.testSet.TestSetCreateDTO;
import com.elearning.elearning_support.dtos.test.testSet.TestSetDetailDTO;
import com.elearning.elearning_support.dtos.test.testSet.TestSetGenerateReqDTO;
import com.elearning.elearning_support.dtos.test.testSet.TestSetPreviewDTO;
import com.elearning.elearning_support.dtos.test.testSet.TestSetSearchReqDTO;
import com.elearning.elearning_support.dtos.test.testSet.TestSetUpdateDTO;

@Service
public interface TestSetService {

    /**
     * Gen bộ đề thi tự động
     */
    List<TestSetPreviewDTO> generateTestSet(TestSetGenerateReqDTO generateReqDTO);

    /**
     *
     */
    TestSetPreviewDTO createTestSet(TestSetCreateDTO createDTO);

    /**
     * Chi tiết đề thi
     */
    TestSetDetailDTO getTestSetDetail(TestSetSearchReqDTO searchReqDTO);

    /**
     * Export file word đề thi
     */
    InputStreamResource exportTestSet(TestSetSearchReqDTO searchReqDTO) throws IOException;


    /**
     *
     */
    void updateTestSet(TestSetUpdateDTO updateDTO);
    /**
     *  ======================== TEST SET SCORING SERVICES ====================
     */
    List<ScoringPreviewItemDTO> scoreStudentTestSet(String examClassCode, List<StudentHandledTestDTO> handledTestSets);

    /**
     * Process answered sheets and score by exClassCode
     */
    ScoringPreviewResDTO scoreExamClassTestSet(String examClassCode, ScoringImageReqDTO scoringImagesReqDTO);

    /**
     * Upload handled answer sheet's images
     */
    void uploadStudentHandledAnswerSheet(String examClassCode, MultipartFile[] handledFiles) throws IOException;

    CustomInputStreamResource downloadStudentHandledAnswerSheet(String examClassCode, String type);

    /**
     * Delete handled answer sheet's images in exam class folder
     */
    void deleteImagesInClassFolder(HandledImagesDeleteDTO deleteDTO) throws IOException;

    /**
     * Get list file uploaded in exam class folder
     */
    List<FileAttachDTO> getListFileInExClassFolder(String examClassCode);


    /**
     * Save scored results
     */
    void saveScoringResults(String examClassCode, String tempFileCode, String option) throws IOException;

    /**
     * List testSet by testId
     */
    List<ITestSetPreviewDTO> getListTestSetPreview(Long testId);

    /**
     * delete a test set
     */
    void deleteTestSetById(Long testSetId);

    /**
     * load temporary scored data of an exam class
     */
    ScoringPreviewResDTO loadExamClassTempScoredData(String examClassCode, Set<String> studentCodes);

}
