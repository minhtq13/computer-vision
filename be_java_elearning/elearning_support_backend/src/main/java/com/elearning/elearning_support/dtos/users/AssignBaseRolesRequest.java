package com.elearning.elearning_support.dtos.users;

import java.util.List;
import javax.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class AssignBaseRolesRequest {

    @Schema(description = "ID của vai trò cơ bản cần gán", example = "1")
    private Long roleId;

    @NotNull
    @Schema(description = "Danh sách ID của người dùng cần được gán vai trò")
    private List<Long> userIds;
}
