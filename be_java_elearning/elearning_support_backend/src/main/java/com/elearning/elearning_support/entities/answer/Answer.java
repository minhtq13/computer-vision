package com.elearning.elearning_support.entities.answer;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.Type;
import org.springframework.beans.BeanUtils;
import com.elearning.elearning_support.dtos.answer.AnswerReqDTO;
import com.elearning.elearning_support.entities.BaseEntity;
import com.elearning.elearning_support.utils.auth.AuthUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "answer", schema = "elearning_support_dev")
public class Answer extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "is_enabled")
    private Boolean isEnabled;

    @Column(name = "content")
    private String content;

    @Column(name = "is_correct")
    private Boolean isCorrect;

    @Column(name = "question_id")
    private Long questionId;

    public Answer(AnswerReqDTO createDTO){
        BeanUtils.copyProperties(createDTO, this);
        this.setCreatedBy(AuthUtils.getCurrentUserId());
        this.setCreatedAt(new Date());
        this.isEnabled = Boolean.TRUE;
    }

    public Answer(Long questionId, AnswerReqDTO createDTO){
        BeanUtils.copyProperties(createDTO, this);
        this.questionId = questionId;
        this.setCreatedBy(AuthUtils.getCurrentUserId());
        this.setCreatedAt(new Date());
        this.isEnabled = Boolean.TRUE;
    }



}