package com.elearning.elearning_support.services.question;

import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.elearning.elearning_support.dtos.fileAttach.importFile.ImportResponseDTO;
import com.elearning.elearning_support.dtos.question.QuestionDetailsDTO;
import com.elearning.elearning_support.dtos.question.QuestionListCreateDTO;
import com.elearning.elearning_support.dtos.question.QuestionListDTO;
import com.elearning.elearning_support.dtos.question.QuestionListResDTO;
import com.elearning.elearning_support.dtos.question.QuestionUpdateDTO;
import com.elearning.elearning_support.enums.question.QuestionLevelEnum;

@Service
public interface QuestionService {

    void createListQuestion(QuestionListCreateDTO createDTO);

    void updateQuestion(Long questionId, QuestionUpdateDTO updateDTO);

    QuestionListResDTO getListQuestion(Long subjectId, String subjectCode, Set<Long> chapterIds, String chapterCode, QuestionLevelEnum level,
        String search, Long testId, Long tagId, Integer fetchSize);

    Page<QuestionListDTO> getPageQuestion(Long subjectId, String subjectCode, Set<Long> chapterIds, String chapterCode, QuestionLevelEnum level,
        String search, Long testId, Long tagId, Pageable pageable);

    /**
     * Import questions
     */
    ImportResponseDTO importQuestion(MultipartFile fileImport);


    /**
     * Get question details
     */
    QuestionDetailsDTO getQuestionDetails(Long questionId);


    /**
     * Xóa question
     */
    void deleteQuestion(Long questionId);


}
