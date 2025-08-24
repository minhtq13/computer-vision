package com.elearning.elearning_support.controllers.restful.test;

import java.util.Collections;
import java.util.Date;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
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
import com.elearning.elearning_support.components.BaseController;
import com.elearning.elearning_support.dtos.question.QuestionListDTO;
import com.elearning.elearning_support.dtos.question.QuestionListResDTO;
import com.elearning.elearning_support.dtos.test.TestDetailDTO;
import com.elearning.elearning_support.dtos.test.TestReqDTO;
import com.elearning.elearning_support.dtos.test.TestUpdateDTO;
import com.elearning.elearning_support.enums.commons.StatusEnum;
import com.elearning.elearning_support.enums.question.QuestionLevelEnum;
import com.elearning.elearning_support.enums.test.TestTypeEnum;
import com.elearning.elearning_support.services.test.TestService;
import com.elearning.elearning_support.utils.DateUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/test")
@Tag(name = "APIs Kỳ thi (Test)")
@RequiredArgsConstructor
public class TestController {

    private final TestService testService;

    @PreAuthorize("@dynamicAccessAspect.hasAnyPermission('P_TEST_CREATE')")
    @PostMapping("/create")
    @Operation(summary = "Tạo kỳ thi với bộ câu hỏi chọn trước")
    public Long createTest(@RequestBody @Validated TestReqDTO createDTO) {
        return testService.createTest(createDTO);
    }

    @PreAuthorize("@dynamicAccessAspect.hasAnyPermission('P_TEST_CREATE')")
    @PostMapping("/create/random")
    @Operation(summary = "Tạo kỳ thi với bộ câu hỏi random trong một môn học")
    public Long createRandomTest(@RequestBody @Validated TestReqDTO createDTO) {
        return testService.createRandomTest(createDTO);
    }

    @PreAuthorize("@dynamicAccessAspect.hasAnyPermission('P_TEST_UPDATE')")
    @PutMapping("/{testId}")
    @Operation(summary = "Cập nhật kỳ thi")
    public void updateTest(@PathVariable(name = "testId") Long testId,
            @RequestBody @Validated TestUpdateDTO updateDTO) {
        testService.updateTest(testId, updateDTO);
    }

    @GetMapping
    @Operation(summary = "Lấy danh sách các kỳ thi")
    public Page<TestDetailDTO> getListTest(
            @RequestParam(name = "subjectId", required = false, defaultValue = "-1") Long subjectId,
            @RequestParam(name = "subjectCode", required = false, defaultValue = "ALL") String subjectCode,
            @DateTimeFormat(pattern = DateUtils.FORMAT_DATE_DD_MM_YYYY_HH_MM, iso = ISO.DATE_TIME) @RequestParam(name = "startTime", required = false, defaultValue = "01/01/1970 00:00") Date startTime,
            @DateTimeFormat(pattern = DateUtils.FORMAT_DATE_DD_MM_YYYY_HH_MM, iso = ISO.DATE_TIME) @RequestParam(name = "endTime", required = false, defaultValue = "01/01/1970 00:00") Date endTime,
            @RequestParam(name = "semesterId", required = false, defaultValue = "-1") Long semesterId,
            @RequestParam(name = "semesterCode", required = false, defaultValue = "") String semesterCode,
            @RequestParam(name = "testType", required = false, defaultValue = "ALL") TestTypeEnum testType,
            @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(name = "size", required = false, defaultValue = "10") Integer size,
            @RequestParam(name = "sort", required = false, defaultValue = "modifiedAt,desc") String sort) {
        Pageable pageable = BaseController.getPageable(page, size, Collections.singletonList(sort));
        return testService.getListTest(subjectId, subjectCode, startTime, endTime, semesterId, semesterCode, testType,
                pageable);
    }

    @PreAuthorize("@dynamicAccessAspect.hasAnyPermission('P_TEST_DETAIL')")
    @GetMapping("/details/{testId}")
    @Operation(description = "Chi tiết kỳ thi")
    public TestDetailDTO getTestDetail(@PathVariable("testId") Long testId) {
        return testService.getTestDetail(testId);
    }

    @PreAuthorize("@dynamicAccessAspect.hasAnyPermission('P_TEST_UPDATE')")
    @PutMapping("/status/{testId}")
    @Operation(summary = "Đổi trạng thái hiển thị kỳ thi")
    public void switchTestStatus(@PathVariable(name = "testId") Long testId,
            @RequestParam(name = "newStatus") StatusEnum status) {
        testService.switchTestStatus(testId, status);
    }

    @PreAuthorize("@dynamicAccessAspect.hasAnyPermission('P_TEST_DELETE')")
    @DeleteMapping("/{testId}")
    @Operation(description = "Xóa bộ đề thi")
    public void deleteTest(@PathVariable(name = "testId") Long testId) {
        testService.deleteTest(testId);
    }

    @PreAuthorize("@dynamicAccessAspect.hasAnyPermission('P_TEST_DETAIL')")
    @GetMapping("/questions/allowed/{testId}")
    @Operation(description = "Lấy danh sách các câu hỏi được sử dụng (chọn/không chọn) trong một kỳ thi")
    public QuestionListResDTO getListQuestionAllowedUsingInTest(
            @PathVariable(name = "testId") Long testId,
            @RequestParam(name = "search", required = false, defaultValue = "") String search,
            @RequestParam(name = "level", required = false, defaultValue = "ALL") QuestionLevelEnum level) {
        return testService.getListQuestionAllowedUsingInTest(testId, search, level);
    }

    @PreAuthorize("@dynamicAccessAspect.hasAnyPermission('P_TEST_DETAIL')")
    @GetMapping("/questions/allowed/page/{testId}")
    @Operation(description = "Lấy danh sách các câu hỏi được sử dụng (chọn/không chọn) trong một kỳ thi")
    public Page<QuestionListDTO> getPageQuestionAllowedUsingInTest(
            @PathVariable(name = "testId") Long testId,
            @RequestParam(name = "search", required = false, defaultValue = "") String search,
            @RequestParam(name = "level", required = false, defaultValue = "ALL") QuestionLevelEnum level,
            @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(name = "size", required = false, defaultValue = "10") Integer size,
            @RequestParam(name = "sort", required = false, defaultValue = "modifiedAt,desc") String sort) {
        return testService.getPageQuestionAllowedUsingInTest(testId, search, level,
                BaseController.getPageable(page, size, Collections.singletonList(sort)));
    }

}
