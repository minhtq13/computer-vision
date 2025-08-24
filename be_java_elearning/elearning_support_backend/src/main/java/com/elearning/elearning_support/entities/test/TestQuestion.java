package com.elearning.elearning_support.entities.test;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "test_question", schema = "elearning_support_dev")
public class TestQuestion {

    @Id
    @SequenceGenerator(name = "testQuestionIdSeq", sequenceName = "test_question_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "testQuestionIdSeq")
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @Column(name = "test_id", nullable = false)
    private Long testId;

    @NotNull
    @Column(name = "question_id", nullable = false)
    private Long questionId;

    public TestQuestion(Long testId, Long questionId) {
        this.testId = testId;
        this.questionId = questionId;
    }
}