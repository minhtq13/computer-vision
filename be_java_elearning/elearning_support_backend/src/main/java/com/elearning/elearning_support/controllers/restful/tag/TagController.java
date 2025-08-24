package com.elearning.elearning_support.controllers.restful.tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.elearning.elearning_support.dtos.common.CommonIdNameDTO;
import com.elearning.elearning_support.dtos.tag.TagCreateDTO;
import com.elearning.elearning_support.services.tag.TagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/tag")
@Tag(name = "API thẻ (tag)")
public class TagController {

    @Autowired
    private TagService tagService;

    @PreAuthorize("@dynamicAccessAspect.hasAnyRole('ADMIN', 'TEACHER')")
    @PostMapping
    @Operation(description = "Tạo tag")
    public CommonIdNameDTO createTag(@Validated @RequestBody TagCreateDTO createDTO) {
        return tagService.createTag(createDTO);
    }

}
