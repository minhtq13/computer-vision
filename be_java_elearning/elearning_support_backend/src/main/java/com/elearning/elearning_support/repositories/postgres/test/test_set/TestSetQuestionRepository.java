package com.elearning.elearning_support.repositories.postgres.test.test_set;

import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.elearning.elearning_support.constants.sql.SQLTestSet;
import com.elearning.elearning_support.entities.test.TestSetQuestion;

@Repository
public interface TestSetQuestionRepository extends JpaRepository<TestSetQuestion, Long> {

    @Transactional
    @Modifying
    void deleteAllByTestSetId(Long testSetId);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = SQLTestSet.DELETE_TEST_SET_QUESTION_BY_TEST_SET_IDS_IN)
    void deleteAllByTestSetIdIn(Set<Long> testSetIds);

    @Transactional
    @Modifying
    void deleteAllByQuestionId(Long questionId);

    Boolean existsByQuestionId(Long questionId);

}
