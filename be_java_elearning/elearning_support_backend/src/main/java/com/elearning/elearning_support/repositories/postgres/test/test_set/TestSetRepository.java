package com.elearning.elearning_support.repositories.postgres.test.test_set;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.elearning.elearning_support.constants.sql.SQLTest;
import com.elearning.elearning_support.constants.sql.SQLTestSet;
import com.elearning.elearning_support.dtos.test.testQuestion.ITestQuestionAnswerResDTO;
import com.elearning.elearning_support.dtos.test.testSet.ITestQuestionCorrectAnsDTO;
import com.elearning.elearning_support.dtos.test.testSet.ITestSetPreviewDTO;
import com.elearning.elearning_support.dtos.test.testSet.ITestSetResDTO;
import com.elearning.elearning_support.dtos.test.testSet.ITestSetScoringDTO;
import com.elearning.elearning_support.entities.test.TestSet;

@Repository
public interface TestSetRepository extends JpaRepository<TestSet, Long> {

    @Query(nativeQuery = true, value = SQLTest.GET_TEST_SET_DETAILS_BY_TEST_ID_AND_CODE)
    ITestSetResDTO getTestSetDetail(Long testId, String code);

    @Query(nativeQuery = true, value = SQLTest.GET_TEST_SET_DETAILS_BY_ID)
    ITestSetResDTO getTestSetDetail(Long testSetId);

    @Query(nativeQuery = true, value = SQLTest.GET_LIST_TEST_SET_QUESTION)
    List<ITestQuestionAnswerResDTO> getListTestSetQuestion(Long testSetId);

    Boolean existsByTestIdAndCode(Long testId, String code);

    @Query(value = "SELECT testSet.id FROM TestSet AS testSet WHERE testSet.testId = :testId")
    Set<Long> findByTestId(Long testId);

    Optional<TestSet> findByIdAndIsEnabled(Long id, Boolean isEnabled);

    @Query(nativeQuery = true, value = SQLTestSet.GET_LIST_TEST_QUESTION_CORRECT_ANSWER)
    Set<ITestQuestionCorrectAnsDTO> getListTestQuestionCorrectAns(Long testSetId);

    @Query(nativeQuery = true, value = SQLTestSet.GET_LIST_TEST_SET_GENERAL_SCORING_DATA)
    List<ITestSetScoringDTO> getTestSetGeneralScoringData(Set<String> examClassCodes, Set<String> testCodes);

    @Query(value = "SELECT testSet.id FROM TestSet testSet WHERE testSet.testId = :testId AND testSet.isEnabled = TRUE")
    Set<Long> getListTestSetIdByTestId(Long testId);

    @Query(nativeQuery = true, value = SQLTestSet.GET_MAX_CURRENT_TEST_NO_BY_TEST_ID)
    Integer getMaxCurrentTestNoByTestId(Long testId);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = SQLTestSet.DELETE_ALL_BY_TEST_ID)
    void deleteAllByTestId(Long testId);

    @Query(nativeQuery = true, value = SQLTestSet.GET_LIST_TEST_SET_PREVIEW_BY_TEST_ID)
    List<ITestSetPreviewDTO> getListTestSetPreviewByTestId(Long testId);

    Boolean existsByIdAndIsEnabled(Long testSetId, Boolean isEnabled);
}
