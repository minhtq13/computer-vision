package com.elearning.elearning_support.repositories.postgres.department;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.elearning.elearning_support.constants.sql.SQLDepartment;
import com.elearning.elearning_support.dtos.common.ICommonIdCodeName;
import com.elearning.elearning_support.entities.department.Department;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {

    Boolean existsByIdAndCodeNot(Long id, String code);

    @Query(nativeQuery = true, value = SQLDepartment.GET_LIST_DEPARTMENT)
    List<ICommonIdCodeName> getAllDepartments();

}
