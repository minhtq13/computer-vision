package com.elearning.elearning_support.services.course;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.elearning.elearning_support.dtos.CustomInputStreamResource;
import com.elearning.elearning_support.dtos.course.CourseSaveReqDTO;
import com.elearning.elearning_support.dtos.course.ICommonCourseDTO;
import com.elearning.elearning_support.dtos.course.ICourseDetailDTO;
import com.elearning.elearning_support.dtos.course.ICourseParticipantDTO;
import com.elearning.elearning_support.dtos.course.UserCourseDTO;
import com.elearning.elearning_support.enums.course.UserCourseRoleTypeEnum;

@Service
public interface CourseService {

    /**
     * Tạo lớp học
     */
    Long createCourse(CourseSaveReqDTO createDTO);


    /**
     * Cập nhật lớp học
     */
    void updateCourse(Long id, CourseSaveReqDTO updateDTO);


    /**
     * Danh sách lớp học dạng page
     */
    Page<ICommonCourseDTO> getPageCourse(String code, Long semesterId, Long subjectId, Pageable pageable);

    /**
     * Danh sách lớp học trong kỳ thi/ kỳ học
     */
    CustomInputStreamResource exportListCourse(String code, Long semesterId, Long subjectId) throws IOException;

    /**
     * Lấy chi tiết lớp học
     */
    ICourseDetailDTO getCourseDetails(Long id);

    /**
     * Thêm/Xóa Giám thị / Thí sinh trong lớp học
     */
    void updateCourseParticipant(UserCourseDTO userCourseDTO);

    /**
     * Lấy danh sách Giảng viên / thí sinh trong lớp học
     */
    List<ICourseParticipantDTO> getListCourseParticipant(Long courseId, UserCourseRoleTypeEnum roleType);

    /**
     * Export danh sách SV / GV trong lớp học
     */
    CustomInputStreamResource exportCourseParticipant(Long courseId, UserCourseRoleTypeEnum roleType) throws IOException;

    /**
     * Import danh sách SV vào lớp học
     */
    Set<Long> importStudentCourse(Long courseId, MultipartFile fileImport) throws IOException;

}
