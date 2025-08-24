package com.elearning.elearning_support.repositories.postgres.tag;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.elearning.elearning_support.constants.sql.SQLTag;
import com.elearning.elearning_support.dtos.common.ICommonIdName;
import com.elearning.elearning_support.entities.tag.TagEntity;

@Repository
public interface TagRepository extends JpaRepository<TagEntity, Long> {

    @Query(nativeQuery = true, value = SQLTag.GET_LIST_TAG_BY_OBJECT_APPLIED_TYPE)
    List<ICommonIdName> getListTagByObjectAppliedType(String filter, String objectAppliedType);

}
