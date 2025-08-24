package com.elearning.elearning_support.services.tag;

import org.springframework.stereotype.Service;
import com.elearning.elearning_support.dtos.common.CommonIdNameDTO;
import com.elearning.elearning_support.dtos.tag.TagCreateDTO;

@Service
public interface TagService {

    CommonIdNameDTO createTag(TagCreateDTO createDTO);

}
