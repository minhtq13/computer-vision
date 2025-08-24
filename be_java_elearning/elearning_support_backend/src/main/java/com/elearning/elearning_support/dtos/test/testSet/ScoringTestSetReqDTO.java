package com.elearning.elearning_support.dtos.test.testSet;

import java.util.List;
import com.elearning.elearning_support.dtos.studentTestSet.StudentHandledTestDTO;
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
public class ScoringTestSetReqDTO {


    @Schema(description = "Mã lớp thi")
    String classCode;

    @Schema(description = "Dữ liệu xử lý chấm thi")
    List<StudentHandledTestDTO> handledTestSets;

}
