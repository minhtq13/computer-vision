package com.elearning.elearning_support.entities.examClass;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user_exam_class", schema = "elearning_support_dev")
public class UserExamClass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "exam_class_id", nullable = false)
    private Long examClassId;

    @Column(name = "role_type", nullable = false)
    private Integer roleType;

    public UserExamClass(Long userId, Long examClassId, Integer roleType) {
        this.userId = userId;
        this.examClassId = examClassId;
        this.roleType = roleType;
    }
}