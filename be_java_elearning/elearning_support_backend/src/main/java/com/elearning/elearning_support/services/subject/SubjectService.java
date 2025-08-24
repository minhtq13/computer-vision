package com.elearning.elearning_support.services.subject;

import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.elearning.elearning_support.dtos.subject.ISubjectListDTO;
import com.elearning.elearning_support.dtos.subject.SubjectDetailDTO;
import com.elearning.elearning_support.dtos.subject.SubjectSaveReqDTO;
import com.elearning.elearning_support.entities.subject.Subject;

@Service
public interface SubjectService {

    Subject findById(Long subjectId);

    /**
     * Tạo môn học
     */
    void createSubject(SubjectSaveReqDTO createDTO);


    /**
     * Cập nhật môn học
     */
    void updateSubject(Long subjectId, SubjectSaveReqDTO updateDTO);


    /**
     * Danh sách môn học
     */
    Page<ISubjectListDTO> getListSubject(String search, Long departmentId, String departmentName, Pageable pageable);


    /**
     * Chi tiết môn học
     */
    SubjectDetailDTO getSubjectDetail(Long subjectId);

    Set<Long> getListViewableSubjectIds();
}
