package com.elearning.elearning_support.repositories.postgres.semester;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.elearning.elearning_support.entities.semester.Semester;

@Repository
public interface SemesterRepository extends JpaRepository<Semester, Long> {

    Boolean existsByCode(String code);

}
