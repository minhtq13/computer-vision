package com.elearning.elearning_support.repositories.postgres.course;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.elearning.elearning_support.constants.sql.SQLCourse;
import com.elearning.elearning_support.dtos.course.ICommonCourseDTO;
import com.elearning.elearning_support.dtos.course.ICourseDetailDTO;
import com.elearning.elearning_support.dtos.course.ICourseParticipantDTO;
import com.elearning.elearning_support.entities.course.Course;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    Boolean existsByCode(String code);

    Optional<Course> findByIdAndIsEnabled(Long id, Boolean isEnabled);

    Optional<Course> findByCodeAndIsEnabled(String code, Boolean isEnabled);

    @Query(nativeQuery = true, value = SQLCourse.GET_LIST_COURSE)
    Page<ICommonCourseDTO> getPageCourse(Long currentUserId, String code, Long subjectId, Long semesterId, Pageable pageable);

    @Query(nativeQuery = true, value = SQLCourse.GET_LIST_COURSE)
    List<ICommonCourseDTO> getListCourse(Long currentUserId, String code, Long subjectId, Long semesterId);

    @Query(nativeQuery = true, value = SQLCourse.GET_COURSE_DETAILS)
    ICourseDetailDTO getCourseDetails(Long id);

    @Query(nativeQuery = true, value = SQLCourse.GET_LIST_COURSE_PARTICIPANT)
    List<ICourseParticipantDTO> getListCourseParticipants(Long courseId, Integer roleType);

    @Query(nativeQuery = true, value = SQLCourse.GET_LIST_COURSE_PARTICIPANT_ID)
    Set<Long> getListCourseParticipantId(Long course, Integer roleType);



}
