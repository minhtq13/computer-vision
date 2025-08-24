package com.elearning.elearning_support.dtos.test;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;
import org.springframework.beans.BeanUtils;
import com.elearning.elearning_support.utils.DateUtils;
import com.elearning.elearning_support.utils.auth.AuthUtils;
import com.elearning.elearning_support.utils.object.ObjectMapperUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
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
public class TestDetailDTO {

        @Schema(description = "Id kỳ thi")
        Long id;

        @Schema(description = "Tên kỳ thi")
        String name;

        @Schema(description = "Id kỳ học")
        Long semesterId;

        @Schema(description = "Kỳ học")
        String semester;

        @Schema(description = "Id môn thi")
        Long subjectId;

        @Schema(description = "Tên học phần")
        String subjectName;

        @Schema(description = "Mã học phần")
        String subjectCode;

        @Schema(description = "Số lượng câu hỏi trong đề thi (cấu hình ban đầu)")
        Integer questionQuantity;

        @JsonFormat(pattern = DateUtils.FORMAT_DATE_DD_MM_YYYY_HH_MM_SS, timezone = DateUtils.TIME_ZONE)
        Date createdAt;

        @Schema(description = "Id người tạo")
        Long createdBy;

        @JsonFormat(pattern = DateUtils.FORMAT_DATE_DD_MM_YYYY_HH_MM_SS, timezone = DateUtils.TIME_ZONE)
        Date modifiedAt;

        @JsonFormat(pattern = DateUtils.FORMAT_DATE_DD_MM_YYYY_HH_MM, timezone = DateUtils.TIME_ZONE)
        Date startTime;

        @JsonFormat(pattern = DateUtils.FORMAT_DATE_DD_MM_YYYY_HH_MM, timezone = DateUtils.TIME_ZONE)
        Date endTime;

        @Schema(description = "Điểm tổng")
        BigDecimal totalPoint;

        @Schema(description = "Thời giam làm bài thi")
        Integer duration;

        @Schema(description = "Danh sách các mã đề thi trong bộ đề của kỳ thi")
        String lstTestSetCode;

        @Schema(description = "Cấu hình gen đề thi (tự động)")
        GenTestConfigDTO generateConfig;

        @Schema(description = "Hình thức thi")
        String testType;

        Boolean hasDeletePermission;

        Boolean hasEditPermission;

        public TestDetailDTO(ITestListDTO iTestListDTO) {
                BeanUtils.copyProperties(iTestListDTO, this);
                this.generateConfig = ObjectMapperUtils.objectMapper(iTestListDTO.getGenTestConfig(),
                                GenTestConfigDTO.class);
                boolean hasDeleteOrEditPermission = AuthUtils.isAdmin() || AuthUtils.isDepartmentAdmin()
                                ? Objects.equals(this.createdBy, AuthUtils.getCurrentUserId())
                                : Boolean.FALSE;
                this.hasEditPermission = hasDeleteOrEditPermission;
                this.hasDeletePermission = hasDeleteOrEditPermission;
        }

}
