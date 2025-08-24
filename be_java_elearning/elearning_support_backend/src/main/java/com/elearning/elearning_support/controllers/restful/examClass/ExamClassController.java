package com.elearning.elearning_support.controllers.restful.examClass;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
import org.springframework.web.multipart.MultipartFile;
import com.elearning.elearning_support.components.BaseController;
import com.elearning.elearning_support.dtos.CustomInputStreamResource;
import com.elearning.elearning_support.dtos.examClass.ExamClassCreateDTO;
import com.elearning.elearning_support.dtos.examClass.ExamClassSaveReqDTO;
import com.elearning.elearning_support.dtos.examClass.ICommonExamClassDTO;
import com.elearning.elearning_support.dtos.examClass.IExamClassDetailDTO;
import com.elearning.elearning_support.dtos.examClass.IExamClassParticipantDTO;
import com.elearning.elearning_support.dtos.examClass.UserExamClassDTO;
import com.elearning.elearning_support.dtos.mail.MailBaseReqDTO;
import com.elearning.elearning_support.enums.examClass.UserExamClassRoleEnum;
import com.elearning.elearning_support.services.examClass.ExamClassService;
import com.elearning.elearning_support.utils.file.FileUtils.Excel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/exam-class")
@Tag(name = "APIs Lớp thi (Exam Class)")
@RequiredArgsConstructor
public class ExamClassController {

    private final ExamClassService examClassService;

    @PreAuthorize("@dynamicAccessAspect.hasAnyPermission('P_EXAM_CLASS_CREATE')")
    @PostMapping
    @Operation(summary = "Tạo lớp thi")
    public Long createExamClass(@RequestBody @Validated ExamClassCreateDTO createDTO) {
        return examClassService.createExamClass(createDTO);
    }

    @GetMapping("/page")
    @Operation(summary = "Danh sách lớp thi dạng page")
    public Page<ICommonExamClassDTO> getListExamClass(
            @RequestParam(name = "code", required = false, defaultValue = "") String code,
            @RequestParam(name = "semesterId", required = false, defaultValue = "-1") Long semesterId,
            @RequestParam(name = "subjectId", required = false, defaultValue = "-1") Long subjectId,
            @RequestParam(name = "testId", required = false, defaultValue = "-1") Long testId,
            @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(name = "size", required = false, defaultValue = "10") Integer size,
            @RequestParam(name = "sort", required = false, defaultValue = "modifiedAt,desc") String sort) {
        Pageable pageable = BaseController.getPageable(page, size, Collections.singletonList(sort));
        return examClassService.getPageExamClass(code, semesterId, subjectId, testId, pageable);
    }

