package com.elearning.elearning_support.repositories.postgres.examClass;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.elearning.elearning_support.constants.sql.SQLExamClass;
import com.elearning.elearning_support.entities.examClass.UserExamClass;

@Repository
public interface UserExamClassRepository extends JpaRepository<UserExamClass, Long> {

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = SQLExamClass.DELETE_EXAM_CLASS_PARTICIPANT_BY_ID)
    void deleteAllByExamClassId(Long examClassId);

    Boolean existsByExamClassIdAndUserIdAndRoleType(Long examClassId, Long userId, Integer roleType);

    @Modifying
    void deleteAllByUserId(Long userId);

}
