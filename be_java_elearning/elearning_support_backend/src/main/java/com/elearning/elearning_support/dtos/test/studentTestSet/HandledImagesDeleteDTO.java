package com.elearning.elearning_support.dtos.test.studentTestSet;

import java.util.HashSet;
import java.util.Set;
import javax.validation.constraints.NotNull;
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
public class HandledImagesDeleteDTO {

    @NotNull
    @Schema(description = "Mã lớp (mã folder)")
    String examClassCode;

    @Schema(description = "Tên các file ảnh bị xóa")
    Set<String> lstFileName = new HashSet<>();

}
