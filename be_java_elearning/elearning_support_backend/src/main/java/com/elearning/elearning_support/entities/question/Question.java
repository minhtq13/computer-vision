package com.elearning.elearning_support.entities.question;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import com.elearning.elearning_support.entities.BaseEntity;
import com.elearning.elearning_support.entities.answer.Answer;
import com.vladmihalcea.hibernate.type.array.LongArrayType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "question", schema = "elearning_support_dev")
@TypeDef(typeClass = LongArrayType.class, name = "long-array")
public class Question extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Size(max = 20)
    @NotNull
    @Column(name = "code", nullable = false, length = 20)
    private String code;

    @Column(name = "is_enabled")
    private Boolean isEnabled = Boolean.TRUE;

    @Column(name = "level")
    private Integer level;

    @Column(name = "image_ids", columnDefinition = "Long[]")
    @Type(type = "long-array")
    private Long[] imageIds;

    @Column(name = "content")
    private String content;

    @Column(name = "chapter_id")
    private Long chapterId;

    @Column(name = "is_multiple_ans")
    private Boolean isMultipleAnswers;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "question_id", referencedColumnName = "id")
    private List<Answer> lstAnswer;

    @Column(name = "previous_id")
    private Long previousId;

    @Column(name = "base_id")
    private Long baseId;

    @Column(name = "is_newest")
    private Boolean isNewest = Boolean.TRUE;

    @Column(name = "tag_ids", columnDefinition = "Long[]")
    @Type(type = "long-array")
    private Long[] tagIds;

}