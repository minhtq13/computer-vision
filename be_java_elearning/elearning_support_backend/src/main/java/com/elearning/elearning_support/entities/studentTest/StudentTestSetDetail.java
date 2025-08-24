package com.elearning.elearning_support.entities.studentTest;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import com.elearning.elearning_support.entities.BaseEntity;
import com.vladmihalcea.hibernate.type.array.IntArrayType;
import com.vladmihalcea.hibernate.type.array.LongArrayType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "student_test_set_detail", schema = "elearning_support_dev")
@TypeDef(name = "int-array", typeClass = IntArrayType.class)
public class StudentTestSetDetail extends BaseEntity {

    @Id
    @SequenceGenerator(name = "studentTestSetDetailsIdSeq", sequenceName = "student_test_set_detail_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "studentTestSetDetailsIdSeq")
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "is_enabled")
    private Boolean isEnabled = Boolean.TRUE;

    @Column(name = "is_corrected")
    private Boolean isCorrected;

    @Column(name = "selected_answer", columnDefinition = "Integer[]")
    @Type(type = "int-array")
    private Integer[] selectedAnswer;

    @Column(name = "student_test_set_id")
    private Long studentTestSetId;

    @Column(name = "test_set_question_id")
    private Long testSetQuestionId;

}