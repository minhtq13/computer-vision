package com.elearning.elearning_support.controllers.graphql.subject;

import java.util.Collections;
import org.springframework.data.domain.Pageable;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import com.elearning.elearning_support.components.BaseController;
import com.elearning.elearning_support.dtos.subject.ISubjectListDTO;
import com.elearning.elearning_support.dtos.subject.SubjectDetailDTO;
import com.elearning.elearning_support.models.graphql.paging.GraphQLCustomPage;
import com.elearning.elearning_support.services.subject.SubjectService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class SubjectGraphQLController {

    private final SubjectService subjectService;

    @QueryMapping(name = "subjectList")
    @Operation(summary = "Danh sách học phần")

    public GraphQLCustomPage<ISubjectListDTO> getListSubject(@Argument String search, @Argument Long departmentId,
            @Argument String departmentName,
            @Argument Integer page, @Argument Integer size, @Argument String sort) {
        Pageable pageable = BaseController.getPageable(page, size, Collections.singletonList(sort));
        return new GraphQLCustomPage<>(subjectService.getListSubject(search, departmentId, departmentName, pageable));
    }

    @QueryMapping(name = "subjectDetail")
    @Operation(summary = "Chi tiết học phần")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public SubjectDetailDTO getSubjectDetail(@Argument @NonNull Long subjectId) {
        return subjectService.getSubjectDetail(subjectId);
    }
}