    @PreAuthorize("@dynamicAccessAspect.hasAnyRole('ADMIN', 'TEACHER')")
    @GetMapping("/export")
    @Operation(summary = "Export danh sách lớp thi theo kỳ")
    public ResponseEntity<InputStreamResource> exportExamClass(
            @RequestParam(name = "semesterId", required = false, defaultValue = "-1") Long semesterId,
            @RequestParam(name = "testId", required = false, defaultValue = "-1") Long testId,
            @RequestParam(name = "subjectId", required = false, defaultValue = "-1") Long subjectId)
            throws IOException {
        HttpHeaders headers = new HttpHeaders();
        CustomInputStreamResource resourceRes = examClassService.exportListExamClass(semesterId, testId, subjectId);
        headers.add(HttpHeaders.CONTENT_TYPE,
                MediaType.parseMediaType(String.join(";", Arrays.asList(Excel.CONTENT_TYPES))).toString());
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + resourceRes.getFileName());
        return ResponseEntity.ok().headers(headers).body(resourceRes.getResource());
    }

    @GetMapping("/detail/{id}")
    @Operation(summary = "Chi tiết lớp thi")
    @PreAuthorize("@dynamicAccessAspect.hasAnyRole('ADMIN', 'TEACHER')")
    public IExamClassDetailDTO getDetailExamClass(@PathVariable(name = "id") Long id) {
        return examClassService.getExamClassDetail(id);
    }

    @PreAuthorize("@dynamicAccessAspect.hasAnyPermission('P_EXAM_CLASS_UPDATE')")
    @PutMapping("/{id}")
    @Operation(summary = "Cập nhật thông tin lớp thi")
    public void updateExamClass(@PathVariable(name = "id") Long id,
            @RequestBody @Validated ExamClassSaveReqDTO updateDTO) {
        examClassService.updateExamClass(id, updateDTO);
    }

    @PreAuthorize("@dynamicAccessAspect.hasAnyPermission('P_EXAM_CLASS_UPDATE')")
    @PutMapping("/participant")
    @Operation(summary = "Cập nhật Giám thị / Thí sinh vào lớp thi")
    public void updateParticipantToExamClass(@RequestBody @Validated UserExamClassDTO userExamClassDTO) {
        examClassService.updateParticipantToExamClass(userExamClassDTO);
    }

    @PreAuthorize("@dynamicAccessAspect.hasAnyRole('ADMIN', 'TEACHER')")
    @GetMapping("/participant/list/{examClassId}")
    @Operation(summary = "Lấy danh sách Giám thị / Thí sinh trong lớp thi")
    public List<IExamClassParticipantDTO> getListExamClassParticipant(
            @PathVariable(name = "examClassId") Long examClassId,
            @RequestParam(name = "roleType") UserExamClassRoleEnum roleType) {
        return examClassService.getListExamClassParticipant(examClassId, roleType);
    }

    @PreAuthorize("@dynamicAccessAspect.hasAnyRole('ADMIN', 'TEACHER')")
    @GetMapping("/participant/export/{examClassId}")
    @Operation(summary = "Export danh sách SV / GV lớp thi")
    public ResponseEntity<InputStreamResource> exportExamClassParticipant(
            @PathVariable(name = "examClassId") Long examClassId,
            @RequestParam(name = "roleType") UserExamClassRoleEnum roleType) throws IOException {
        CustomInputStreamResource resourceRes = examClassService.exportExamClassParticipant(examClassId, roleType);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE,
                MediaType.parseMediaType(String.join(";", Arrays.asList(Excel.CONTENT_TYPES))).toString());
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + resourceRes.getFileName());
        return ResponseEntity.ok().headers(headers).body(resourceRes.getResource());
    }

    @PreAuthorize("@dynamicAccessAspect.hasAnyPermission('P_EXAM_CLASS_UPDATE')")
    @PostMapping(value = "/participant/student/import/{examClassId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Import thí sinh vào lớp thi")
    public Set<Long> importStudentExamClass(@PathVariable(name = "examClassId") Long examClassId,
            @RequestParam(name = "file") MultipartFile importFile) throws IOException {
        return examClassService.importStudentExamClass(examClassId, importFile);
    }

    @PreAuthorize("@dynamicAccessAspect.hasAnyRole('ADMIN', 'TEACHER')")
    @PutMapping("/test-set/assign/{examClassId}")
    @Operation(description = "Assign đề thi online cho thí sinh trong lớp thi")
    public void assignOnlineTestSet(@PathVariable("examClassId") Long examClassId) {
        examClassService.assignOnlineTestSet(examClassId);
    }

    @PreAuthorize("@dynamicAccessAspect.hasAnyRole('ADMIN', 'TEACHER')")
    @PutMapping("/test-set/publish/{examClassId}")
    @Operation(description = "Publish đề thi online cho thí sinh trong lớp thi")
    public void publishOnlineTestSet(@PathVariable("examClassId") Long examClassId,
            @RequestParam(name = "isPublished") Boolean isPublished) {
        examClassService.publishOnlineTestSet(examClassId, isPublished);
    }

    @PreAuthorize("@dynamicAccessAspect.hasAnyRole('ADMIN', 'TEACHER')")
    @PostMapping("/result/send-email/{examClassId}")
    @Operation(description = "Gửi email danh sách và kết quả của lớp thi")
    public void sendEmailExamClassResult(
            @PathVariable(name = "examClassId") Long examClassId,
            @RequestBody @Validated MailBaseReqDTO mailReqDTO) throws IOException {
        examClassService.sendEmailExamClassResult(examClassId, mailReqDTO);
    }

}
