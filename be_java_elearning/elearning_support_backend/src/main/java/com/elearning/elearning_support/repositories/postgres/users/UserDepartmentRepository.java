package com.elearning.elearning_support.repositories.postgres.users;

import java.util.List;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import com.elearning.elearning_support.entities.users.UserDepartment;

@Repository
public interface UserDepartmentRepository extends JpaRepository<UserDepartment, Long> {

    List<UserDepartment> findAllByUserId(Long userId);

    @Modifying
    void deleteAllByUserIdAndDepartmentIdIn(Long userId, Set<Long> departmentIds);

}
