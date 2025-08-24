package com.elearning.elearning_support.repositories.postgres.chapter;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.elearning.elearning_support.constants.sql.SQLChapter;
import com.elearning.elearning_support.dtos.chapter.ISubjectChapterDTO;
import com.elearning.elearning_support.entities.chapter.Chapter;

@Repository
public interface ChapterRepository extends JpaRepository<Chapter, Long> {

    Optional<Chapter> findByIdAndIsEnabled(Long chapterId, Boolean isEnabled);

    @Query(nativeQuery = true, value = SQLChapter.EXISTS_BY_SUBJECT_ID_AND_ORDERS_AND_ID_NOT)
    Boolean existsOtherByOrderInSubject(Long subjectId, Integer orders, Long chapterId);

    @Query(nativeQuery = true, value = SQLChapter.GET_LIST_EXISTED_CHAPTER_ORDERS_IN_SUBJECT)
    Set<Integer> getListExistedChapterOrderInSubject(Long subjectId);

    @Query(nativeQuery = true, value = SQLChapter.GET_ALL_SUBJECT_CHAPTER_MAPPINGS)
    List<ISubjectChapterDTO> getAllSubjectChapterMappings();

}
