package com.elearning.elearning_support.models.graphql.paging;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(Include.NON_NULL)
public class GraphQLCustomPage<T> {

    List<T> content;
    Pageable pageable;
    Boolean empty;
    Boolean first;
    Boolean last;
    Integer number;
    Integer numberOfElements;
    Integer totalPages;
    Long totalElements;

    /**
     * Page constructor
     */
    public GraphQLCustomPage(Page<T> page) {
        BeanUtils.copyProperties(page, this);
    }

    /**
     * Slice constructor
     */
    public GraphQLCustomPage(Slice<T> slice) {
        BeanUtils.copyProperties(slice, this);
    }

}
