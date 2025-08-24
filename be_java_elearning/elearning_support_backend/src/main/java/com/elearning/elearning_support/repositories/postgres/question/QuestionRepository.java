package com.elearning.elearning_support.repositories.postgres.question;

import java.util.List;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.elearning.elearning_support.constants.sql.SQLQuestion;
import com.elearning.elearning_support.dtos.question.IListQuestionDTO;
import com.elearning.elearning_support.dtos.question.IQuestionAnswerDTO;
import com.elearning.elearning_support.dtos.question.IQuestionDetailsDTO;
import com.elearning.elearning_support.entities.question.Question;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

    Boolean existsByCode(String code);

    @Query(nativeQuery = true, value = SQLQuestion.GET_LIST_QUESTION)
    Page<IListQuestionDTO> getPageQuestion(Set<Long> viewableSubjectIds, Long subjectId, String subjectCode, Set<Long> chapterIds, String chapterCode, Integer questionLevel,
        Long tagId, String search, Pageable pageable);

    @Query(nativeQuery = true, value = SQLQuestion.GET_LIST_QUESTION_IDS)
    Set<Long> getListQuestionIds(Set<Long> viewableSubjectIds, Long subjectId, String subjectCode, Set<Long> chapterIds, String chapterCode, Integer questionLevel,
        String search);

    @Query(nativeQuery = true, value = SQLQuestion.COUNT_LIST_QUESTION_IDS)
    Integer countListQuestion(Set<Long> viewableSubjectIds, Long subjectId, String subjectCode, Set<Long> chapterIds, String chapterCode, Integer questionLevel,
        Long tagId, String search);

    @Query(nativeQuery = true, value = SQLQuestion.GET_LIST_QUESTION_IDS_WITH_LIMIT)
    Set<Long> getListQuestionIdsWithLimit(Set<Long> viewableSubjectIds, Long subjectId, String subjectCode, Set<Long> chapterIds, String chapterCode, Integer questionLevel,
        Long tagId, String search, Integer limitSize);

    @Query(nativeQuery = true, value = SQLQuestion.GET_LIST_QUESTION_BY_IDS_IN)
    List<IListQuestionDTO> getListQuestion(Set<Long> questionIds);

    @Query(nativeQuery = true, value = SQLQuestion.GET_LIST_QUESTION_ALLOWED_USING_IN_TEST)
    List<IListQuestionDTO> getListQuestionAllowedUsingInTest(Long testId, String search, Integer level);

    @Query(nativeQuery = true, value = SQLQuestion.GET_LIST_QUESTION_ALLOWED_USING_IN_TEST)
    Page<IListQuestionDTO> getPageQuestionAllowedUsingInTest(Long testId, String search, Integer level, Pageable pageable);

    @Query(nativeQuery = true, value = SQLQuestion.GET_LIST_QUESTION_ID_BY_CHAPTER_ID_IN)
    Set<Long> getListQuestionIdByChapterIn(Set<Long> lstChapterId);

    @Query(nativeQuery = true, value = SQLQuestion.GET_LIST_QUESTION_ID_IN_TEST)
    Set<IQuestionAnswerDTO> getListQuestionIdInTest(Long testId);

    @Query(nativeQuery = true, value = SQLQuestion.GET_LIST_QUESTION_IN_TEST)
    List<IListQuestionDTO> getListQuestionInTest(Long testId, Long tagId, String search, Integer questionLevel);

    @Query(nativeQuery = true, value = SQLQuestion.GET_LIST_QUESTION_IN_TEST)
    Page<IListQuestionDTO> getPageQuestionInTest(Long testId, Long tagId, String search, Integer questionLevel, Pageable pageable);

    @Query(nativeQuery = true, value = SQLQuestion.GET_QUESTION_DETAILS)
    IQuestionDetailsDTO getQuestionDetails(Long questionId);

    @Query(nativeQuery = true, value = SQLQuestion.EXISTED_IN_USED_TEST_SET)
    Boolean existsInUsedTestSet(Long questionId);

}
