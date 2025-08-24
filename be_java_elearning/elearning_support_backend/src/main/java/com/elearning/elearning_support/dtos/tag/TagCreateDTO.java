package com.elearning.elearning_support.dtos.tag;

import java.util.Set;
import com.elearning.elearning_support.enums.tag.TagObjectAppliedTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TagCreateDTO {

    @Schema(description = "Tên tag")
    String name;

    @Schema(description = "Các loại đối tượng có thể gắn với tag")
    Set<TagObjectAppliedTypeEnum> objectAppliedTypes;

}
