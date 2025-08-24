package com.elearning.elearning_support.dtos.subject;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.BeanUtils;
import com.elearning.elearning_support.dtos.chapter.ChapterBaseResDTO;
import com.elearning.elearning_support.utils.object.ObjectMapperUtils;
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
public class SubjectDetailDTO {

    @Schema(description = "Id môn học")
    Long id;

    @Schema(description = "Tiêu đề môn học")
    String title;

    @Schema(description = "Mã môn học")
    String code;

    @Schema(description = "Mô tả môn học")
    String description;

    @Schema(description = "Số tín chỉ của môn học")
    Integer credit;

    @Schema(description = "Id Khoa/ Viện / Đơn vị phụ trách")
    Long departmentId;

    @Schema(description = "Tên Khoa/ Viện / Đơn vị phụ trách")
    String departmentName;

    @Schema(description = "Danh sách chương của học phần")
    List<ChapterBaseResDTO> chapters = new ArrayList<>();

    public SubjectDetailDTO(ISubjectDetailDTO iSubjectDetailDTO){
        BeanUtils.copyProperties(iSubjectDetailDTO, this);
        this.chapters = ObjectMapperUtils.listMapper(iSubjectDetailDTO.getLstChapterJson(), ChapterBaseResDTO.class);
    }

}
