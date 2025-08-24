package com.elearning.elearning_support.services.examClass;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.elearning.elearning_support.dtos.CustomInputStreamResource;
import com.elearning.elearning_support.dtos.examClass.ExamClassCreateDTO;
import com.elearning.elearning_support.dtos.examClass.ExamClassSaveReqDTO;
import com.elearning.elearning_support.dtos.examClass.ICommonExamClassDTO;
import com.elearning.elearning_support.dtos.examClass.IExamClassDetailDTO;
import com.elearning.elearning_support.dtos.examClass.IExamClassParticipantDTO;
import com.elearning.elearning_support.dtos.examClass.UserExamClassDTO;
import com.elearning.elearning_support.dtos.mail.MailBaseReqDTO;
import com.elearning.elearning_support.enums.examClass.UserExamClassRoleEnum;

@Service
public interface ExamClassService {

    /**
     * Tạo lớp thi
     */
    Long createExamClass(ExamClassCreateDTO createDTO);


    /**
     * Cập nhật lớp thi
     */
    void updateExamClass(Long id, ExamClassSaveReqDTO updateDTO);


    /**
     * Danh sách lớp thi dạng page
     */
    Page<ICommonExamClassDTO> getPageExamClass(String code, Long semesterId, Long subjectId, Long testId, Pageable pageable);

    /**
     * Danh sách lớp thi trong kỳ thi/ kỳ học
     */
    CustomInputStreamResource exportListExamClass(Long semesterId, Long testId, Long subjectId) throws IOException;

    /**
     * Lấy chi tiết lớp thi
     */
    IExamClassDetailDTO getExamClassDetail(Long id);

    /**
     * Thêm/Xóa Giám thị / Thí sinh trong lớp thi
     */
    void updateParticipantToExamClass(UserExamClassDTO userExamClassDTO);

    /**
     * Lấy danh sách Giám thị / thí sinh trong lớp thi
     */
    List<IExamClassParticipantDTO> getListExamClassParticipant(Long examClassId, UserExamClassRoleEnum roleType);

    /**
     * Export danh sách SV / GV trong lớp thi
     */
    CustomInputStreamResource exportExamClassParticipant(Long examClassId, UserExamClassRoleEnum roleType) throws IOException;

    /**
     * Import danh sách SV vào lớp thi
     */
    Set<Long> importStudentExamClass(Long examClassId, MultipartFile fileImport) throws IOException;


    /**
     * Assign đề thi cho thí sinh trong lớp thi (lớp thi online)
     */
    void assignOnlineTestSet(Long examClassId);


    /**
     * Published đề thi trong lớp thi (online)
     */
    void publishOnlineTestSet(Long examClassId, Boolean isPublished);

    /**
     * Gửi email danh sách và kết quả của lớp thi
     */
    void sendEmailExamClassResult(Long examClassId, MailBaseReqDTO mailReqDTO) throws IOException;


}
