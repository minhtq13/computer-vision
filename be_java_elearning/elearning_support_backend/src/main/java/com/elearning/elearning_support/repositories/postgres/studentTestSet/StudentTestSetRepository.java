package com.elearning.elearning_support.repositories.postgres.studentTestSet;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.elearning.elearning_support.annotations.dataSource.DataSource;
import com.elearning.elearning_support.constants.sql.SQLStudentTestSet;
import com.elearning.elearning_support.dtos.test.studentTestSet.IStudentTestSetDTO;
import com.elearning.elearning_support.dtos.test.studentTestSet.IStudentTestSetResultDTO;
import com.elearning.elearning_support.entities.studentTest.StudentTestSet;
import com.elearning.elearning_support.enums.dataSource.DataSourceRouteEnum;

@Repository
public interface StudentTestSetRepository extends JpaRepository<StudentTestSet, Long> {

    @Query(nativeQuery = true, value = SQLStudentTestSet.GET_LIST_STUDENT_TEST_SET_RESULT)
    List<IStudentTestSetResultDTO> getStudentTestSetResult(Long examClassId);

    @Query(nativeQuery = true, value = SQLStudentTestSet.EXISTS_NOT_PUBLISHED_IN_EXAM_CLASS)
    Boolean existedNotPublishedInExamClass(Long examClassId);

    List<StudentTestSet> findAllByStudentIdInAndExamClassIdIn(Set<Long> studentIds, Set<Long> examClassIds);

    List<StudentTestSet> findAllByStudentId(Long studentId);

    Optional<StudentTestSet> findByIdAndStatusAndIsEnabled(Long id, Integer status, Boolean isEnabled);

    Optional<StudentTestSet> findByIdAndStatusInAndIsEnabled(Long id, Set<Integer> status, Boolean isEnabled);

    @Query(nativeQuery = true, value = SQLStudentTestSet.GET_TEMP_SUBMISSION_BY_ID)
    String getTempSubmissionById(Long id);

    @Modifying
    @Query(nativeQuery = true, value = SQLStudentTestSet.DELETE_BY_EXAM_CLASS_ID_AND_STATUS)
    void deleteAllByExamClassIdAndStatus(Long examClassId, Integer status);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = SQLStudentTestSet.UPDATE_IS_PUBLISHED_STATUS)
    void updatePublishedState(Long examClassId, Boolean isPublished);

    Boolean existsByExamClassIdAndStatusNot(Long examClassId, Integer status);

    @Query(nativeQuery = true, value = SQLStudentTestSet.GET_LIST_ONLINE_STUDENT_TEST_SET)
    Page<IStudentTestSetDTO> getListOnlineStudentTestSet(Long currentUserId, String keyword, Long subjectId, Long semesterId, Integer status, Integer testType, Pageable pageable);


    @Query(nativeQuery = true, value = SQLStudentTestSet.GET_LIST_CLOSED_STUDENT_TEST_SET)
    Page<IStudentTestSetDTO> getListClosedStudentTestSet(Long currentUserId, String keyword, Pageable pageable);

    @Query(nativeQuery = true, value = SQLStudentTestSet.EXISTS_BY_TEST_ID)
    Boolean existsByTestId(Long testId);

    Boolean existsByTestSetId(Long testSetId);

    Boolean existsByTestSetIdAndStatusNot(Long testSetId, Integer status);

    @Query(nativeQuery = true, value = SQLStudentTestSet.GET_STUDENT_TEST_SET_RESULT_BY_ID)
    Double getStudentTestSetResultById(Long id);

    @Modifying
    @Query(nativeQuery = true, value = SQLStudentTestSet.UPDATE_DUE_ONLINE_TEST_BY_STUDENT_ID)
    void updateDueStatusByStudentId(Long studentId, Date updatedTime);

    @Modifying
    @Query(nativeQuery = true, value = SQLStudentTestSet.UPDATE_DUE_ALL_ONLINE_TEST)
    void updateAllStudentTestSetDueStatus(Date updatedTime);

    Boolean existsByExamClassIdAndStatusIn(Long examClassId, List<Integer> lstStatus);
}
