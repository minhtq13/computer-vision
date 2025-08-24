package com.elearning.elearning_support.repositories.postgres.studentTestSet;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import com.elearning.elearning_support.entities.studentTest.StudentTestSetDetail;

@Repository
public interface StudentTestSetDetailRepository extends JpaRepository<StudentTestSetDetail, Long> {

    @Modifying
    void deleteAllByStudentTestSetId(Long studentTestSetId);

}
