package com.elearning.elearning_support.repositories.postgres.teacherSubject;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.elearning.elearning_support.constants.sql.SQLUser;
import com.elearning.elearning_support.entities.teacherSubject.TeacherSubject;

@Repository
public interface TeacherSubjectRepository extends JpaRepository<TeacherSubject, Long> {

    @Modifying
    @Query(nativeQuery = true, value = SQLUser.DELETE_ALL_TEACHER_SUBJECT_BY_TEACHER_ID)
    void deleteAllByTeacherId(Long teacherId);

}
