package com.elearning.elearning_support.repositories.postgres.course;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import com.elearning.elearning_support.entities.course.UserCourse;

@Repository
public interface UserCourseRepository extends JpaRepository<UserCourse, Long> {

    @Modifying
    void deleteAllByCourseId(Long courseId);

    List<UserCourse> findAllByCourseIdAndRoleType(Long courseId, Integer roleType);
}
