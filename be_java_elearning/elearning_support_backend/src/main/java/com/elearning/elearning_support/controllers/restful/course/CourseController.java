package com.elearning.elearning_support.controllers.restful.course;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
import com.elearning.elearning_support.dtos.CustomInputStreamResource;
import com.elearning.elearning_support.dtos.course.CourseSaveReqDTO;
import com.elearning.elearning_support.dtos.course.ICommonCourseDTO;
import com.elearning.elearning_support.dtos.course.ICourseDetailDTO;
import com.elearning.elearning_support.dtos.course.ICourseParticipantDTO;
import com.elearning.elearning_support.dtos.course.UserCourseDTO;
import com.elearning.elearning_support.enums.course.UserCourseRoleTypeEnum;
import com.elearning.elearning_support.services.course.CourseService;
import com.elearning.elearning_support.utils.file.FileUtils.Excel;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/course")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    @PreAuthorize("@dynamicAccessAspect.hasAnyRole('ADMIN', 'TEACHER') or @dynamicAccessAspect.hasAnyPermission('P_COURSE_CREATE'))")
    @PostMapping
    @Operation(summary = "Tạo lớp học")
    public Long createCourse(@RequestBody @Validated CourseSaveReqDTO createDTO) {
        return courseService.createCourse(createDTO);
    }

    @GetMapping("/page")
    @Operation(summary = "Danh sách lớp học dạng page")
    public Page<ICommonCourseDTO> getListCourse(
            @RequestParam(name = "code", required = false, defaultValue = "") String code,
            @RequestParam(name = "semesterId", required = false, defaultValue = "-1") Long semesterId,
            @RequestParam(name = "subjectId", required = false, defaultValue = "-1") Long subjectId,
            @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(name = "size", required = false, defaultValue = "10") Integer size,
            @RequestParam(name = "sort", required = false, defaultValue = "id") String sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort).descending());
        return courseService.getPageCourse(code, semesterId, subjectId, pageable);
    }

    @GetMapping("/export")
    @Operation(summary = "Export danh sách lớp học theo kỳ/học phần")
    public ResponseEntity<InputStreamResource> exportCourse(
            @RequestParam(name = "semesterId", required = false, defaultValue = "-1") Long semesterId,
            @RequestParam(name = "subjectId", required = false, defaultValue = "-1") Long subjectId,
            @RequestParam(name = "code", required = false, defaultValue = "") String code) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        CustomInputStreamResource resourceRes = courseService.exportListCourse(code, semesterId, subjectId);
        headers.add(HttpHeaders.CONTENT_TYPE,
                MediaType.parseMediaType(String.join(";", Arrays.asList(Excel.CONTENT_TYPES))).toString());
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + resourceRes.getFileName());
        return ResponseEntity.ok().headers(headers).body(resourceRes.getResource());
    }

    @GetMapping("/detail/{id}")
    @Operation(summary = "Chi tiết lớp học")
    @PreAuthorize("@dynamicAccessAspect.hasAnyRole('ADMIN', 'TEACHER')")
    public ICourseDetailDTO getDetailCourse(@PathVariable(name = "id") Long id) {
        return courseService.getCourseDetails(id);
    }

    @PreAuthorize("@dynamicAccessAspect.hasAnyRole('ADMIN', 'TEACHER')")
    @PutMapping("/{id}")
    @Operation(summary = "Cập nhật thông tin lớp học")
    public void updateCourse(@PathVariable(name = "id") Long id, @RequestBody @Validated CourseSaveReqDTO updateDTO) {
        courseService.updateCourse(id, updateDTO);
    }

    @PreAuthorize("@dynamicAccessAspect.hasAnyPermission('P_COURSE_UPDATE')")
    @PutMapping("/participant")
    @Operation(summary = "Cập nhật Giảng viên / Thí sinh vào lớp học")
    public void updateCourseParticipant(@RequestBody @Validated UserCourseDTO userCourseDTO) {
        courseService.updateCourseParticipant(userCourseDTO);
    }

    @PreAuthorize("@dynamicAccessAspect.hasAnyRole('ADMIN', 'TEACHER')")
    @GetMapping("/participant/list/{courseId}")
    @Operation(summary = "Lấy danh sách Giảng viên/ Sinh viên trong lớp học")
    public List<ICourseParticipantDTO> getListCourseParticipant(@PathVariable(name = "courseId") Long courseId,
            @RequestParam(name = "roleType") UserCourseRoleTypeEnum roleType) {
        return courseService.getListCourseParticipant(courseId, roleType);
    }

    @PreAuthorize("@dynamicAccessAspect.hasAnyRole('ADMIN', 'TEACHER')")
    @GetMapping("/participant/export/{courseId}")
    @Operation(summary = "Export danh sách SV / GV lớp học")
    public ResponseEntity<InputStreamResource> exportCourseParticipant(@PathVariable(name = "courseId") Long courseId,
            @RequestParam(name = "roleType") UserCourseRoleTypeEnum roleType) throws IOException {
        CustomInputStreamResource resourceRes = courseService.exportCourseParticipant(courseId, roleType);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE,
                MediaType.parseMediaType(String.join(";", Arrays.asList(Excel.CONTENT_TYPES))).toString());
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + resourceRes.getFileName());
        return ResponseEntity.ok().headers(headers).body(resourceRes.getResource());
    }

    @PreAuthorize("@dynamicAccessAspect.hasAnyPermission('P_COURSE_UPDATE')")
    @PostMapping(value = "/participant/student/import/{courseId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Import thí sinh vào lớp học")
    public Set<Long> importStudentCourse(@PathVariable(name = "courseId") Long courseId,
            @RequestParam(name = "file") MultipartFile importFile) throws IOException {
        return courseService.importStudentCourse(courseId, importFile);
    }

}
