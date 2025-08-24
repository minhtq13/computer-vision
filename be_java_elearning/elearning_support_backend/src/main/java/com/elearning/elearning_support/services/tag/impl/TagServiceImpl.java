package com.elearning.elearning_support.services.tag.impl;

import java.util.Date;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.elearning.elearning_support.dtos.common.CommonIdNameDTO;
import com.elearning.elearning_support.dtos.tag.TagCreateDTO;
import com.elearning.elearning_support.entities.tag.TagEntity;
import com.elearning.elearning_support.enums.commons.DeletedFlag;
import com.elearning.elearning_support.repositories.postgres.tag.TagRepository;
import com.elearning.elearning_support.services.tag.TagService;
import com.elearning.elearning_support.utils.auth.AuthUtils;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TagServiceImpl implements TagService {

    @Autowired
    private TagRepository tagRepository;

    @Override
    public CommonIdNameDTO createTag(TagCreateDTO createDTO) {
        TagEntity newTag = TagEntity.builder()
            .name(createDTO.getName())
            .objectAppliedTypes(createDTO.getObjectAppliedTypes().stream().map(Enum::name).collect(Collectors.toSet()))
            .createdBy(AuthUtils.getCurrentUserId())
            .createdAt(new Date())
            .isEnabled(Boolean.TRUE)
            .deletedFlag(DeletedFlag.NOT_YET_DELETED.getValue())
            .build();
        newTag = tagRepository.save(newTag);
        return new CommonIdNameDTO(newTag.getId(), newTag.getName());
    }
}
