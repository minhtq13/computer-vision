package com.elearning.elearning_support.controllers.restful.question;

import java.util.Collections;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.elearning.elearning_support.components.BaseController;
import com.elearning.elearning_support.dtos.fileAttach.importFile.ImportResponseDTO;
import com.elearning.elearning_support.dtos.question.QuestionDetailsDTO;
import com.elearning.elearning_support.dtos.question.QuestionListCreateDTO;
import com.elearning.elearning_support.dtos.question.QuestionListDTO;
import com.elearning.elearning_support.dtos.question.QuestionListResDTO;
import com.elearning.elearning_support.dtos.question.QuestionUpdateDTO;
import com.elearning.elearning_support.enums.question.QuestionLevelEnum;
import com.elearning.elearning_support.services.question.QuestionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/question")
@Tag(name = "APIs Câu hỏi (Question)")
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService questionService;

    @PostMapping
    @Operation(summary = "Tạo bộ câu hỏi")
    @PreAuthorize("@dynamicAccessAspect.hasAnyPermission('P_QUESTION_CREATE')")
    public void createQuestion(@RequestBody QuestionListCreateDTO createDTO) {
        questionService.createListQuestion(createDTO);
    }

    @PostMapping(value = "/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Import câu hỏi")
    @PreAuthorize("@dynamicAccessAspect.hasAnyPermission('P_QUESTION_CREATE')")
    public ImportResponseDTO importQuestion(@RequestParam(name = "file") MultipartFile file) {
        return questionService.importQuestion(file);
    }

    @GetMapping
    @Operation(summary = "Danh sách câu hỏi")
    @PreAuthorize("@dynamicAccessAspect.hasAnyRole('ADMIN', 'TEACHER')")
    public QuestionListResDTO getListQuestion(
            @RequestParam(name = "subjectId", required = false, defaultValue = "-1") Long subjectId,
            @RequestParam(name = "subjectCode", required = false, defaultValue = "") String subjectCode,
            @RequestParam(name = "chapterCode", required = false, defaultValue = "") String chapterCode,
            @RequestParam(name = "chapterIds", required = false, defaultValue = "-1") Set<Long> chapterIds,
            @RequestParam(name = "level", required = false, defaultValue = "ALL") QuestionLevelEnum level,
            @RequestParam(name = "search", required = false, defaultValue = "") String search,
            @RequestParam(name = "testId", required = false, defaultValue = "-1") Long testId,
            @RequestParam(name = "tagId", required = false, defaultValue = "-1") Long tagId,
            @RequestParam(name = "fetchSize", required = false, defaultValue = "50") Integer fetchSize) {
        return questionService.getListQuestion(subjectId, subjectCode, chapterIds, chapterCode, level, search, testId,
                tagId, fetchSize);
    }

    @GetMapping("/page")
    @Operation(summary = "Danh sách câu hỏi dạng phân trang")
    @PreAuthorize("@dynamicAccessAspect.hasAnyRole('ADMIN', 'TEACHER')")
    public Page<QuestionListDTO> getPageQuestion(
            @RequestParam(name = "subjectId", required = false, defaultValue = "-1") Long subjectId,
            @RequestParam(name = "subjectCode", required = false, defaultValue = "") String subjectCode,
            @RequestParam(name = "chapterCode", required = false, defaultValue = "") String chapterCode,
            @RequestParam(name = "chapterIds", required = false, defaultValue = "-1") Set<Long> chapterIds,
            @RequestParam(name = "level", required = false, defaultValue = "ALL") QuestionLevelEnum level,
            @RequestParam(name = "search", required = false, defaultValue = "") String search,
            @RequestParam(name = "testId", required = false, defaultValue = "-1") Long testId,
            @RequestParam(name = "tagId", required = false, defaultValue = "-1") Long tagId,
            @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(name = "size", required = false, defaultValue = "10") Integer size,
            @RequestParam(name = "sort", required = false, defaultValue = "id,desc") String sort) {
        Pageable pageable = BaseController.getPageable(page, size, Collections.singletonList(sort));
        return questionService.getPageQuestion(subjectId, subjectCode, chapterIds, chapterCode, level, search, testId,
                tagId, pageable);
    }

    @PreAuthorize("@dynamicAccessAspect.hasAnyPermission('P_QUESTION_DETAIL')")
    @GetMapping("/detail/{questionId}")
    @Operation(summary = "Chi tiết câu hỏi")
    public QuestionDetailsDTO getQuestionDetails(@PathVariable(name = "questionId") Long questionId) {
        return questionService.getQuestionDetails(questionId);
    }

    @PutMapping("/{questionId}")
    @Operation(summary = "Cập nhật câu hỏi")
    @PreAuthorize("@dynamicAccessAspect.hasAnyPermission('P_QUESTION_UPDATE')")
    public void updateQuestion(@PathVariable(name = "questionId") Long questionId,
            @RequestBody @Validated QuestionUpdateDTO updateDTO) {
        questionService.updateQuestion(questionId, updateDTO);
    }

    @DeleteMapping("/{questionId}")
    @Operation(summary = "Xóa câu hỏi")
    @PreAuthorize("@dynamicAccessAspect.hasAnyPermission('P_QUESTION_DELETE')")
    public void deleteQuestion(@PathVariable(name = "questionId") Long questionId) {
        questionService.deleteQuestion(questionId);
    }
}
