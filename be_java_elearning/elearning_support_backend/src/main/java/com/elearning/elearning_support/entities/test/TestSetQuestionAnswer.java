package com.elearning.elearning_support.entities.test;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "test_set_question_answer")
public class TestSetQuestionAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @Column(name = "test_set_question_id")
    private Long testSetQuestionId;

    @NotNull
    @Column(name = "answer_id", nullable = false)
    private Long answerId;

    @Column(name = "answer_no")
    private Integer answerNo;

}