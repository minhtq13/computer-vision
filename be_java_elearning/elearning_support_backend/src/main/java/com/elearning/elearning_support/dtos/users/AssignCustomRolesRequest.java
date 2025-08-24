package com.elearning.elearning_support.dtos.users;

import java.util.List;
import javax.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class AssignCustomRolesRequest {

    @Schema(description = "Danh sách ID của vai trò cần gán", example = "1")
    private List<Long> roleIds;

    @NotNull
    @Schema(description = "Danh sách ID của người dùng cần được gán vai trò")
    private List<Long> userIds;
}
