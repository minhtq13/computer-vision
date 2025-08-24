package com.elearning.elearning_support.controllers.restful.subject;

import java.util.Collections;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.elearning.elearning_support.components.BaseController;
import com.elearning.elearning_support.dtos.subject.ISubjectListDTO;
import com.elearning.elearning_support.dtos.subject.SubjectDetailDTO;
import com.elearning.elearning_support.dtos.subject.SubjectSaveReqDTO;
import com.elearning.elearning_support.services.subject.SubjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/subject")
@Tag(name = "APIs học phần (Subject)")
@RequiredArgsConstructor
public class SubjectController {

    private final SubjectService subjectService;

    @PostMapping
    @Operation(summary = "Tạo học phần")
    @PreAuthorize("@dynamicAccessAspect.hasAnyPermission('P_SUBJECT_CREATE')")
    public void createSubject(@RequestBody @Validated SubjectSaveReqDTO createDTO) {
        subjectService.createSubject(createDTO);
    }

    @PutMapping("/{subjectId}")
    @Operation(summary = "Cập nhật học phần")
    @PreAuthorize("@dynamicAccessAspect.hasAnyPermission('P_SUBJECT_UPDATE')")
    public void updateSubject(@PathVariable(name = "subjectId") Long subjectId,
            @RequestBody @Validated SubjectSaveReqDTO updateDTO) {
        subjectService.updateSubject(subjectId, updateDTO);
    }

    @GetMapping("/list")
    @Operation(summary = "Danh sách học phần")
    @PreAuthorize("@dynamicAccessAspect.hasAnyRole('ADMIN', 'TEACHER')")
    public Page<ISubjectListDTO> getListSubject(
            @RequestParam(name = "search", required = false, defaultValue = "") String search,
            @RequestParam(name = "departmentId", required = false, defaultValue = "-1") Long departmentId,
            @RequestParam(name = "departmentName", required = false, defaultValue = "") String departmentName,
            @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(name = "size", required = false, defaultValue = "10") Integer size,
            @RequestParam(name = "sort", required = false, defaultValue = "modifiedAt,desc") String sort) {
        Pageable pageable = BaseController.getPageable(page, size, Collections.singletonList(sort));
        return subjectService.getListSubject(search, departmentId, departmentName, pageable);
    }

    @GetMapping("/detail/{subjectId}")
    @Operation(summary = "Chi tiết học phần")
    @PreAuthorize("@dynamicAccessAspect.hasAnyRole('ADMIN', 'TEACHER')")
    public SubjectDetailDTO getSubjectDetail(@PathVariable(name = "subjectId") Long subjectId) {
        return subjectService.getSubjectDetail(subjectId);
    }

}
