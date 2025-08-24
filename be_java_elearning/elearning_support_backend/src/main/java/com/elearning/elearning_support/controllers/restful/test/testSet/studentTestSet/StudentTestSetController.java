package com.elearning.elearning_support.controllers.restful.test.testSet.studentTestSet;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.elearning.elearning_support.components.BaseController;
import com.elearning.elearning_support.dtos.CustomInputStreamResource;
import com.elearning.elearning_support.dtos.test.studentTestSet.ExamClassResultStatisticsDTO;
import com.elearning.elearning_support.dtos.test.studentTestSet.IStudentTestSetDTO;
import com.elearning.elearning_support.dtos.test.studentTestSet.StudentTestSetDetailsDTO;
import com.elearning.elearning_support.dtos.test.studentTestSet.SubmissionDTO;
import com.elearning.elearning_support.dtos.test.studentTestSet.SubmissionUpdateResDTO;
import com.elearning.elearning_support.dtos.test.testQuestion.TestQuestionAnswerResDTO;
import com.elearning.elearning_support.enums.test.StudentTestStatusEnum;
import com.elearning.elearning_support.enums.test.TestTypeEnum;
import com.elearning.elearning_support.services.studentTestSet.StudentTestSetService;
import com.elearning.elearning_support.utils.file.FileUtils.Excel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/std-test-set")
@Tag(name = "APIs Kết quả thi (StudentTestSet)")
@RequiredArgsConstructor
public class StudentTestSetController {

    private final StudentTestSetService studentTestSetService;

    @PreAuthorize("@dynamicAccessAspect.hasAnyPermission('P_TEST_DETAIL')")
    @GetMapping("/result/{examClassCode}")
    @Operation(summary = "Lấy kết quả thi theo mã lớp")
    public ExamClassResultStatisticsDTO getStudentTestSetResult(
            @PathVariable(name = "examClassCode") String examClassCode) {
        return studentTestSetService.getListStudentTestSetResult(examClassCode);
    }

    @PreAuthorize("@dynamicAccessAspect.hasAnyPermission('P_TEST_DETAIL')")
    @GetMapping(value = "/result/export/{examClassCode}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @Operation(summary = "Export kết quả thi theo lớp")
    public ResponseEntity<InputStreamResource> exportExamClassResult(
            @PathVariable(name = "examClassCode") String examClassCode) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        CustomInputStreamResource resource = studentTestSetService.exportStudentTestSetResult(examClassCode);
        headers.add(HttpHeaders.CONTENT_TYPE,
                MediaType.parseMediaType(String.join(";", Arrays.asList(Excel.CONTENT_TYPES))).toString());
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + resource.getFileName());
        return ResponseEntity.ok().headers(headers).body(resource.getResource());
    }

    @GetMapping("/online-test")
    @Operation(description = "Danh sách các bài thi đang mở (còn làm được và submit được bài thi)")
    public Page<IStudentTestSetDTO> getListOpeningTest(
            @RequestParam(name = "keyword", required = false, defaultValue = "") String keyword,
            @RequestParam(name = "subjectId", required = false, defaultValue = "-1") Long subjectId,
            @RequestParam(name = "semesterId", required = false, defaultValue = "-1") Long semesterId,
            @RequestParam(name = "testType", required = false, defaultValue = "ALL") TestTypeEnum testType,
            @RequestParam(name = "status", required = false, defaultValue = "ALL") StudentTestStatusEnum status,
            @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(name = "size", required = false, defaultValue = "10") Integer size,
            @RequestParam(name = "sort", required = false, defaultValue = "allowedStartTime,desc") String sort) {
        Pageable pageable = BaseController.getPageable(page, size, Collections.singletonList(sort));
        return studentTestSetService.getListOnlineStudentTestSet(keyword, subjectId, semesterId, status, testType,
                pageable);
    }

    @GetMapping("/details/{id}")
    @Operation(description = "Chi tiết bài thi của thí sinh")
    public StudentTestSetDetailsDTO getDetailStdTestSet(@PathVariable("id") Long id) {
        return studentTestSetService.getDetailsStudentTestSet(id);
    }

    @GetMapping("/details/test-set/{studentTestSetId}/{testSetId}")
    @Operation(description = "Tải đề thi của thí sinh")
    public List<TestQuestionAnswerResDTO> loadDetailsStudentTestSet(
            @PathVariable(name = "studentTestSetId") Long studentTestSetId,
            @PathVariable(name = "testSetId") Long testSetId) {
        return studentTestSetService.loadDetailsStudentTestSet(studentTestSetId, testSetId);
    }

    @GetMapping("/closed-tests")
    @Operation(description = "Danh sách các bài thi đã đóng (đã submit hoặc hết thời gian làm bài)")
    public Page<IStudentTestSetDTO> getListClosedTest(
            @RequestParam(name = "keyword", required = false, defaultValue = "") String keyword,
            @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(name = "size", required = false, defaultValue = "10") Integer size,
            @RequestParam(name = "sort", required = false, defaultValue = "allowedStartTime,desc") String sort) {
        Pageable pageable = BaseController.getPageable(page, size, Collections.singletonList(sort));
        return studentTestSetService.getListClosedStudentTestSet(keyword, pageable);
    }

    @PutMapping("/temp-submission")
    @Operation(description = "Cập nhật bài làm hiện tại của thí sinh (sau một khoảng thời gian nhất định)")
    public SubmissionUpdateResDTO saveTemporarySubmission(@RequestBody SubmissionDTO submissionDTO) {
        return studentTestSetService.saveTemporarySubmission(submissionDTO);
    }

    @PutMapping("/attempt")
    @Operation(description = "Bắt đầu làm bài thi")
    public SubmissionUpdateResDTO startAttemptTest(@RequestBody SubmissionDTO submissionDTO) {
        return studentTestSetService.startAttemptTest(submissionDTO);
    }

    @PutMapping("/submit")
    @Operation(description = "Submit bài thi")
    public SubmissionUpdateResDTO submitTest(@RequestBody SubmissionDTO submissionDTO) {
        return studentTestSetService.submitTest(submissionDTO);
    }
}
