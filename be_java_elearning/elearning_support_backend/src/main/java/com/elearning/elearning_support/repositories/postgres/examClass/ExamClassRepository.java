package com.elearning.elearning_support.repositories.postgres.examClass;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.elearning.elearning_support.constants.sql.SQLExamClass;
import com.elearning.elearning_support.dtos.examClass.ICommonExamClassDTO;
import com.elearning.elearning_support.dtos.examClass.IExamClassDetailDTO;
import com.elearning.elearning_support.dtos.examClass.IExamClassParticipantDTO;
import com.elearning.elearning_support.entities.examClass.ExamClass;

@Repository
public interface ExamClassRepository extends JpaRepository<ExamClass, Long> {

    Boolean existsByCode(String code);

    Optional<ExamClass> findByIdAndIsEnabled(Long id, Boolean isEnabled);

    Optional<ExamClass> findByIdAndTestTypeAndIsEnabled(Long id, Integer testType, Boolean isEnabled);

    Optional<ExamClass> findByCodeAndIsEnabled(String code, Boolean isEnabled);

    @Query(nativeQuery = true, value = SQLExamClass.GET_LIST_EXAM_CLASS)
    Page<ICommonExamClassDTO> getPageExamClass(Long currentUserId, String code, Long semesterId, Long subjectId, Long testId, Pageable pageable);

    @Query(nativeQuery = true, value = SQLExamClass.GET_LIST_EXAM_CLASS)
    List<ICommonExamClassDTO> getListExamClass(Long currentUserId, String code, Long semesterId, Long subjectId, Long testId);

    @Query(nativeQuery = true, value = SQLExamClass.GET_DETAILS_EXAM_CLASS)
    IExamClassDetailDTO getDetailExamClass(Long id);

    @Query(nativeQuery = true, value = SQLExamClass.GET_LIST_EXAM_CLASS_PARTICIPANT)
    List<IExamClassParticipantDTO> getListExamClassParticipant(Long examClassId, Set<Integer> roleTypes);

    @Query(nativeQuery = true, value = SQLExamClass.GET_LIST_EXAM_CLASS_PARTICIPANT_ID)
    Set<Long> getListExamClassParticipantId(Long examClassId, Integer roleType);

    @Query(nativeQuery = true, value = SQLExamClass.GET_LIST_EXAM_CLASS_NAME_BY_IDS_IN)
    List<String> getListExamClassNameByIdsIn(Set<Long> examClassIds);


    Boolean existsByTestId(Long testId);

}
