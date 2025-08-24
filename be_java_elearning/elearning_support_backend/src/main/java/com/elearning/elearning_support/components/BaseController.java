package com.elearning.elearning_support.components;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import com.elearning.elearning_support.constants.message.messageConst.MessageConst;
import com.elearning.elearning_support.exceptions.BadRequestException;

public class BaseController {

    private static final String STR_REGEXP_SORT = "^[A-z0-9]+,(asc|desc|ASC|DESC)$";

    public static Pageable getPageable(Integer page, Integer size, List<String> lstSort) {
        List<Sort.Order> lstSortOrder = new ArrayList<>();
        lstSort.forEach(sort -> {
            if (!sort.matches(STR_REGEXP_SORT)) {
                throw new BadRequestException(MessageConst.PATTERN_NOT_MATCH, "SORT_REGEX", "sort", sort);
            }
            String[] part = sort.split(",", 2);
            Sort.Order sortOrder = new Sort.Order(Sort.Direction.valueOf(part[1].toUpperCase()), part[0]);
            lstSortOrder.add(sortOrder);
        });
        Sort sortable = Sort.by(lstSortOrder);
        return PageRequest.of(page, size, sortable);
    }


}
