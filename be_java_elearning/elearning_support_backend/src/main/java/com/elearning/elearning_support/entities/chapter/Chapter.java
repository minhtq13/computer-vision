package com.elearning.elearning_support.entities.chapter;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.springframework.beans.BeanUtils;
import com.elearning.elearning_support.dtos.chapter.ChapterSaveReqDTO;
import com.elearning.elearning_support.entities.BaseEntity;
import com.elearning.elearning_support.utils.auth.AuthUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "chapter", schema = "elearning_support_dev")
public class Chapter extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Size(max = 20)
    @NotNull
    @Column(name = "code", nullable = false, length = 20)
    private String code;

    @Column(name = "is_enabled")
    private Boolean isEnabled;

    @Column(name = "orders")
    private Integer orders;

    @Size(max = 255)
    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @NotNull
    @Column(name = "subject_id", nullable = false)
    private Long subjectId;

    public Chapter(Long subjectId, ChapterSaveReqDTO chapterReqDTO) {
        BeanUtils.copyProperties(chapterReqDTO, this);
        this.subjectId = subjectId;
        this.isEnabled = Boolean.TRUE;
        this.setCreatedAt(new Date());
        this.setCreatedBy(AuthUtils.getCurrentUserId());
        this.code = String.format("C%05d%d", subjectId, chapterReqDTO.getOrders());
    }

}