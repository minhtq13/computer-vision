package com.elearning.elearning_support.repositories.postgres.test;

import java.util.Date;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.elearning.elearning_support.constants.sql.SQLTest;
import com.elearning.elearning_support.dtos.test.ITestListDTO;
import com.elearning.elearning_support.entities.test.Test;

@Repository
public interface TestRepository extends JpaRepository<Test, Long> {

    Optional<Test> findByIdAndIsEnabled(Long id, Boolean isEnabled);

    Optional<Test> findByIdAndTestTypeAndIsEnabled(Long id, Integer testType, Boolean isEnabled);

    Boolean existsByIdAndIsEnabled(Long id, Boolean isEnabled);

    Boolean existsByIdAndCreatedBy(Long id, Long createdBy);


    @Query(nativeQuery = true, value = SQLTest.GET_LIST_TEST)
    Page<ITestListDTO> getListTest(Set<Long> viewableSubjectIds, Long subjectId, String subjectCode, Date startTime, Date endTime, Long semesterId, String semesterCode,
        Integer testType, Pageable pageable);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = SQLTest.SWITCH_TEST_STATUS)
    void switchTestStatus(Long testId, Boolean isEnabled);

    @Query(nativeQuery = true, value = SQLTest.GET_TEST_DETAILS)
    ITestListDTO getTestDetails(Long testId);

}
