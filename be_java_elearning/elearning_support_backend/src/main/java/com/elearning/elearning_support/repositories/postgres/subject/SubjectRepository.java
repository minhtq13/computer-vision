package com.elearning.elearning_support.repositories.postgres.subject;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.elearning.elearning_support.constants.sql.SQLSubject;
import com.elearning.elearning_support.dtos.common.ICommonIdCode;
import com.elearning.elearning_support.dtos.subject.ISubjectDetailDTO;
import com.elearning.elearning_support.dtos.subject.ISubjectListDTO;
import com.elearning.elearning_support.entities.subject.Subject;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {

    Optional<Subject> findByIdAndIsEnabled(Long id, Boolean isEnabled);

    Boolean existsByCode(String code);

    Boolean existsByCodeAndIdNot(String code, Long id);

    Boolean existsByIdAndIsEnabled(Long id, Boolean isEnabled);

    @Query(nativeQuery = true, value = SQLSubject.GET_LIST_SUBJECT)
    Page<ISubjectListDTO> getListSubject(Set<Long> viewableSubjectIds, String search, Long departmentId,
            String departmentName,
            Pageable pageable);

    @Query(nativeQuery = true, value = SQLSubject.GET_DETAIL_SUBJECT)
    ISubjectDetailDTO getDetailSubject(Long subjectId);

    @Query(nativeQuery = true, value = SQLSubject.GET_ALL_SUBJECT_ID_CODE)
    List<ICommonIdCode> getAllSubjectIdCode();

    @Query(nativeQuery = true, value = SQLSubject.GET_LIST_VIEWABLE_SUBJECT_ID)
    Set<Long> getListViewableSubjectId(Boolean isAdmin, Long currentUserId);

    @Query(nativeQuery = true, value = SQLSubject.GET_LIST_VIEWABLE_SUBJECT_ID_BY_EXAM_CLASS)
    Set<Long> getListViewableSubjectIdByExamClass(Boolean isAdmin, Long currentUserId);

    @Query(nativeQuery = true, value = SQLSubject.GET_LIST_SUBJECT_ID_BY_DEPARTMENT_ID_IN)
    Set<Long> getListSubjectIdByDepartmentIdIn(Set<Long> departmentIds);
}
